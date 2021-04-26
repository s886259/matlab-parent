package SoundRecorder;

import generalDatabase.PamTableDefinition;
import generalDatabase.PamTableItem;
import generalDatabase.SQLLogging;

import java.io.File;
import java.sql.Types;

import PamUtils.PamCalendar;
import PamUtils.PamUtils;
import PamguardMVC.PamDataBlock;
import PamguardMVC.PamDataUnit;

public class RecorderLogger extends SQLLogging {

	PamTableDefinition tableDefinition;
	
	RecorderControl recorderControl;
	
	int dateCol, startCol, endCol, fileCol, srCol, lenCol, nChanCol, chanBitmapCol, startSampleCol, triggerCol;
	
	public RecorderLogger(RecorderControl recorderControl, PamDataBlock pamDataBlock) {
		super(pamDataBlock);
		
		this.recorderControl = recorderControl;
		setUpdatePolicy(SQLLogging.UPDATE_POLICY_OVERWRITE);
		tableDefinition = new PamTableDefinition(recorderControl.getUnitName(), getUpdatePolicy());
		PamTableItem tableItem;
		tableDefinition.addTableItem(tableItem = new PamTableItem("GpsIndex", Types.INTEGER));
		tableItem.setCrossReferenceItem("GpsData", "Id");
		dateCol = tableDefinition.addTableItem(new PamTableItem("SystemDate", Types.TIMESTAMP));
		startCol = tableDefinition.addTableItem(new PamTableItem("Recording Start", Types.TIMESTAMP));
		endCol = tableDefinition.addTableItem(new PamTableItem("Recording End", Types.TIMESTAMP));
		fileCol = tableDefinition.addTableItem(new PamTableItem("File Name", Types.CHAR, 255));
		srCol = tableDefinition.addTableItem(new PamTableItem("SampleRate", Types.DOUBLE));
		lenCol = tableDefinition.addTableItem(new PamTableItem("Length", Types.DOUBLE));
		nChanCol = tableDefinition.addTableItem(new PamTableItem("Channels", Types.INTEGER));
		chanBitmapCol = tableDefinition.addTableItem(new PamTableItem("ChannelBitMap", Types.INTEGER));
		startSampleCol = tableDefinition.addTableItem(new PamTableItem("StartSample", Types.INTEGER));
		triggerCol = tableDefinition.addTableItem(new PamTableItem("Action", Types.CHAR, 255));
		tableDefinition.setUseCheatIndexing(true);
		
		setTableDefinition(tableDefinition);
	}


	@Override
	public void setTableData(PamDataUnit pamDataUnit) {
		RecordingInfo ri = ((RecorderDataUnit) pamDataUnit).getRecordingInfo();
		setColumnData(dateCol, PamCalendar.getTimeStamp(pamDataUnit.getTimeMilliseconds()));
		setColumnData(startCol, PamCalendar.getTimeStamp(ri.startTimeMillis));
		setColumnData(endCol, PamCalendar.getTimeStamp(ri.endTimeMillis));
		File f = new File(ri.fileName);
		setColumnData(fileCol, f.getName());
		setColumnData(srCol, (double) ri.sampleRate);
		setColumnData(lenCol, ((ri.endTimeMillis - ri.startTimeMillis) / 1000.));
		setColumnData(nChanCol, PamUtils.getNumChannels(ri.channelBitMap));
		setColumnData(chanBitmapCol, ri.channelBitMap);
		setColumnData(startSampleCol, (int) ri.startSample);
		setColumnData(triggerCol, ri.trigger);
		
	}
//	@Override
//	public boolean getCanLog() {
//		return true;
//	}
//
//	@Override
//	public PamTableDefinition getTableDefinition() {
//		return tableDefinition;
//	}

}
