package dbht.offline;

import dbht.DbHtDataUnit;

public class OfflineDbHtDataUnit extends DbHtDataUnit {

	private int nDatas;
	private int interval;
	
	public OfflineDbHtDataUnit(long timeMilliseconds, int channelBitmap,
			long startSample, long duration) {
		super(timeMilliseconds, channelBitmap, startSample, duration);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the nDatas
	 */
	public int getnDatas() {
		return nDatas;
	}

	/**
	 * @param nDatas the nDatas to set
	 */
	public void setnDatas(int nDatas) {
		this.nDatas = nDatas;
	}

	/**
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

}
