package generalDatabase;

import generalDatabase.pamCursor.PamCursor;
import generalDatabase.pamCursor.PamCursorManager;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


import PamView.PamDialog;
import PamView.PamGridBagContraints;

public class DatabaseSpeedDialog extends PamDialog{

	/*
	 * Problem "[Microsoft][ODBC Microsoft Access Driver] Cannot open any more tables."
	 * Take a look at
	 * http://stackoverflow.com/questions/1807934/ms-access-cant-open-any-more-tables-using-jdbcodbcdriver
	 */

	private static DatabaseSpeedDialog singleInstance;

	private PamTableDefinition tableDef;

	private PamTableItem countItem, timeItem, prevIndex;

	private PamTableItem[] colItem;

	private SpeedWorker speedWorker;

	private static final int nExtraColumns = 20;

	private JTextField lastTime, nWrites, lastIndex;

	private DatabaseSpeedDialog(Window parentFrame) {
		super(parentFrame, "Database speed test", false);
		makeTableDef(nExtraColumns);
		tableDef.setUseCheatIndexing(true);

		getOkButton().setText("Start");
		if (checkTableDef() == false) {
			getOkButton().setEnabled(false);
		}
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		mainPanel.add(BorderLayout.NORTH, topPanel);

		topPanel.setBorder(new TitledBorder("Test Output"));
		topPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new PamGridBagContraints();
		addComponent(topPanel, new JLabel("Total Writes: ", JLabel.RIGHT), c);
		c.gridx++;
		addComponent(topPanel, nWrites = new JTextField(6), c);
		c.gridx = 0;
		c.gridy++;
		addComponent(topPanel, new JLabel("Time for last write: ", JLabel.RIGHT), c);
		c.gridx++;
		addComponent(topPanel, lastTime = new JTextField(6), c);
		c.gridx++;
		addComponent(topPanel, new JLabel(" milliseconds", JLabel.LEFT), c);
		c.gridx = 0;
		c.gridy++;
		addComponent(topPanel, new JLabel("Last database index: ", JLabel.RIGHT), c);
		c.gridx++;
		addComponent(topPanel, lastIndex = new JTextField(6), c);
		c.gridx++;
		addComponent(topPanel, new JLabel(" (read back from db)", JLabel.LEFT), c);


		setDialogComponent(mainPanel);
		setModal(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
	}

	void makeTableDef(int nCol) {
		tableDef = new PamTableDefinition("DatabaseSpeedTest", SQLLogging.UPDATE_POLICY_WRITENEW);
		//		tableDef.addTableItem(prevIndex = new PamTableItem("PreviousIndex", Types.INTEGER));
		tableDef.addTableItem(countItem = new PamTableItem("NumberOfWrites", Types.INTEGER));
		tableDef.addTableItem(timeItem = new PamTableItem("LastWriteTime", Types.DOUBLE));
		colItem = new PamTableItem[nCol];
		for (int i = 0; i < nCol; i++) {
			String colName = String.format("ExtraCol%d", i);
			tableDef.addTableItem(colItem[i] = new PamTableItem(colName, Types.DOUBLE));
		}
	}

	private boolean checkTableDef() {
		DBProcess dbProcess = getProcess();
		if (dbProcess == null) {
			return false;
		}
		if (tableDef == null) {
			return false;
		}
		return dbProcess.checkTable(tableDef);
	}

	Connection getConnection() {
		return DBControlUnit.findConnection();
	}

	DBProcess getProcess() {
		if (DBControlUnit.findDatabaseControl() == null) {
			return null;
		}
		return DBControlUnit.findDatabaseControl().getDbProcess();
	}

	public static void showDialog(Frame frame) {
		if (singleInstance == null || singleInstance.getOwner() != frame) {
			singleInstance = new DatabaseSpeedDialog(frame);
		}
		singleInstance.setVisible(true);
	}

	@Override
	public void cancelButtonPressed() {
		stopTest();

	}

	@Override
	public boolean getParams() {
		startTest();
		return false;
	}

	@Override
	public void restoreDefaultSettings() {
		// TODO Auto-generated method stub

	}

	public void newSpeedData(SpeedData data) {
		nWrites.setText(String.format("%d", data.nWrites));
		lastTime.setText(String.format("%3.3f", data.lastTime * 1000.));
		lastIndex.setText(String.format("%d", data.lastIndex));
	}

	private void startTest() {
		if (speedWorker != null) {
			return;
		}
		speedWorker = new SpeedWorker();
		speedWorker.execute();
	}

	private void stopTest() {
		if (speedWorker != null) {
			speedWorker.stop();
			speedWorker = null;
		}
	}

	private class SpeedWorker extends javax.swing.SwingWorker<Integer, SpeedData> {

		private volatile boolean keepRunning = true;
		@Override
		protected Integer doInBackground() throws Exception {
						return doUsingPamCursor();
//			return doUsingTrialCursor();
		}
		private Integer doUsingTrialCursor() {
			//http://coding.derkeiler.com/Archive/Java/comp.lang.java.databases/2003-12/0052.html
			String selStr = "SELECT Id, NumberOfWrites, LastWriteTime FROM DataBaseSpeedTest";
			Connection con = getConnection();
			Statement stmt;
			double lastTime = 0;
			int lastIndex;
			int nWrites = 0;
			long startN, endN;
			try {
				// see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4486195
				stmt = con.createStatement(
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);


				// the following works with MySQL
//				stmt = con.createStatement(
//						ResultSet.TYPE_SCROLL_SENSITIVE,
//						ResultSet.CONCUR_UPDATABLE);

				stmt.setFetchDirection(ResultSet.FETCH_UNKNOWN);
//				stmt.
				ResultSet rs = stmt.executeQuery(selStr);
				Object rowOb;

				while (keepRunning) {
					startN = System.nanoTime();
					rs.moveToInsertRow();
//					rs.updateInt(1, 0);
					rs.updateInt(2, nWrites);
					rs.updateDouble(3, lastTime);
					rs.insertRow();
					rs.last();
					lastIndex = (int) rs.getInt(1);

					endN = System.nanoTime();
					nWrites++;
					lastTime = (double) (endN-startN) / 1.0e9;
					publish(new SpeedData(lastIndex, lastTime, nWrites));
					if (lastIndex < 0) {
						break;
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};

			return null;
		}
		protected Integer doUsingPamCursor() throws Exception {

			keepRunning = true;
			int nWrites = 0;
			double lastTime = 0;
			int lastIndex = -1;
			Connection con;
			long startN, endN;
			con = getConnection();
			DBProcess dbProc = getProcess();
			PamCursor cursor = PamCursorManager.createCursor(tableDef);
			while (keepRunning) {
				// first fill the database tableDef with the last time data.
				countItem.setValue(nWrites);
				tableDef.getUpdateReference().setValue(lastIndex);
				timeItem.setValue(lastTime);
				for (int i = 0; i < nExtraColumns; i++) {
					colItem[i].setValue(lastTime);
				}
				startN = System.nanoTime();
				lastIndex = cursor.immediateInsert(con);
				endN = System.nanoTime();
				nWrites++;
				lastTime = (double) (endN-startN) / 1.0e9;
				publish(new SpeedData(lastIndex, lastTime, nWrites));
				if (lastIndex < 0) {
					break;
				}
			}
			return null;
		}

		public void stop() {
			keepRunning = false;
		}

		@Override
		protected void process(List<SpeedData> chunks) {
			for (SpeedData data:chunks) {
				newSpeedData(data);
			}
		}

	}

	private class SpeedData {
		public double lastTime;
		public int nWrites;
		private int lastIndex;
		/**
		 * @param lastTime
		 * @param nWrites
		 */
		public SpeedData(int lastIndex, double lastTime, int nWrites) {
			super();
			this.lastIndex = lastIndex;
			this.lastTime = lastTime;
			this.nWrites = nWrites;
		}

	}

}
