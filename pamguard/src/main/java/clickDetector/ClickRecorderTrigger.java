package clickDetector;

import java.awt.Window;

import PamguardMVC.PamDataUnit;
import SoundRecorder.trigger.RecorderTrigger;
import SoundRecorder.trigger.RecorderTriggerData;

public class ClickRecorderTrigger extends RecorderTrigger {

	private ClickControl clickControl;
	
	private ClickRecorderTriggerData crtData;

	/**
	 * @param clickControl
	 */
	public ClickRecorderTrigger(ClickControl clickControl) {
		super();
		this.clickControl = clickControl;
	}

	@Override
	public RecorderTriggerData getDefaultTriggerData() {
		if (crtData == null) {
			crtData = new ClickRecorderTriggerData(clickControl);
		}
		return crtData;
	}

	/* (non-Javadoc)
	 * @see SoundRecorder.trigger.RecorderTrigger#hasOptions()
	 */
	@Override
	public boolean hasOptions() {
		return false;
	}

	/* (non-Javadoc)
	 * @see SoundRecorder.trigger.RecorderTrigger#showOptionsDialog(java.awt.Window, SoundRecorder.trigger.RecorderTriggerData)
	 */
	@Override
	public boolean showOptionsDialog(Window frame,
			RecorderTriggerData triggerData) {
		return false;
	}

	/* (non-Javadoc)
	 * @see SoundRecorder.trigger.RecorderTrigger#triggerDataUnit(PamguardMVC.PamDataUnit, SoundRecorder.trigger.RecorderTriggerData)
	 */
	@Override
	public boolean triggerDataUnit(PamDataUnit dataUnit,
			RecorderTriggerData rtData) {
		return true;
	}

}
