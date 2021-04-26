package AIS;

import java.io.Serializable;

public class AISParameters implements Serializable, Cloneable {

	public static final long serialVersionUID = 0;
	
	protected String nmeaSource;
	
	/**
	 * Only record AIS data within 
	 * maxRange_kn of the vessel
	 */
	public boolean limitRange = false;
	
	/**
	 * Maximum range for AIS data. Data further off than 
	 * this will be ignored.
	 */
	public double maxRange_km = 10.;
	
	public boolean showTail;
	/**
	 * Tail length in minutes. 
	 */
	public int tailLength = 30;
	
	public boolean showPredictionArrow;
	
	/**
	 * Prediction length in seconds
	 */
	public int predictionLength = 600;
	
	@Override
	protected AISParameters clone()  {
		try {
			return (AISParameters) super.clone();
		}
		catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	
}
