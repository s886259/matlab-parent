package dataMap;

import java.io.Serializable;


public class DataMapParameters implements Cloneable, Serializable {

	protected static final long serialVersionUID = 1L;
	
	
	public int vScaleChoice = OfflineDataMap.SCALE_PERHOUR;
	
	public boolean vLogScale = true;
	
	/*
	 * Scale factor for horizontal axis. 
	 */
	public int hScaleChoice = 4;
	public static final double[] hScaleChoices = {0.1, 0.5, 1, 2, 5, 10, 20, 60, 180, 600};
	public double getPixeslPerHour() {
		return hScaleChoices[hScaleChoice];
	}
	
	@Override
	protected DataMapParameters clone() {
		try {
			return (DataMapParameters) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
