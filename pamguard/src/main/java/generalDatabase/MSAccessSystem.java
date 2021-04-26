package generalDatabase;

import generalDatabase.pamCursor.NonScrollablePamCursor;
import generalDatabase.pamCursor.PamCursor;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import javax.swing.JFileChooser;

//import sun.jdbc.odbc.JdbcOdbcDriver;


import PamController.PamControlledUnitSettings;
import PamController.PamSettingManager;
import PamController.PamSettings;
import PamUtils.PamFileFilter;

public class MSAccessSystem extends DBSystem implements PamSettings {

	protected ArrayList<File> recentDatabases;
	
	private DBControl dbControl;
	
	private MSAccessDialogPanel dialogPanel;
	
	private SQLTypes sqlTypes = new MSAccessSQLTypes();
	
	public static final String SYSTEMNAME = "Microsoft Access Database";
	
	private static final String driverClass = "sun.jdbc.odbc.JdbcOdbcDriver";

	public MSAccessSystem(DBControl dbControl, int settingsStore) {
		super();
		this.dbControl = dbControl;
		PamSettingManager.getInstance().registerSettings(this, settingsStore);
		if (recentDatabases == null) {
			recentDatabases = new ArrayList<File>();
		}
	}

	@Override
	String browseDatabases(Component parent) {
		PamFileFilter fileFilter = new PamFileFilter("Microsoft Access Database", ".mdb");
		fileFilter.addFileType(".accdb");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(fileFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if (getDatabaseName() != null) {
			fileChooser.setSelectedFile(new File(getDatabaseName()));
		}
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int state = fileChooser.showOpenDialog(parent);
		if (state == JFileChooser.APPROVE_OPTION) {
			File currFile = fileChooser.getSelectedFile();
			//System.out.println(currFile);
			return currFile.getAbsolutePath();
		}
		return null;
	}

	@Override
	boolean canCreate() {
		return false;
	}

	@Override
	boolean create() {
		return false;
	}

	@Override
	public PamCursor createPamCursor(EmptyTableDefinition tableDefinition) {
		return new NonScrollablePamCursor(tableDefinition);
	}

	@Override
	boolean exists() {

		File file = new File(getDatabaseName());
		if (file == null) return false;
		return file.exists();
		
	}

	@Override
	Connection getConnection() {
			String databaseName = getDatabaseName();
			if (databaseName == null) return null;
			String dbName = databaseName.trim();
			if (dbName == null) return null;
			
			connection = null;
			Exception mdbException = null;
			Exception accdbException = null;
			if (dbName.endsWith(".mdb")) {
				try {
					connection = getMDBConnection(dbName);
				}
				catch (Exception e) {
					mdbException = e;
				}
			}
			if (connection == null) {
				try {
					connection = getACCDBConnection(dbName);
				}
				catch (Exception e) {
					accdbException = e;
				}
			}
			if (connection == null) {
				System.out.println("Unable to open database " + dbName);
				if (mdbException != null) {
					mdbException.printStackTrace();
				}
				if (accdbException != null) {
					accdbException.printStackTrace();
				}
			}
			return connection;
	}
	
	public static Connection getMDBConnection(String dbName) throws Exception {
		if (dbName == null) return null;
		try {
			Class.forName(driverClass);
			// only works for an existing Access database. 
			String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
			database += dbName.trim() + ";DriverID=22;READONLY=false}"; // add on to the end 
			// now we can get the connection from the DriverManager
			Connection connection = DriverManager.getConnection( database ,"",""); 
			return connection;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public static Connection getACCDBConnection(String dbName) throws Exception {
		if (dbName == null) return null;
		try {
			Class.forName(driverClass);
			// only works for an existing Access database. 
			String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=";
			database += dbName.trim() + ";DriverID=22;READONLY=false}"; // add on to the end 
			// now we can get the connection from the DriverManager
			Connection connection = DriverManager.getConnection( database ,"",""); 
			return connection;
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	void closeConnection() {
		
	}


	@Override
		public boolean hasDriver() {
			try {
				Class.forName(driverClass);
			} catch (ClassNotFoundException e) {
				return false;
			}
	//		findAccessDrivers();
			return true;
		}

	private String[] findAccessDrivers() {
	//		Enumeration<Driver> d = DriverManager.getDrivers();
	//		JdbcOdbcDriver jod = null;
	//		try {
	//			jod = (JdbcOdbcDriver) DriverManager.getDriver("jdbc:odbc:Driver");
	//		} catch (SQLException e1) {
	//			jod = null;
	//			e1.printStackTrace();
	//		}
	//		if (jod != null) {
	////			jod.
	//		}
	//		try {
	//			Driver od = DriverManager.getDriver("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)}");
	//			System.out.println("Found standard access mdb driver");
	//		} catch (SQLException e) {
	//			e.printStackTrace();
	//		}
	//		try {
	//			Driver od2 = DriverManager.getDriver("jdbc:odbc:Driver");
	//			System.out.println("Found access mdb / accdb driver");
	//		} catch (SQLException e) {
	//			e.printStackTrace();
	//		}
	//		Driver ad;
	//		while (d.hasMoreElements()) {
	//			ad = d.nextElement();
	//			System.out.println("Database Driver name: " + ad.toString());
	//		}
			return null;
		}

	@Override
	public String getDatabaseName() {
		if (recentDatabases == null) return null;
		if (recentDatabases.size() < 1) return null;
		return recentDatabases.get(0).getAbsolutePath();
	}
	@Override
	public String getShortDatabaseName() {
		if (recentDatabases == null) return null;
		if (recentDatabases.size() < 1) return null;
		return recentDatabases.get(0).getName();
	}

	@Override
	public SystemDialogPanel getDialogPanel(Component parent) {
		if (dialogPanel == null) {
			dialogPanel = new MSAccessDialogPanel(parent, this);
		}
		return dialogPanel;
	}

	@Override
	public SQLTypes getSqlTypes() {
		return sqlTypes;
	}

	
	
	
	//
	//
	//    PAM SETTINGS METHODS
	//
	//
	
	@Override
	String getSystemName() {
		return SYSTEMNAME;
	}

	public String getUnitName() {
		return dbControl.getUnitName();
	}

	public String getUnitType() {
		return "MS Access Database System";
	}

	//
	//
	//    PAM SETTINGS METHODS
	//
	//
	
	public Serializable getSettingsReference() {
		return recentDatabases;
	}

	public long getSettingsVersion() {
		return 0;
	}

	public boolean restoreSettings(PamControlledUnitSettings pamControlledUnitSettings) {
		recentDatabases = (ArrayList<File>) (pamControlledUnitSettings.getSettings());
		return true;
	}

	/* (non-Javadoc)
	 * @see generalDatabase.DBSystem#getKeywords()
	 */
	@Override
	public String getKeywords() {
		// no restrictions at all in MS Access since names
		// are wrapped in "" 
		return null;
	}

	/* (non-Javadoc)
	 * @see generalDatabase.DBSystem#canOpenDatabase()
	 */
	@Override
	public boolean canOpenDatabase() {
		return true;
	}

	/* (non-Javadoc)
	 * @see generalDatabase.DBSystem#openCurrentDatabase()
	 */
	@Override
	public boolean openCurrentDatabase() {
		String databaseName = getDatabaseName();
		if (databaseName == null) return false;
		databaseName = databaseName.trim();
		 try {
		     if (Desktop.isDesktopSupported()) {
		       Desktop.getDesktop().open(new File(databaseName));
		     }
		   } catch (IOException ioe) {
//		     ioe.printStackTrace();
//			   System.out.println("Unable to open ");
		  }

		return true;
	}
}
