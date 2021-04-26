package generalDatabase.dataExport;

import PamUtils.PamCalendar;
/**
 * 
 * Class for filtering double of float values in a database table
 * @author Doug Gillespie
 *
 */
public class TimeValueParams extends ValueFilterParams {

	private static final long serialVersionUID = 1L;

	private long minValue;
	
	private long maxValue;
	
	/**
	 * Get the min value as a string
	 * @return min value as a string
	 */
	@Override
	public String getMinValue() {
		return PamCalendar.formatDBDateTime(minValue);
	}
	
	/**
	 * Get the max value as a string
	 * @return max value as a string
	 */
	@Override
	public String getMaxValue() {
		return PamCalendar.formatDBDateTime(maxValue);
	}
	

	@Override
	public String getMinQueryValue() {
		return PamCalendar.formatDBDateTimeQueryString(minValue);
	}

	@Override
	public String getMaxQueryValue() {
		return PamCalendar.formatDBDateTimeQueryString(maxValue);
	}

	/**
	 * Set the minimum value
	 * @param minValue min value as a string
	 * @return true if decoded successfully
	 */
	@Override
	public boolean setMinValue(String minValue) {
		try {
			this.minValue = PamCalendar.msFromDateString(minValue);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Set the maximum value
	 * @param maxValue max value as a string
	 * @return true if decoded successfully
	 */
	@Override
	public boolean setMaxValue(String maxValue) {
		try {
			this.maxValue = PamCalendar.msFromDateString(maxValue);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get the length of a typical text field need to display these data. 
	 * @return the length of a typical text field need to display these data. 
	 */
	@Override
	public int getTextFieldLength() {
		return 20;
	}
	
}
