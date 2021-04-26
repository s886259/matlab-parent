package RightWhaleEdgeDetector;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fftManager.Complex;
import fftManager.FFTDataBlock;
import fftManager.FFTDataUnit;
import PamController.PamController;
import PamUtils.PamUtils;
import PamguardMVC.PamConstants;
import PamguardMVC.PamDataBlock;
import PamguardMVC.PamDataUnit;
import PamguardMVC.PamObservable;
import PamguardMVC.PamProcess;

public class RWEProcess extends PamProcess {

	private RWEChannelProcess[] rweChannelProcesses;
	private RWEControl rweControl;
	private FFTDataBlock sourceDataBlock;
	private RWEDataBlock rweDataBlock;
	
	public RWEProcess(RWEControl rweControl) {
		super(rweControl, null);
		this.rweControl = rweControl;
		rweDataBlock = new RWEDataBlock(rweControl.getUnitName(), this, 0);
		addOutputDataBlock(rweDataBlock);
		rweDataBlock.setDatagramProvider(new RWEDatagramProvider());
		rweDataBlock.setOverlayDraw(new RWEOverlayGraphics(rweDataBlock));
		rweDataBlock.setBinaryDataSource(new RWEBinaryDataSource(rweDataBlock));
		rweDataBlock.setCanClipGenerate(true);
	}
	
	@Override
	public void pamStart() {
		setupProcesses();
	}
	@Override
	public void pamStop() {
	}
	
	@Override
	public void newData(PamObservable o, PamDataUnit pamDataUnit) {
		FFTDataUnit fftDataUnit = (FFTDataUnit) pamDataUnit;
		int chan = PamUtils.getSingleChannel(fftDataUnit.getChannelBitmap());
		if (rweChannelProcesses[chan] == null) {
			return;
		}
		else {
			rweChannelProcesses[chan].newData(fftDataUnit);
		}
	}


	public void setupProcesses() {
		
		PamDataBlock db = PamController.getInstance().getDataBlock(FFTDataUnit.class, 
				rweControl.rweParameters.dataSourceName);
		if (db != getSourceDataBlock()) {
			setParentDataBlock(db, true);
			sourceDataBlock = (FFTDataBlock) db;
		}
		rweDataBlock.setChannelMap(rweControl.rweParameters.channelMap);
		if (db == null) {
			return;
		}
		rweDataBlock.setFftLength(sourceDataBlock.getFftLength());
		rweDataBlock.setFftHop(sourceDataBlock.getFftHop());
		rweChannelProcesses = new RWEChannelProcess[PamConstants.MAX_CHANNELS];
		int channelMap = rweControl.rweParameters.channelMap;
		for (int i = 0; i < PamConstants.MAX_CHANNELS; i++) {
			if ((channelMap & (1<<i)) != 0) {
				rweChannelProcesses[i] = new RWEChannelProcess(this, i);
			}
		}
	}

	/**
	 * Class to do absolutely all the processing - peak detection, region detection, etc
	 * for a single channel of the detector. 
	 * @author Doug Gillespie
	 *
	 */
	class RWEChannelProcess {
		private RWEProcess rweProcess;
		private int iChannel;
		int searchBin1, searchBin2;
		double[] backgroundData;
		double[] magData;
		boolean[] oTh;
		boolean first = true;
		double updateConstant[] = new double[2];
		double threshold;
		private List<RWESound> growingSounds;
		private int maxGap;
		private int maxFrequencyGap;
		private int numOT; // number of bins in last analysed slice over threshold
		private int minSoundType;
		private RWClassifier classifier = new RWStandardClassifier();
		public RWEChannelProcess(RWEProcess rweProcess, int iChannel) {
			this.rweProcess = rweProcess;
			this.iChannel = iChannel;
			searchBin1 = (int) (rweControl.rweParameters.startFreq * 
					sourceDataBlock.getFftLength() / getSampleRate());
			searchBin2 = (int) (rweControl.rweParameters.endFreq * 
					sourceDataBlock.getFftLength() / getSampleRate());
			searchBin1 = Math.max(0, searchBin1);
			searchBin2 = Math.min(searchBin2, sourceDataBlock.getFftLength()/2-1);
			backgroundData = new double[searchBin2+1];
			magData = new double[searchBin2+1];
			oTh = new boolean[searchBin2+1];
			updateConstant[0] = rweControl.rweParameters.backgroundConst[0];
			updateConstant[1] = rweControl.rweParameters.backgroundConst[1];
			threshold = rweControl.rweParameters.threshold;
			growingSounds = new LinkedList<RWESound>();
			maxGap = rweControl.rweParameters.maxSoundGap;
			maxFrequencyGap = rweControl.rweParameters.maxFrequencyGap;
			minSoundType = rweControl.rweParameters.minSoundType;
			classifier.setSoundData(getSampleRate(), sourceDataBlock.getFftLength(),
					sourceDataBlock.getFftHop());
		}
		
		
		public void newData(FFTDataUnit fftDataUnit) {
			RWEDetectionPeak[] newPeaks = findPeaks(fftDataUnit.getFftData());
			RWESound[] newSounds = findSounds(fftDataUnit.getTimeMilliseconds(), newPeaks);
			if (newSounds == null) {
				return;
			}
			int nSounds = newSounds.length;
			int soundType;
			RWESound aSound;
			RWEDataUnit rweDataUnit;
			long duration;
			double f[] = new double[2];
			for (int i = 0; i < nSounds; i++) {
				aSound = newSounds[i];
				soundType = classifier.getSoundClass(aSound);
				aSound.soundType = soundType;
				if (soundType >= minSoundType) {
					// output the sound the the output data block. 
//					System.out.println(String.format("Detected sound type %d on channel %d", 
//							soundType, this.iChannel));
					duration = sourceDataBlock.getFftHop() * aSound.duration;
					rweDataUnit = new RWEDataUnit(aSound.timeMilliseconds, 
							1<<iChannel, fftDataUnit.getStartSample()-duration, duration, aSound);
					f[0] = aSound.minFreq * getSampleRate()/sourceDataBlock.getFftLength();
					f[1] = aSound.maxFreq * getSampleRate()/sourceDataBlock.getFftLength();
					rweDataUnit.setFrequency(f);
					rweDataBlock.addPamData(rweDataUnit);
				}
			}
		}
		
		private RWEDetectionPeak[] findPeaks(Complex[] fftData) {
			RWEDetectionPeak[] detectedPeaks = null;
			int nPeaks = 0;
			int nOver = 0;
			for (int i = searchBin1; i <= searchBin2; i++) {
				magData[i] = fftData[i].magsq();
			}
			if (first) {
				first = false;
				for (int i = searchBin1; i <= searchBin2; i++)  {
					backgroundData[i] = magData[i] * 10;
				}
			}
			for (int i = searchBin1; i <= searchBin2; i++)  {
				if (oTh[i] = (magData[i]/threshold) > backgroundData[i]) {
			      // now update the background slowly (over threshold)
			    	backgroundData[i] += (magData[i]-backgroundData[i])/updateConstant[1];
			    	nOver++;
			    }
			    else
			    {
			      // now update the background more quickly (below threshold)
			    	backgroundData[i] += (magData[i]-backgroundData[i])/updateConstant[0];
			    }
			}
			numOT = nOver;
			if (nOver == 0) {
				return null;
			}
			boolean peakOn = false;
			RWEDetectionPeak newPeak = new RWEDetectionPeak(0);
			for (int i = searchBin1; i <= searchBin2; i++)  {
				if (!peakOn) {
					if(oTh[i]) { // start a peak.
						newPeak.bin1 = newPeak.bin2 = newPeak.peakBin = i;
						newPeak.signal = newPeak.maxAmp = magData[i];
						newPeak.noise = backgroundData[i];
						peakOn = true;
					}
					// do nothing it it's still off !
				}
				else {
					if (oTh[i]) { // continue peak
						newPeak.bin2 = i;
						if (magData[i] > newPeak.maxAmp) {
							newPeak.maxAmp = magData[i];
							newPeak.peakBin = i;
							newPeak.signal += magData[i];
							newPeak.noise += backgroundData[i];
						}
					}
					else { // end peak
						if (detectedPeaks == null) {
							detectedPeaks = new RWEDetectionPeak[1];
						}
						else {
							detectedPeaks = Arrays.copyOf(detectedPeaks, nPeaks+1);
						}
						detectedPeaks[nPeaks++] = newPeak;
						newPeak = new RWEDetectionPeak(0);
						peakOn = false;
					}
				}
			}
			return detectedPeaks;
		}

		/**
		 * From the detected peaks, so some region connecting 
		 * and make some sounds. 
		 * @param newPeaks array of new peaks
		 * @return array of completed sounds. 
		 */
		private RWESound[] findSounds(long timeMilliseconds, RWEDetectionPeak[] newPeaks) {
			RWESound[] rweSounds = null;
			int nSounds = 0;
			/*
			 * First see if any old sounds have had their day and send them 
			 * back ...
			 */
			RWESound aSound;
			Iterator<RWESound> sI = growingSounds.iterator();
			while (sI.hasNext()) {
				aSound = sI.next();
				if (aSound.deadCount++ > maxGap) {
					sI.remove();
					aSound.completeSound();
					if (rweSounds == null) {
						rweSounds = new RWESound[1];
					}
					else {
						rweSounds = Arrays.copyOf(rweSounds, nSounds+1);
					}
					rweSounds[nSounds++] = aSound;
				}
			}
			/*
			 *  now go through any sounds left in the list and grow them 
			 *  with the new peaks. 
			 */
			int lastSlice, newSlice = 0;
			int nPeaks = 0;
			if (newPeaks != null) {
				nPeaks = newPeaks.length;
				sI = growingSounds.iterator();
				while (sI.hasNext()) {
					aSound = sI.next();
					if (aSound.peakStolen) continue;
					lastSlice = aSound.sliceCount-1;
					for (int iP = 0; iP < nPeaks; iP++) {
						if (aSound.lowFreq[lastSlice] > newPeaks[iP].bin2+maxFrequencyGap) continue;
						if (aSound.highFreq[lastSlice] < newPeaks[iP].bin1-maxFrequencyGap) continue;
						if (newPeaks[iP].rweSound != null) {
					        // the peak has already been added to another sound.
					        // see which sound is the longest and if this sound is longer, steal the
					        // peak, otherwise, let the other sound keep it
					        if (newPeaks[iP].rweSound.sliceCount >= aSound.sliceCount) continue;
					        // otherwise, shorten that other sound.  
					        newPeaks[iP].rweSound.peakStolen = true;
						}
						newPeaks[iP].rweSound = aSound;
						// if the deadCount is == 0 then a peak has already been added to this
						// sound from this time partition, otherwise we need a new one
						if (aSound.deadCount == 0) {
							aSound.highFreq[newSlice] = newPeaks[iP].bin2;
					        if (newPeaks[iP].maxAmp > aSound.peakAmp[newSlice]) {
					          aSound.peakFreq[newSlice] = newPeaks[iP].peakBin;   
					          aSound.peakAmp[newSlice] = newPeaks[iP].maxAmp;
					        }
					        aSound.signal += newPeaks[iP].signal;
					        aSound.noise += newPeaks[iP].noise;
						}
						else {
							aSound.addPeak(aSound.sliceList[lastSlice] + aSound.deadCount, newPeaks[iP], numOT);
					        newSlice = lastSlice + 1;    
						}
					}	
				}
				/*
				 * Now go through the peaks one more time and any peak which hasn't been 
				 * allocated to a sound is used to create a new sound. 
				 * (line 146 in fprwfindsounds.c)
				 */
				for (int iP = 0; iP < nPeaks; iP++) {
					if (newPeaks[iP].rweSound != null) {
						continue;
					}
					aSound = new RWESound(timeMilliseconds, newPeaks[iP], numOT);
					growingSounds.add(aSound);
				}
			}
			
			return rweSounds;
		}
				
	}

}
