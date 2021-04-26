/**
 * 
 */
package IshmaelLocator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;



import Layout.DisplayPanel;
import Layout.DisplayPanelContainer;
import Layout.DisplayPanelProvider;
import Layout.DisplayProviderList;
import PamUtils.PamUtils;
import PamView.PamColors;
import PamView.PamColors.PamColor;
import PamguardMVC.PamDataBlock;
import PamguardMVC.PamDataUnit;
import PamguardMVC.PamObservable;
import PamguardMVC.PamObserver;
import IshmaelDetector.IshDetFnDataUnit;

/**
 * @author Hisham
 *
 */
public class IshLocGraphics implements DisplayPanelProvider
{
IshLocControl ishLocControl;
	
	public IshLocGraphics(IshLocControl ishLocControl) {
		super();
		DisplayProviderList.addDisplayPanelProvider(this);
		this.ishLocControl = ishLocControl;
	}

	public DisplayPanel createDisplayPanel(DisplayPanelContainer displayPanelContainer) {
		return new IshDisplayPanel(this, displayPanelContainer);
	}

	public String getDisplayPanelName() {
		return ishLocControl.getUnitName() + " Graphics";
//		return "Ishmael Graphics Panel";
	}
    
	class IshDisplayPanel extends DisplayPanel implements PamObserver {
		
		IshLocGraphics ishLocGraphics;
		
		PamDataBlock ishmaelDataPr, ishmaelDataHy;

		public IshDisplayPanel(DisplayPanelProvider displayPanelProvider, DisplayPanelContainer displayPanelContainer) {
			super(displayPanelProvider, displayPanelContainer);
			if (ishLocControl.ishLocProcessPr != null) 
				ishmaelDataPr = ishLocControl.ishLocProcessPr.outputDataBlock;
			if (ishLocControl.ishLocProcessHy != null) 
				ishmaelDataHy = ishLocControl.ishLocProcessHy.outputDataBlock;
			//Each time a unit is added to outputData, this class is informed via update(). 
			if (ishmaelDataPr != null) ishmaelDataPr.addObserver(this); 
			if (ishmaelDataHy != null) ishmaelDataHy.addObserver(this);
		}

		@Override
		public PamObserver getObserverObject() {
			return this;
		}
		
		@Override
		public void containerNotification(DisplayPanelContainer displayContainer, int noteType) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void destroyPanel() {
			if (ishmaelDataPr != null)
				ishmaelDataPr.deleteObserver(this);
			if (ishmaelDataHy != null)
				ishmaelDataHy.deleteObserver(this);
		}

		public String getObserverName() {
			return displayPanelProvider.getDisplayPanelName();
		}

		public long getRequiredDataHistory(PamObservable o, Object arg) {
			return 0;
		}

		public void noteNewSettings() {
			// TODO Auto-generated method stub
			
		}

		public void removeObservable(PamObservable o) {
			// TODO Auto-generated method stub
			
		}

		float sampleRate;
		@Override
		public void masterClockUpdate(long milliSeconds, long sampleNumber) {
			// TODO Auto-generated method stub
			
		}

		public void setSampleRate(float sampleRate, boolean notify) {
			this.sampleRate = sampleRate;
		}

		int lastX = 0;
		int lastY = 0;
		public void update(PamObservable o, PamDataUnit dataUnit1) {
			
			/**
			 * look at RawDataDisplay for an example !
			 */
			
			//PamDataBlock dataBlock = (PamDataBlock) o;
			IshDetFnDataUnit dataUnit = (IshDetFnDataUnit)dataUnit1; 
			double[] detData = dataUnit.getDetData();
			
			//int totalChannels = PamUtils.getNumChannels(dataBlock.getChannelMap());
			int thisChannel = PamUtils.getSingleChannel(dataUnit.getChannelBitmap());
			
			if (thisChannel != 0) return;
			
			double containerDrawPix = displayPanelContainer.getCurrentXPixel();

			BufferedImage image = getDisplayImage();
			if (image == null) return;
			Graphics2D g2d = (Graphics2D) image.getGraphics();
			
			int y = getInnerHeight()-(int)((detData[10]-100) / 40 * getInnerHeight());
			int x = (int) containerDrawPix;
			
			g2d.setColor(PamColors.getInstance().getColor(PamColor.PLAIN));
			
			if (x >= lastX) {
				g2d.drawLine(lastX, lastY, x, y);
			}
			lastX = x;
			lastY = y;
			
			clearImage(x+1, x+3, true);
			
			repaint();
		}
		
	}

}

