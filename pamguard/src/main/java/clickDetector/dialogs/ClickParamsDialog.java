/*	PAMGUARD - Passive Acoustic Monitoring GUARDianship.
 * To assist in the Detection Classification and Localisation 
 * of marine mammals (cetaceans).
 *  
 * Copyright (C) 2006 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package clickDetector.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import Localiser.bearingLocaliser.DelayOptionsPanel;
import PamController.PamController;
import PamDetection.RawDataUnit;
import PamView.GroupedSourcePanel;
import PamView.PamDialog;
import PamView.PamGridBagContraints;
import PamView.SourcePanelMonitor;
import PamguardMVC.PamConstants;
import PamguardMVC.PamRawDataBlock;
import clickDetector.ClickControl;
import clickDetector.ClickParameters;
import clickDetector.echoDetection.EchoDetectionSystem;
import clickDetector.echoDetection.EchoDialogPanel;

/**
 * Dialog for click detection parameters.
 * @author Doug
 *
 */
public class ClickParamsDialog extends PamDialog implements SourcePanelMonitor {

	private static ClickParamsDialog singleInstance;

	private ClickParameters clickParameters;
	
	private ClickControl clickControl;

	private JTextField threshold, longFilter, longFilter2, shortFilter;

	private JTextField preSample, postSample, minSep, maxLength;

	private JButton okButton, cancelButton;
	
	private GroupedSourcePanel sourcePanel;
	
	private JCheckBox[] triggerList;
	
	private TriggerPanel triggerPanel;
	
	private LengthPanel lengthPanel;
	
	private NoisePanel noisePanel;
	
	private DelayOptionsPanel delayOptionsPanel;
	
	private EchoDialogPanel echoDialogPanel;
	
	private EchoPanel echoPanel;

	private ClickParamsDialog(Frame parentFrame, ClickControl clickControl) {
		
		super(parentFrame, "Click Detection Parameters", true);

		this.clickControl = clickControl;

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTabbedPane tabbedPane = new JTabbedPane();
		panel.setLayout(new BorderLayout());
		panel.add(BorderLayout.CENTER, tabbedPane);
		sourcePanel = new GroupedSourcePanel(this, "Raw Data Source", RawDataUnit.class, true, false, true);
		excludeDataBlocks();
		sourcePanel.addSourcePanelMonitor(this);
		
		delayOptionsPanel = new DelayOptionsPanel(parentFrame);
		
		EchoDetectionSystem eds = clickControl.getEchoDetectionSystem();
		if (eds != null) {
			echoDialogPanel = eds.getEchoDialogPanel();
			echoPanel = new EchoPanel();
		}
		
		tabbedPane.add("Source", sourcePanel.getPanel());
		tabbedPane.add("Trigger", triggerPanel = new TriggerPanel());
		tabbedPane.add("Click Length", lengthPanel = new LengthPanel());
		tabbedPane.add("Delays", delayOptionsPanel.getMainPanel());
		if (echoPanel != null) {
			tabbedPane.add("Echoes", echoPanel);
		}
		tabbedPane.add("Noise", noisePanel = new NoisePanel());

		setDialogComponent(panel);

		//*************************************************************************
		//Add these lines to enable context sensitive help at the specified target
		this.setHelpPoint("detectors.clickDetectorHelp.docs.ClickDetector_clickDetector");
//		this.enableHelpButton(true);
		//*************************************************************************

	}
	
	private void excludeDataBlocks() {
		PamRawDataBlock[] excludeBlocks = clickControl.getClickDetector().getFilteredDataBlocks();
		if (excludeBlocks == null) {
			return;
		}
		for (int i = 0; i < excludeBlocks.length; i++) {
			sourcePanel.excludeDataBlock(excludeBlocks[i], true);
		}
	}

	public void setParams(ClickParameters clickParameters) {
		// isSetup = false;
		// show them ...
		PamRawDataBlock rawDataBlock = PamController.getInstance().
			getRawDataBlock(clickParameters.rawDataSource);
		sourcePanel.clearExcludeList();
		excludeDataBlocks();
		sourcePanel.excludeDataBlock(clickControl.getClickDetector().getTriggerFunctionDataBlock(), true);
		if (rawDataBlock != null) {
			sourcePanel.setSource(rawDataBlock);
		}
		else {
			sourcePanel.setSourceIndex(0);
		}
		sourcePanel.setChannelList(clickParameters.channelBitmap);
		sourcePanel.setChannelGroups(clickParameters.channelGroups);
		sourcePanel.setGrouping(clickParameters.groupingType);

		threshold.setText(String.format("%3.1f", clickParameters.dbThreshold));
		longFilter.setText(String.format("%3.8f", clickParameters.longFilter));
		longFilter2
				.setText(String.format("%3.8f", clickParameters.longFilter2));
		shortFilter
				.setText(String.format("%3.8f", clickParameters.shortFilter));

		preSample.setText(String.format("%d", clickParameters.preSample));
		postSample.setText(String.format("%d", clickParameters.postSample));
		minSep.setText(String.format("%d", clickParameters.minSep));
		maxLength.setText(String.format("%d", clickParameters.maxLength));
		
		if (clickParameters.triggerBitmap == 0) {
			clickParameters.triggerBitmap = 0xFFFFFFFF;
		}
		
		showTriggerBoxes(clickParameters.channelBitmap);
		
		delayOptionsPanel.setParams(clickParameters.delayMeasurementParams);
		
		noisePanel.setParams();
		
		if (echoPanel != null) {
			echoPanel.setParams();
		}
		
		pack();
	}
	
	private void showTriggerBoxes(int currentChannels) {

		for (int i = 0; i < PamConstants.MAX_CHANNELS; i++) {
			triggerList[i].setVisible((1<<i & currentChannels) != 0);
			triggerList[i].setEnabled((1<<i & currentChannels) != 0);
			triggerList[i].setSelected((1<<i & currentChannels) != 0 && 
					triggerList[i].isVisible() &&
					(1<<i & clickParameters.triggerBitmap) != 0);
			
		}
		
	}

	@Override
	public boolean getParams() {
		PamRawDataBlock rawDataBlock = (PamRawDataBlock) sourcePanel.getSource();
		if (rawDataBlock == null) return false;
		clickParameters.rawDataSource = rawDataBlock.toString();
		clickParameters.channelBitmap = sourcePanel.getChannelList();
		clickParameters.channelGroups = sourcePanel.getChannelGroups();
		clickParameters.groupingType = sourcePanel.getGrouping();
//		if (sourcePanel.getParams() == false) return false;
		try {
			clickParameters.dbThreshold = Double.valueOf(threshold.getText());
			clickParameters.longFilter = Double.valueOf(longFilter.getText());
			clickParameters.longFilter2 = Double.valueOf(longFilter2.getText());
			clickParameters.shortFilter = Double.valueOf(shortFilter.getText());

			clickParameters.preSample = Integer.valueOf(preSample.getText());
			clickParameters.postSample = Integer.valueOf(postSample.getText());
			clickParameters.minSep = Integer.valueOf(minSep.getText());
			clickParameters.maxLength = Integer.valueOf(maxLength.getText());
		} catch (Exception ex) {
			return false;
		}
		clickParameters.triggerBitmap = 0;
		for (int i = 0; i < PamConstants.MAX_CHANNELS; i++) {
			if (triggerList[i].isSelected()) {
				 clickParameters.triggerBitmap |= 1<<i;
			}
		}
		if (delayOptionsPanel.getParams(clickParameters.delayMeasurementParams) == false) {
			return false;
		}

		if (echoPanel != null) {
			if (echoPanel.getParams() == false) {
				return false;
			}
		}
		
		if (noisePanel.getParams() == false) {
			return false;
		}
		return true;
	}

	public static ClickParameters showDialog(Frame parentFrame, ClickControl clickControl, ClickParameters clickParameters) {

		if (singleInstance == null || singleInstance.getOwner() != parentFrame) {
			singleInstance = new ClickParamsDialog(parentFrame, clickControl);
		}
		singleInstance.clickParameters = clickParameters.clone();
		singleInstance.setParams(clickParameters);
		singleInstance.setVisible(true);
		return singleInstance.clickParameters;
	}

	@Override
	public void cancelButtonPressed() {
		clickParameters = null;
	}
	
	@Override
	public void restoreDefaultSettings() {
		ClickParameters defaltParameters = new ClickParameters();
		setParams(defaltParameters);		
	}
	

	@Override
	public void channelSelectionChanged() {
		// Called when the channel selection changes - 
		// need to reconfigure the trigger panel accordingly. 
		showTriggerBoxes(sourcePanel.getChannelList());
	}


	class TriggerPanel extends JPanel {
		TriggerPanel() {
			super();
			threshold = new JTextField(5);
			longFilter = new JTextField(10);
			longFilter2 = new JTextField(10);
			shortFilter = new JTextField(10);
			triggerList = new JCheckBox[PamConstants.MAX_CHANNELS];
			JPanel generalPanel = new JPanel();
			setBorder(BorderFactory.createTitledBorder("Trigger"));
			generalPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(0,5,4,5);
			addComponent(generalPanel, new JLabel("Threshold"), c);
			c.gridx++;
			addComponent(generalPanel, threshold, c);
			c.gridx++;
			addComponent(generalPanel, new JLabel(" dB"), c);
			
			c.gridy++;
			c.gridx = 0;
			c.gridwidth = 1;
			addComponent(generalPanel, new JLabel("Long filter"), c);
			c.gridx++;
			c.gridwidth = 2;
			addComponent(generalPanel, longFilter, c);

			c.gridy++;
			c.gridx = 0;
			c.gridwidth = 1;
			addComponent(generalPanel, new JLabel("Long filter 2"), c);
			c.gridx++;
			c.gridwidth = 2;
			addComponent(generalPanel, longFilter2, c);

			c.gridy++;
			c.gridx = 0;
			c.gridwidth = 1;
			addComponent(generalPanel, new JLabel("Short filter"), c);
			c.gridx++;
			c.gridwidth = 2;
			addComponent(generalPanel, shortFilter, c);
			add(BorderLayout.CENTER, generalPanel);
			
			JPanel listPanel = new JPanel();
			listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
			for (int i = 0; i < PamConstants.MAX_CHANNELS; i++) {
				listPanel.add(triggerList[i] = new JCheckBox(String.format("Ch %d", i)));
			}
			add(BorderLayout.EAST, listPanel);
		}
	}

	class LengthPanel extends JPanel {
		LengthPanel() {
			super();
			preSample = new JTextField(5);
			postSample = new JTextField(5);
			minSep = new JTextField(5);
			maxLength = new JTextField(5);
			setBorder(BorderFactory.createTitledBorder("Click Length"));
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(0,5,4,5);
			addComponent(this, new JLabel("Min Click Separation"), c);
			c.gridx ++;
			addComponent(this, minSep, c);
			c.gridx ++;
			addComponent(this, new JLabel(" samples"), c);
			
			c.gridx = 0;
			c.gridy ++;
			addComponent(this, new JLabel("Max click Length"), c);
			c.gridx ++;
			addComponent(this, maxLength, c);
			c.gridx ++;
			addComponent(this, new JLabel(" samples"), c);

			c.gridx = 0;
			c.gridy ++;
			addComponent(this, new JLabel("pre sample"), c);
			c.gridx ++;
			addComponent(this, preSample, c);
			c.gridx ++;
			addComponent(this, new JLabel(" samples"), c);

			c.gridx = 0;
			c.gridy ++;
			addComponent(this, new JLabel("post samples"), c);
			c.gridx ++;
			addComponent(this, postSample, c);
			c.gridx ++;
			addComponent(this, new JLabel(" samples"), c);
		}
	}
	
	class NoisePanel extends JPanel implements ActionListener {
		private JCheckBox sampleNoise;
		private JTextField noiseInterval;
		NoisePanel() {
			setLayout(new GridBagLayout());
			GridBagConstraints c = new PamGridBagContraints();
			setBorder(new TitledBorder("Noise Sampling"));
			c.gridx = c.gridy = 0;
			c.gridwidth = 3;
			addComponent(this, sampleNoise = new JCheckBox("Create sample noise measurements"), c);
			sampleNoise.addActionListener(this);
			c.gridy++;
			c.gridx = 0;
			c.gridwidth = 1;
			addComponent(this, new JLabel("Interval "), c);
			c.gridx++;
			addComponent(this, noiseInterval = new JTextField(5), c);
			c.gridx++;
			addComponent(this, new JLabel(" s"), c);
			sampleNoise.setToolTipText("<html><b>Warning</b><br>Output RainbowClick files containing noise " +
					"measurements will only be <br>compatible with RainbowClick versions 5.00.0000 and greater</html>");;
		}
		
		protected void setParams() {
			sampleNoise.setSelected(clickParameters.sampleNoise);
			noiseInterval.setText(String.format("%3.1f", clickParameters.noiseSampleInterval));
			enableControls();
		}
		
		protected boolean getParams() {
			try {
				clickParameters.sampleNoise = sampleNoise.isSelected();
				clickParameters.noiseSampleInterval = Double.valueOf(noiseInterval.getText());
			}
			catch (NumberFormatException e) {
				return false;
			}
			if (clickParameters.sampleNoise && clickParameters.noiseSampleInterval <= 0) {
				JOptionPane.showMessageDialog(getParent(), "Noise sample interval must be greater than 0 s",
						"Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
			
		}
		
		private void enableControls() {
			noiseInterval.setEnabled(sampleNoise.isSelected());
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			enableControls();
		}
		
	}

	class EchoPanel extends JPanel {
		private JCheckBox runOnline;
		private JCheckBox discardEchoes;
		EchoPanel() {
			this.setLayout(new BorderLayout());
			JPanel topBit = new JPanel();
			topBit.setBorder(new TitledBorder("Echo detection policy"));
			topBit.setLayout(new GridBagLayout());
			GridBagConstraints c = new PamGridBagContraints();
			addComponent(topBit, runOnline = new JCheckBox("Run Echo Detector online"), c);
			c.gridy++;
			addComponent(topBit, discardEchoes = new JCheckBox("Discard Echoes"), c);
			runOnline.addActionListener(new RunEchoOnline());
			
			this.add(BorderLayout.NORTH, topBit);
			if (echoDialogPanel != null) {
				this.add(BorderLayout.CENTER, echoDialogPanel.getDialogComponent());
			}
		}
		
		void setParams() {
			runOnline.setSelected(clickParameters.runEchoOnline);
			discardEchoes.setSelected(clickParameters.discardEchoes);
			if (echoDialogPanel != null) {
				echoDialogPanel.setParams();
			}
			enableControls();
		}
		
		boolean getParams() {
			clickParameters.runEchoOnline = runOnline.isSelected();
			clickParameters.discardEchoes = discardEchoes.isSelected();
			if (echoDialogPanel != null) {
				if (echoDialogPanel.getParams() == false) {
					return false;
				}
			}
			return true;
		}
		
		private void enableControls() {
			discardEchoes.setEnabled(runOnline.isSelected());
			if (discardEchoes.isEnabled() == false) {
				discardEchoes.setSelected(false);
			}
		}
		
		private class RunEchoOnline implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableControls();
			}
		}
		
	}
}
