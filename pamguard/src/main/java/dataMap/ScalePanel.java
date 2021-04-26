package dataMap;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import PamView.PamBorderPanel;
import PamView.PamCheckBox;
import PamView.PamDialog;
import PamView.PamGridBagContraints;
import PamView.PamLabel;

public class ScalePanel extends PamBorderPanel {
	
	private DataMapControl dataMapControl;
	private DataMapPanel dataMapPanel;
	
	private JSlider hScrollBar;
	private JLabel hScaleText;
	private JComboBox vDropDown;
	private JCheckBox vLogScale;
	private double[] hScaleChoices = DataMapParameters.hScaleChoices;

	public ScalePanel(DataMapControl dataMapControl, DataMapPanel dataMapPanel) {
		setBorder(new TitledBorder("Scales"));
		this.dataMapControl = dataMapControl;
		this.dataMapPanel = dataMapPanel;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new PamGridBagContraints();
		PamDialog.addComponent(this, new PamLabel("Horizontal ", SwingConstants.RIGHT), c);
		c.gridx++;
		PamDialog.addComponent(this, hScrollBar = new JSlider(0, hScaleChoices.length-1), c);
		c.gridx++;
		PamDialog.addComponent(this, hScaleText = new PamLabel(""), c);
		c.gridx = 0;
		c.gridy++;
		PamDialog.addComponent(this, new PamLabel("Vertical ", SwingConstants.RIGHT), c);
		c.gridx++;
		PamDialog.addComponent(this, vDropDown = new JComboBox(), c);
		c.gridx++;
		PamDialog.addComponent(this, vLogScale = new PamCheckBox("Log Scale"), c);
		
		Dimension d = hScrollBar.getPreferredSize();
		d.width = 0;
		hScrollBar.setPreferredSize(d);
		hScrollBar.setToolTipText("Change horizontal scaling");
		
//		hScrollBar.addAdjustmentListener(new HScrollListener());
		hScrollBar.addChangeListener(new HScrollListener());
		
		vDropDown.addActionListener(new VerticalListener());
		vDropDown.setToolTipText("Change vertical scaling");
		vLogScale.addActionListener(new LogScaleListener());
		vLogScale.setToolTipText("Use a log vertical scale");
//		hScrollBar.setMaximum(hScaleChoices.length);
//		hScrollBar.setBlockIncrement(1);
//		hScrollBar.setUnitIncrement(1);
//		hScrollBar.setVisibleAmount(1);
		
		vDropDown.addItem("No Scaling");
		vDropDown.addItem("per Second");
		vDropDown.addItem("per Minute");
		vDropDown.addItem("per Hour");
		vDropDown.addItem("per Day");
		
		setParams(dataMapControl.dataMapParameters);
		
	}
	
	// use setting flag to avoid immediate callback which overwrites changes 2 and 3 !
	boolean setting = false;
	public void setParams(DataMapParameters dataMapParameters) {
		setting = true;
		hScrollBar.setValue(dataMapParameters.hScaleChoice);
		vDropDown.setSelectedIndex(dataMapParameters.vScaleChoice);
		vLogScale.setSelected(dataMapParameters.vLogScale);
		setting = false;
	}
	
	public void getParams(DataMapParameters dataMapParameters) {
		if (setting) return;
		dataMapParameters.hScaleChoice = hScrollBar.getValue();
		dataMapParameters.vScaleChoice = vDropDown.getSelectedIndex();
		dataMapParameters.vLogScale = vLogScale.isSelected();
	}

	private void sayHScale() {
		int hChoice = hScrollBar.getValue();
		hScaleText.setText(String.format("%s pixs/hour", new Double(hScaleChoices[hChoice]).toString()));
	}
	
	class HScrollListener implements AdjustmentListener, ChangeListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent arg0) {
			sayHScale();
			dataMapPanel.scaleChanged();
		}

		@Override
		public void stateChanged(ChangeEvent arg0) {
			sayHScale();
			dataMapPanel.scaleChanged();
		}
	}
	
	class VerticalListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			dataMapPanel.scaleChanged();			
		}
	}
	class LogScaleListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			dataMapPanel.scaleChanged();
		}
	}
		
}
