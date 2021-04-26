/**
 * 
 */
package IshmaelDetector;

/**
 * @author Dave Mellinger
 */

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JMenuItem;

import PamController.PamControlledUnitSettings;
import PamController.PamController;
import PamController.PamSettings;
import PamguardMVC.PamDataBlock;

public class MatchFiltControl extends IshDetControl implements PamSettings 
{
	public MatchFiltControl(String unitName) {
		super("Matched Filter Detector", unitName, new MatchFiltParams());
	}
	
	@Override
	public PamDataBlock getDefaultInputDataBlock() {
		return PamController.getInstance().getRawDataBlock(0);
	}

	@Override
	public IshDetFnProcess getNewDetProcess(PamDataBlock defaultDataBlock) {
		return new MatchFiltProcess(this, defaultDataBlock);
	}
	
	/* (non-Javadoc)
	 * @see PamguardMVC.PamControlledUnit#SetupControlledUnit()
	 */
	//@Override
	//public void setupControlledUnit() {
	//	super.setupControlledUnit();
	//	//have it find its own data block - for now, just take the 
	//	//first fft block that can be found.
	//}

	@Override
	public JMenuItem createDetectionMenu(Frame parentFrame) {
		return super.createDetectionMenu(parentFrame, "Matched Filter Settings...");
	}
	
	class menuSmoothingDetection implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
		}
	}

	@Override
	public void showParamsDialog1(Frame parentFrame) {
		MatchFiltParams newParams =
			MatchFiltParamsDialog.showDialog2(parentFrame, (MatchFiltParams)ishDetParams);
		installNewParams(parentFrame, newParams);
	}

	//This is called after a settings file is read.  Copy the newly read settings
	//to matchFiltParams.
	@Override
	public boolean restoreSettings(PamControlledUnitSettings pamControlledUnitSettings) {
		MatchFiltParams newParams = 
			(MatchFiltParams)pamControlledUnitSettings.getSettings();
		//I can't figure out why inputDataSource is not in the newParams
		//returned by getSettings, but sometimes it's not. 
		if (newParams.inputDataSource == null)
			newParams.inputDataSource = ishDetParams.inputDataSource;
		ishDetParams = newParams.clone();
		return super.restoreSettings(pamControlledUnitSettings);
	}

	public Serializable getSettingsReference() {
		return ishDetParams;
	}
	 
	/**
	 * @return An integer version number for the settings
	 */
	public long getSettingsVersion() {
		return MatchFiltParams.serialVersionUID;
	}
}
