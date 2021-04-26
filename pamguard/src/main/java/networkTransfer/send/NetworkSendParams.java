package networkTransfer.send;

import java.io.Serializable;
import java.util.ArrayList;

import PamguardMVC.PamDataBlock;

public class NetworkSendParams implements Serializable, Cloneable {

	public static final long serialVersionUID = 1L;

	public String ipAddress = "localhost";
	
	public int portNumber = 8011;
	
	public String password;
	
	public String userId;
	
	public int stationId1;
	
	public int stationId2;
	
	boolean savePassword = true;
	
	/**
	 * Max number of queued Objects. 
	 */
	public int maxQueuedObjects = 1000;
	
	/**
	 * Max queue size in kilobytes
	 */
	public int maxQueueSize = 10000;
	
	private ArrayList<String> selectedDataBlocks;
	
	/**
	 * Set send selection for an individual datablock. 
	 * @param dataBlock datablock 
	 * @param doSend true - will send, false don't send. 
	 */
	public void setDataBlock(PamDataBlock dataBlock, boolean doSend) {
		if (selectedDataBlocks == null) {
			selectedDataBlocks = new ArrayList<String>();
		}
		if (doSend) {
			String foundName = findDataBlock(dataBlock);
			if (foundName == null) {
				selectedDataBlocks.add(dataBlock.getDataName());
			}
		}
		else {
			String found = findDataBlock(dataBlock);
			if (found != null){
				selectedDataBlocks.remove(found);
			}
		}
	}
	
	/**
	 * Find if a datablock is listed in the list of wanted datablocks. 
	 * If it is, return a reference to the string in the array list. 
	 * @param dataBlock datablock. 
	 * @return reference to string name. 
	 */
	public String findDataBlock(PamDataBlock dataBlock) {
		if (selectedDataBlocks == null) {
			return null;
		}
		String dbName = dataBlock.getDataName();
		for (String aName:selectedDataBlocks) {
			if (aName.equals(dbName)) {
				return aName;
			}
		}
		return null;
	}
	
	
	
	@Override
	protected NetworkSendParams clone() {
		try {
			return (NetworkSendParams) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void clearDataBlocks() {
		if (selectedDataBlocks == null) {
			return;
		}
		selectedDataBlocks.clear();
	}
	
	

}
