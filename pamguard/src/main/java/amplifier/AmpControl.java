package amplifier;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JMenuItem;

import PamController.PamControlledUnit;
import PamController.PamControlledUnitSettings;
import PamController.PamControllerInterface;
import PamController.PamSettingManager;
import PamController.PamSettings;

public class AmpControl extends PamControlledUnit implements PamSettings {

	AmpProcess ampProcess;
	
	AmpParameters ampParameters = new AmpParameters();
	
	public AmpControl(String unitName) {
		super("Signal Amplifier", unitName);
		
		addPamProcess(ampProcess = new AmpProcess(this));
		
		PamSettingManager.getInstance().registerSettings(this);
	}

	public Serializable getSettingsReference() {
		return ampParameters;
	}

	public long getSettingsVersion() {
		return AmpParameters.serialVersionUID;
	}

	public boolean restoreSettings(PamControlledUnitSettings pamControlledUnitSettings) {
		ampParameters = ((AmpParameters) pamControlledUnitSettings.getSettings()).clone();
		return true;
	}

	@Override
	public JMenuItem createDetectionMenu(Frame parentFrame) {
		JMenuItem playbackMenu;
		playbackMenu = new JMenuItem(getUnitName() + " ...");
		playbackMenu.addActionListener(new AmpSettings(this, parentFrame));
		return playbackMenu;
	}
	
	class AmpSettings implements ActionListener {

		Frame parentFrame;
		
		AmpControl ampControl;
		
		public AmpSettings(AmpControl ampControl, Frame parentFrame) {
			this.ampControl = ampControl;
			this.parentFrame = parentFrame;
		}

		public void actionPerformed(ActionEvent e) {

			AmpParameters newParams = AmpDialog.showDialog(parentFrame, ampParameters, ampControl);
			if (newParams != null) {
				newSettings(newParams);
			}
			
		}
		
	}
	
	private void newSettings(AmpParameters ampParameters) {
		this.ampParameters = ampParameters.clone();
		ampProcess.noteNewSettings();
	}
	@Override
	public void notifyModelChanged(int changeType) {
		if (changeType != PamControllerInterface.CHANGED_PROCESS_SETTINGS) {
			newSettings(ampParameters);
		}
	}
	
}
