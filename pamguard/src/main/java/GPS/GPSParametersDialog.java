package GPS;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import NMEA.NMEADataUnit;
import PamView.PamDialog;
import PamView.PamGridBagContraints;
import PamView.SourcePanel;
import PamguardMVC.PamDataBlock;

public class GPSParametersDialog extends PamDialog {

	private static GPSParametersDialog gpsParametersDialog;
	
	private GPSParameters gpsParameters;
	
	private static Frame lastFrame;
	
	private ShipDimensionsPanel shipDrawing;
	
	private PredictPanel predictPanel;
	
	private MainStringPanel mainStringPanel;
	
	private ShipDimensionsFields shipDimensionsFields;
	
	private SourcePanel sourcePanel;
	
	private ReadOptions readOptions;
	
	private HeadingPanel headingPanel;
	
	private GPSParametersDialog(Frame parentFrame) {
		super(parentFrame, "GPS Options", false);
//		FlowLayout flowLayout;
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		JPanel p1 = new JPanel();
//		p1.setLayout(new GridBagLayout());
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
//		p1.setLayout(flowLayout = new FlowLayout(FlowLayout.CENTER));
		JPanel p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		JTabbedPane tabbedPane = new JTabbedPane();
		
		sourcePanel = new SourcePanel(this, "Select NMEA Source", NMEADataUnit.class, false, false);
		mainStringPanel = new MainStringPanel();
		readOptions = new ReadOptions();
		
//		GridBagConstraints c = new GridBagConstraints();
//		c.gridx = c.gridy = 0;
//		c.fill = GridBagConstraints.HORIZONTAL;
//		addComponent(p1,sourcePanel.getPanel(), c);
//		c.gridy++;
//		addComponent(p1,mainStringPanel,c);
//		c.gridy++;
//		addComponent(p1,predictPanel = new PredictPanel(), c);
		p1.add(sourcePanel.getPanel());
		p1.add(mainStringPanel);
		p1.add(readOptions);
		

		p2.add(shipDrawing = new ShipDimensionsPanel(new ShipDimensionsDrawing(),
				shipDimensionsFields = new ShipDimensionsFields("ABCD")));
		p2.add(predictPanel = new PredictPanel());
		
		headingPanel = new HeadingPanel();
		
		tabbedPane.addTab("NMEA Source", p1);
		tabbedPane.addTab("Heading", headingPanel);
		tabbedPane.addTab("Vessel", p2);
		p.add(tabbedPane);


		setHelpPoint("mapping.NMEA.docs.ConfiguringGPS");
		setDialogComponent(p);
//		setModal(true);
	}
	private void dimensionsChanged() {
		if (gpsParametersDialog.getParams() == true) {
//			shipDrawing.setDimensions(gpsParameters.dimA, gpsParameters.dimB,
//				gpsParameters.dimC, gpsParameters.dimD);
		}
	}

	class PredictPanel extends JPanel {
		JCheckBox showArrow;
		JTextField arrowLength;
		public PredictPanel() {
			showArrow = new JCheckBox("Show ship heading arrow");
			arrowLength = new JTextField(5);
			GridBagLayout l;
			setLayout(l = new GridBagLayout());
			GridBagConstraints gbs = new GridBagConstraints();
//			this.setBorder(new EmptyBorder(5,5,5,5));
			setBorder(new TitledBorder("Predict ships position"));
			gbs.gridx = gbs.gridy = 0;
			gbs.gridwidth = 3;
			gbs.anchor = GridBagConstraints.WEST;
			addComponent(this, showArrow, gbs);
			gbs.gridy = 1;
			gbs.gridwidth = 1;
			addComponent(this, new JLabel("Arrow Length "), gbs);
			gbs.gridx++;
			addComponent(this, arrowLength, gbs);
			gbs.gridx++;
			addComponent(this, new JLabel(" (seconds)"), gbs);
		}
		void setParams() {
			showArrow.setSelected(gpsParameters.plotPredictedPosition);
			arrowLength.setText(String.format("%d", gpsParameters.predictionTime));
		}
		boolean getParams() {
			gpsParameters.plotPredictedPosition = showArrow.isSelected();
			try {
				gpsParameters.predictionTime = Integer.valueOf(arrowLength.getText());
			}
			catch (NumberFormatException ex) {
				return false;
			}
			return true;
		}
	}
	public static GPSParameters showDialog(Frame parentFrame, GPSParameters gpsParameters) {
		if (parentFrame != lastFrame || gpsParametersDialog == null) {
			gpsParametersDialog = new GPSParametersDialog(parentFrame);
		}
		gpsParametersDialog.gpsParameters = gpsParameters.clone();
		gpsParametersDialog.setParams();
		gpsParametersDialog.setVisible(true);		
		return gpsParametersDialog.gpsParameters;
	}

	private void setParams() {
		sourcePanel.setSource(gpsParameters.nmeaSource);
		mainStringPanel.setParams();
		double[] dims = new double[4];
		dims[0] = gpsParameters.dimA; 
		dims[1] = gpsParameters.dimB; 
		dims[2] = gpsParameters.dimC; 
		dims[3] = gpsParameters.dimD; 
		shipDimensionsFields.setDimensions(dims);
		predictPanel.setParams();
		readOptions.setParams();
		headingPanel.setParams(gpsParameters);

		dimensionsChanged();
	}

	@Override
	public boolean getParams() {
		if (!mainStringPanel.getParams()) {
			return false;
		}
		if (sourcePanel.getSource() == null) {
			return false;
		}
		gpsParameters.nmeaSource = sourcePanel.getSource().getDataName();
		
		double[] dims = shipDimensionsFields.getDimensions();
		if (dims == null || dims.length != 4) return false;
		gpsParameters.dimA = dims[0];
		gpsParameters.dimB = dims[1];
		gpsParameters.dimC = dims[2];
		gpsParameters.dimD = dims[3];
		if (predictPanel.getParams() == false) return false;
		if (readOptions.getParams() == false) return false;
		
		return headingPanel.getParams(gpsParameters);
	}
	
	@Override
	public void cancelButtonPressed() {
		// TODO Auto-generated method stub
		gpsParameters = null;
	}

	@Override
	public void restoreDefaultSettings() {
		// TODO Auto-generated method stub
		
	}
	class ReadOptions extends JPanel implements ActionListener {
		JComboBox readType;
		JTextField timeInterval, courseInterval, speedInterval;
		JTextArea textInfo;
		public ReadOptions() {
			super();
			setBorder(new TitledBorder("Read Options"));
			setLayout(new GridBagLayout());
			GridBagConstraints c = new PamGridBagContraints();
			c.gridwidth = 3;
			addComponent(this, readType = new JComboBox(), c);
			c.gridy++;
			addComponent(this, textInfo = new JTextArea( 3, 20), c);
			textInfo.setBackground(this.getBackground());
			c.gridy++;
			c.gridwidth = 1;
			addComponent(this, new JLabel("Time Interval"), c);
			c.gridx++;
			addComponent(this, timeInterval = new JTextField(5), c);
			c.gridx++;
			addComponent(this, new JLabel("s"), c);
			c.gridy++;
			c.gridx= 0;
			addComponent(this, new JLabel("Speed Interval"), c);
			c.gridx++;
			addComponent(this, speedInterval = new JTextField(5), c);
			c.gridx++;
			addComponent(this, new JLabel("Knots"), c);
			c.gridy++;
			c.gridx=0;
			addComponent(this, new JLabel("Angle Interval"), c);
			c.gridx++;
			addComponent(this, courseInterval = new JTextField(5), c);
			c.gridx++;
			addComponent(this, new JLabel("degrees"), c);
			
			textInfo.setLineWrap(true);
			textInfo.setWrapStyleWord(true);
			textInfo.setEditable(false);
			readType.addItem("Read all GPS data");
			readType.addItem("Read at fixed time intervals");
			readType.addItem("Read on timer or course or speed change");
			readType.addActionListener(this);
			
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			enableControls();
			showInfo();
		}
		
		public void setParams() {
			readType.setSelectedIndex(gpsParameters.readType);
			timeInterval.setText(String.format("%d", gpsParameters.readInterval));
			speedInterval.setText(String.format("%.1f", gpsParameters.speedInterval));
			courseInterval.setText(String.format("%.1f", gpsParameters.courseInterval));
			enableControls();
			showInfo();
		}
		public boolean getParams() {
			gpsParameters.readType = readType.getSelectedIndex();
			try {
				gpsParameters.readInterval = Integer.valueOf(timeInterval.getText());
				gpsParameters.speedInterval = Double.valueOf(speedInterval.getText());
				gpsParameters.courseInterval = Double.valueOf(courseInterval.getText());
			}
			catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
		public void enableControls() {
			int t = readType.getSelectedIndex();
			timeInterval.setEnabled(t > 0);
			speedInterval.setEnabled(t == 2);
			courseInterval.setEnabled(t == 2);
		}
		
		private void showInfo () {
			int t = readType.getSelectedIndex();
			String infoString = "";
			switch (t) {
			case 0:
				infoString = "Read and display all available GPS data";
				break;
			case 1:
				infoString = "Read GPS data at fixed time intervals";
				break;
			case 2:
				infoString = "Read GPS data at fixed time intervals OR when the course or speed change";
				break;
			}
			textInfo.setText(infoString);
		}
		
	}
	class MainStringPanel extends JPanel implements ActionListener {

		JRadioButton rmcString;
		JRadioButton ggaString;
		JTextField rmcInitials;
		JTextField ggaInitials;
		public MainStringPanel() {
			super();
			setBorder(new TitledBorder("Main Nav' data string"));
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			
			ButtonGroup buttonGroup = new ButtonGroup();
			
			addComponent(this, new JLabel("RMC String"), c);
			c.gridx++;
			addComponent(this, rmcString = new JRadioButton(""), c);
			c.gridx++;
			addComponent(this, rmcInitials = new JTextField(2), c);
			c.gridx++;
			addComponent(this, new JLabel(" RMC"), c);
			
			c.gridx = 0;
			c.gridy ++;
			addComponent(this, new JLabel("GGA String"), c);
			c.gridx++;
			addComponent(this, ggaString = new JRadioButton(""), c);
			c.gridx++;
			addComponent(this, ggaInitials = new JTextField(2), c);
			c.gridx++;
			addComponent(this, new JLabel(" GGA"), c);
			
			rmcString.addActionListener(this);
			ggaString.addActionListener(this);
			buttonGroup.add(rmcString);
			buttonGroup.add(ggaString);
		}
		
		public void setParams() {
			rmcString.setSelected(gpsParameters.mainString == GPSParameters.READ_RMC);
			ggaString.setSelected(gpsParameters.mainString == GPSParameters.READ_GGA);
			rmcInitials.setText(gpsParameters.rmcInitials);
			ggaInitials.setText(gpsParameters.ggaInitials);
			enableControls();
		}
		
		public boolean getParams() {
			if (ggaString.isSelected()) {
				gpsParameters.mainString = GPSParameters.READ_GGA;
			}
			else {
				gpsParameters.mainString = GPSParameters.READ_RMC;
			}
			gpsParameters.rmcInitials = rmcInitials.getText();
			if (gpsParameters.rmcInitials.length() != 2) {
				return false;
			}
			gpsParameters.ggaInitials = ggaInitials.getText();
			if (gpsParameters.ggaInitials.length() != 2) {
				return false;
			}
			return true;
		}

		public void actionPerformed(ActionEvent e) {
			enableControls();
		}
		
		private void enableControls() {
			rmcInitials.setEnabled(rmcString.isSelected());
			ggaInitials.setEnabled(ggaString.isSelected());
		}
		
		
	}
	
	class HeadingPanel extends JPanel {
		
		SourcePanel headingSource;
		
		JCheckBox readHeading;
		
		HeadingPanel() {
			super(new BorderLayout());
			setBorder(new TitledBorder("Heading"));
			JPanel boxPanel = new JPanel(new BorderLayout());
			this.add(BorderLayout.CENTER, boxPanel);
			this.add(BorderLayout.NORTH, readHeading = new JCheckBox("Read heading data"));
			readHeading.addActionListener(new ReadHeading());
			headingSource = new SourcePanel(getOwner(), NMEADataUnit.class, false, true);
			boxPanel.add(BorderLayout.NORTH, headingSource.getPanel());
		}
		
		private void setParams(GPSParameters gpsParameters) {
			readHeading.setSelected(gpsParameters.readHeading);
			headingSource.setSource(gpsParameters.headingNMEASource);
			enableControls();
		}
		
		private void enableControls() {
			headingSource.setEnabled(readHeading.isSelected());
		}
		
		private boolean getParams(GPSParameters gpsParameters) {
			gpsParameters.readHeading = readHeading.isSelected();
			PamDataBlock pdb = headingSource.getSource();
			if (pdb == null) {
				gpsParameters.headingNMEASource = null;
				if (gpsParameters.readHeading) {
					return showWarning("Heading data NMEA source must be specified");
				}
			}
			gpsParameters.headingNMEASource = pdb.getDataName();
			return true; 
		}
		
		private class ReadHeading implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				enableControls();
			}
		}
	}

}
