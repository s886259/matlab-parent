package whistleClassifier;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import pamMaths.PamHistogram;
import pamMaths.STD;

public class BasicFragmentStore implements FragmentStore {

	private float sampleRate;
	private double fragmentCount;
	
	protected FragmentParameteriser parameteriser = new QuadraticParameteriser();
	
	private LinkedList<double[]> fragmentParams;
	
	private LinkedList<WhistleFragment> fragments;
	
	
	public BasicFragmentStore(float sampleRate) {
		super();
		this.setSampleRate(sampleRate);
	}

	private double[] latestParams;
	public double[] getLatestParams() {
		return latestParams;
	}

	public Iterator<WhistleFragment> getFragmentIterator() {
		return fragments.iterator();
	}
	
	@Override
	public synchronized void addFragemnt(WhistleFragment newFragment) {

		fragments.add(newFragment);
		
		latestParams = parameteriser.getParameters(newFragment);
		fragmentParams.add(latestParams);

		fragmentCount++;
	}

	@Override
	public synchronized void clearStore() {

		fragmentParams.clear();
		fragments.clear();
		
		fragmentCount = 0;

	}

	@Override
	public PamHistogram getFitHistogram(int fit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getFragmentCount() {
		return fragmentCount;
	}

	@Override
	public PamHistogram getNegInflectionsHistogram() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized double[] getParameterArray() {
		if (fragmentParams == null || fragmentParams.size() < 3) {
			return null;
		}
		int nP = fragmentParams.get(0).length;
		int nParams = nP * 3;
		double[] params = new double[nParams];
		double[][] pList = new double[nP][fragmentParams.size()];
		ListIterator<double[]> iterator = fragmentParams.listIterator();
		double[] next;
		int it = 0;
		while (iterator.hasNext()) {
			next = iterator.next();
			for (int iP = 0; iP < nP; iP++) {
				pList[iP][it] = next[iP]; 
			}
			it++;
		}
		double mean, sig, skew;
		STD std = new STD();
		for (int iP = 0; iP < nP; iP++) {
			mean = std.getMean(pList[iP]);
			sig = std.getSTD();
			skew = std.getSkew();
//			if (Double.isNaN(skew)) {
//				skew = std.getSkew();
//			}
			params[iP*3] = mean;
			params[iP*3+1] = sig;
			params[iP*3+2] = skew;
		}
		return params;
	}	
	

	@Override
	public PamHistogram getPosInflectionsHistogram() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareStore() {
		
		fragments = new LinkedList<WhistleFragment>();
		fragmentParams = new LinkedList<double[]>();

	}

	@Override
	public void scaleStoreData(double scaleFactor) {
//
//		fragmentCount *= scaleFactor;
//		int n = (int) fragmentCount;
//		while(fragments.size() > n) {
//			fragments.remove(0);
//			fragmentParams.remove(0);
//		}

	}

	/**
	 * @param sampleRate the sampleRate to set
	 */
	public void setSampleRate(float sampleRate) {
		this.sampleRate = sampleRate;
		prepareStore();
	}

	/**
	 * @return the sampleRate
	 */
	public float getSampleRate() {
		return sampleRate;
	}


}
