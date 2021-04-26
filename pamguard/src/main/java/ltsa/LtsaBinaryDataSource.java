package ltsa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import fftManager.Complex;
import PamguardMVC.PamDataUnit;
import binaryFileStorage.BinaryDataSource;
import binaryFileStorage.BinaryHeader;
import binaryFileStorage.BinaryObjectData;
import binaryFileStorage.ModuleFooter;
import binaryFileStorage.ModuleHeader;
import binaryFileStorage.PackedBinaryObject;

public class LtsaBinaryDataSource extends BinaryDataSource {

	private LtsaDataBlock ltsaDataBlock;

	private LtsaControl ltsaControl;

	public LtsaBinaryDataSource(LtsaControl ltsaControl, LtsaDataBlock ltsaDataBlock) {
		super(ltsaDataBlock);
		this.ltsaControl = ltsaControl;
		this.ltsaDataBlock = ltsaDataBlock;
		//		testFFT();
//		testLUT();
	}

	private void testLUT() {
		byte logVal;
		int reconVal;
		int prev = 1;
		for (int v = 0; v <= 32768; v+= 16) {
			logVal = getLogLUTValue(Math.min(v, 32767));
			reconVal = getInvLogLUTValue(logVal);
			System.out.println(String.format("Convert %d to %d and back to %d, step %3.2fdB", 
					v, logVal, reconVal, 20 * Math.log10((double) reconVal / (double) prev)));
			prev = reconVal;
		}

	}

	//	private void testFFT() {
	//		FastFFT fft = new FastFFT();
	//		double[] input = new double[512];
	//		Complex[] output = Complex.allocateComplexArray(256);
	//		for (int i = 0; i < 512; i++) {
	//			input[i] = 0;
	//		}
	//		input[100] = 100;
	//		fft.rfft(input, output, 9);
	//		for (int i = 0; i < 256; i++) {
	//			System.out.println(String.format("FFT output %d = %6.3f+%6.3fi", i, output[i].real, output[i].imag));
	//		}
	//	}

	@Override
	public int getModuleVersion() {
		// 0 was linear scale 16 bit, 1 is log scale 8 bit. 
		return 1;
	}

	@Override
	public String getStreamName() {
		return "LTSA";
	}

	@Override
	public int getStreamVersion() {
		return 0;
	}

	@Override
	public void newFileOpened(File outputFile) {
		// TODO Auto-generated method stub

	}

	private ByteArrayOutputStream bos;
	private DataOutputStream dos;
	private double[] realFFTData;

	/**
	 * Look up table for converting 0 - 32767 scale data to 
	 * a -128 to + 127 8 bit log scale. 
	 */
	private byte[] logLUT;
	/**
	 * Create a log look up table which will scale values between
	 * 0 and 32768 onto a log scale ranging from -128 to + 256;
	 */
	private void createLogLUT() {
		logLUT = new byte[32768];
		logLUT[0] = -128;
		double a = 127.*2./Math.log(32767);
		double b = -127;
		for (int i = 1; i < 32768; i++) {
			logLUT[i] = (byte) (a*Math.log(i) + b);
		}
	}

	private byte getLogLUTValue(int value) {
		if (logLUT == null) {
			createLogLUT();
		}
		return logLUT[value];
	}

	private int[] invLogLUT;
	private void createInvLogLUT() {
		invLogLUT = new int[256];
		double a = 127.*2./Math.log(32767);
		double b = -127;		
		int v;
		for (int i = -127; i < 128; i++) {
			v = i + 128;
			invLogLUT[v] = (int) Math.exp((i-b)/a);
		}
	}

	private int getInvLogLUTValue(byte logValue) {
		if (invLogLUT == null) {
			createInvLogLUT();
		}
		int index = 128 + logValue;
		return invLogLUT[index];
	}

	private LtsaModuleHeader moduleHeader;
	@Override
	public PackedBinaryObject getPackedData(PamDataUnit pamDataUnit) {


		/**
		 * Data are scaled to be int16, but to use the maximum 
		 * range, so the biggest value will always be 32767
		 */
		LtsaDataUnit ltsaDataUnit = (LtsaDataUnit) pamDataUnit;
		Complex[] fftData = ltsaDataUnit.getFftData();
		double a;
		int len = fftData.length;
		if (realFFTData == null || realFFTData.length != len) {
			realFFTData = new double[len];
		}
		double maxVal = 0;
		/**
		 * LTSA FFT data are only ever real, so no need to look at imag part. 
		 */
		for (int i = 0; i < len; i++) {
			maxVal = Math.max(maxVal, realFFTData[i]=fftData[i].real);
		}

		if (dos == null || bos == null) {
			dos = new DataOutputStream(bos = new ByteArrayOutputStream());
		}
		else {
			bos.reset();
		}
		try {
			dos.writeLong(ltsaDataUnit.getStartSample());
			//			dos.writeLong(ltsaDataUnit.getDuration());
			dos.writeInt(ltsaDataUnit.getChannelBitmap());
			dos.writeLong(ltsaDataUnit.getEndMilliseconds());
			dos.writeInt(ltsaDataUnit.getnFFT());
			dos.writeFloat((float) maxVal);
			if (getModuleVersion() == 0) {
				for (int i = 0; i < len; i++) {
					a = realFFTData[i]*32767/maxVal;
					dos.writeShort((int) a);
				}
			}
			else {
				for (int i = 0; i < len; i++) {
					a = realFFTData[i]*32767/maxVal;
					dos.writeByte(getLogLUTValue((int) a));
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
//		getBinaryStorageStream().storeData(1, ltsaDataUnit.getTimeMilliseconds(), bos.toByteArray());
		return new PackedBinaryObject(1, bos.toByteArray());
	}

	@Override
	public PamDataUnit sinkData(BinaryObjectData binaryObjectData,
			BinaryHeader bh, int moduleVersion) {
		ByteArrayInputStream bis = new ByteArrayInputStream(binaryObjectData.getData(), 
				0, binaryObjectData.getDataLength());
		DataInputStream dis = new DataInputStream(bis);


		if (moduleHeader == null) return null;
		int fftLen = ltsaDataBlock.getFftLength();
		// moduleHeader.fftLength does not seem ot be set in Buoy data !!!
		if (fftLen != moduleHeader.fftLength && moduleHeader.fftLength > 0) {
			System.out.println("LTSA Error - wrong FFT length configured in data block " + ltsaDataBlock.getDataName());
			fftLen = moduleHeader.fftLength;
		}
		int fftHop = ltsaDataBlock.getFftHop();
		int len = fftLen/2;

		LtsaDataUnit ltsaDataUnit = null;
		Complex[] fftData;

		long startSample;
		int channelMap;
		long endMillis;
		int nFFT;
		double maxVal;
		long duration;
		byte a;
		try {
			startSample = dis.readLong();
			//			duration = dis.readLong();
			channelMap = dis.readInt();
			endMillis = dis.readLong();
			nFFT = dis.readInt();
			maxVal = dis.readFloat();
			maxVal /= 32767;
			duration = (nFFT-1) * moduleHeader.ffthop + moduleHeader.fftLength;
			ltsaDataUnit = new LtsaDataUnit(binaryObjectData.getTimeMillis(), 
					endMillis, nFFT, channelMap, startSample, duration);
			fftData = ltsaDataUnit.getFftData();
			if (fftData == null || fftData.length != len) {
				fftData = Complex.allocateComplexArray(len);
			}
			if (moduleVersion == 0) {
				for (int i = 0; i < len; i++) {
					fftData[i].real = maxVal * dis.readShort();
				}
			}
			else {
				for (int i = 0; i < len; i++) {
					a = dis.readByte();
					fftData[i].real = maxVal * getInvLogLUTValue(a);
				}
			}

			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		ltsaDataUnit.setFftData(fftData);

		return ltsaDataUnit;
	}

	@Override
	public ModuleFooter sinkModuleFooter(BinaryObjectData binaryObjectData,
			BinaryHeader bh, ModuleHeader moduleHeader) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getModuleHeaderData() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(ltsaDataBlock.getFftLength());
			dos.writeInt(ltsaDataBlock.getFftHop());
			dos.writeInt(ltsaControl.ltsaParameters.intervalSeconds);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	@Override
	public ModuleHeader sinkModuleHeader(BinaryObjectData binaryObjectData,
			BinaryHeader bh) {
		int version = binaryObjectData.getVersionNumber();
		int dataLength = binaryObjectData.getDataLength();
		if (dataLength != 12) {
			System.out.println("Incorrect module header length in LTSA = " + dataLength + " bytes");
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(binaryObjectData.getData());
		DataInputStream dis = new DataInputStream(bis);
		int fftLength = 0, fftHop = 0, intervalSeconds = 0;
		try {
			fftLength = dis.readInt();
			fftHop = dis.readInt();
			intervalSeconds = dis.readInt();
		}
		catch (IOException e) {
			System.out.println("Exception reading LTSA module header data");
			e.printStackTrace();
			return null;
		}
		return moduleHeader = new LtsaModuleHeader(version, fftLength, fftHop, intervalSeconds);
	}

	

}
