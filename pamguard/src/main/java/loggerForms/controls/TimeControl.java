package loggerForms.controls;

import java.awt.Dimension;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import PamUtils.PamCalendar;

import loggerForms.LoggerForm;
import loggerForms.controlDescriptions.ControlDescription;

public class TimeControl extends SimpleControl{
//	ControlDescription controlDescription;
//	LoggerForm loggerForm;
	
	private TimeZone timeZone;
	
	public TimeControl(ControlDescription controlDescription,
			LoggerForm loggerForm) {
		super(controlDescription, loggerForm);

		Dimension d = this.textField.getPreferredSize();
		d.width = TimestampControl.TIME_FIELD_LENGTH * textField.getFontMetrics(textField.getFont()).getMaxAdvance()/2;
			
		textField.setPreferredSize(new Dimension(d));
		
		// sort out timezone information
		String tzName = controlDescription.getTopic();
		if (tzName != null && tzName.length() > 0) {
			timeZone = TimeZone.getTimeZone(tzName);
		}
	}
	
	@Override
	public Object getData() {
//		System.out.println(textField.getText());
//		System.out.println(PamCalendar.dateFromDateString(textField.getText()));
//		PamCalendar.dateFromDateString(textField.getText());
		
//		System.out.println(textField.getText());
//		System.out.println(PamCalendar.msFromTimeString(textField.getText()));
//		System.out.println(PamCalendar.getTimeStamp(PamCalendar.msFromTimeString(textField.getText())));
		
		String text=textField.getText();
		
		Long tms=PamCalendar.msFromTimeString(text);
		if (text==null||text.length()==0){
			return null;
		}else{
			if (tms==null){
				dataError=controlDescription.getTitle()+": "+text+" is not a valid time";
				return null;
			}else{
				return PamCalendar.getTimeStamp(tms);
			}
		}
	}
	
	@Override
	public void setData(Object data) {
		if (data == null) {
			super.setData(null);
			return;
		}
		if (data.getClass() == Timestamp.class) {
			long millis = PamCalendar.millisFromTimeStamp((Timestamp) data);
			super.setData(PamCalendar.formatTime(millis));
		}
		else if (data.getClass() == String.class) {
			super.setData(data);
		}
//		super.setData(data);
	}

	@Override
	public int autoUpdate() {
		if (loggerForm.getNewOrEdit()==LoggerForm.EditDataForm) return super.autoUpdate();
		
//		setData(PamCalendar.getLocalTimeStamp(PamCalendar.getTime()));
		long millis = PamCalendar.getTimeInMillis();
		if (timeZone != null) {
			millis += timeZone.getOffset(millis);
		}
		setData(PamCalendar.formatTime(millis));
		return AUTO_UPDATE_SUCCESS;
	}

	public void setDefault() {
//		String def = getControlDescription().getDefaultValue();
//		if(def==null||PamCalendar.dateFromDateString(def)==null){
//			setData(PamCalendar.formatTime(PamCalendar.getTimeInMillis()));
//		}else{
//			setData(getControlDescription().getDefaultValue());
//		}
		// there is no concept of default for time. F1 works to get the current time. 
		setData(null);
	}
	
	@Override
	AbstractFormatter getAbstractformatter() {
		/*
		 * Don't use the dataformatter. It seems to reject any date 
		 * where you just put in minutes. Worse, if you put in null, it takes
		 * the previous valid value and put that's back, even if it was from the 
		 * previous form save !
		 */
		return null;
		
//		DateFormat format= new SimpleDateFormat("HH:mm:ss"); //("H:m:s")
//		DateFormatter formatter = new DateFormatter(format);;
//		return formatter;
		
	}

	
	
	
}
