/**
 * 
 */
package loggerForms.controlDescriptions;

import loggerForms.FormDescription;
import loggerForms.FormsTableItem;
import loggerForms.LoggerForm;
import loggerForms.controls.LoggerControl;

/**
 * @author GrahamWeatherup
 *
 */
public class CdxNewline extends ControlDescription {

	/**
	 * @param formDescription
	 */
	public CdxNewline(FormDescription formDescription) {
		super(formDescription);
		// TODO Auto-generated constructor stub
	}

	@Override
	public FormsTableItem[] getFormsTableItems() {return null;};

	/* (non-Javadoc)
	 * @see loggerForms.controlDescriptions.Cd#makeControl(loggerForms.LoggerForm)
	 */
	@Override
	public LoggerControl makeControl(LoggerForm loggerForm) {
		return null;
	}

}
