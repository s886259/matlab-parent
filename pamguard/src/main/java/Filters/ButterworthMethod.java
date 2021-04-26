/*	PAMGUARD - Passive Acoustic Monitoring GUARDianship.
 * To assist in the Detection Classification and Localisation 
 * of marine mammals (cetaceans).
 *  
 * Copyright (C) 2006 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package Filters;

import fftManager.Complex;

/**
 * @author Doug Gillespie
 *         <p>
 *         Pole Zero calculations for Butterworth filters
 *         <p>
 *         Taken from p 179 of Lynn & Fuerst, 1989. Introductory Digital signal
 *         Processing
 * @see IirfFilter
 * 
 */
public class ButterworthMethod extends IIRFilterMethod {

	public ButterworthMethod(double sampleRate, FilterParams filterParams) {
		super(sampleRate, filterParams);
		calculateOmegaValues();
		calculateFilter();
	}

	@Override
	public String filterName() {
		return "Butterworth";
	}

	@Override
	int calculateFilter() {
		int nPoleZeros = filterParams.filterOrder * 2;
		poles = new Complex[nPoleZeros];
		zeros = new Complex[nPoleZeros];

		// taken from Lyn & Fuerst p179
		double mpiterm;
		int m;
		double PRm, PIm;
		double d;
		int GoodPoles = 0;
		Complex TestPoint = new Complex();
		for (m = 0; m <= (filterParams.filterOrder * 2 - 1); m++) {
			mpiterm = mPiTerm(m);
			d = 1.0 - 2.0 * Math.tan(omega1 / 2.0) * Math.cos(mpiterm)
					+ Math.pow(Math.tan(omega1 / 2.0), 2.0);
			if (d == 0.0)
				continue;
			PRm = (1.0 - Math.pow(Math.tan(omega1 / 2.0), 2.0)) / d;
			PIm = 2.0 * Math.tan(omega1 / 2.0) * Math.sin(mpiterm) / d;
			TestPoint = new Complex(PRm, PIm);
			if (TestPoint.norm() <= 1.0000000001) {
				poles[GoodPoles] = new Complex(PRm, PIm);
				zeros[GoodPoles] = new Complex(zeroValue, 0);
				GoodPoles++;
			}
		}
		if (filterParams.filterBand == FilterBand.HIGHPASS) {
			for (int i = 0; i < GoodPoles; i++) {
				poles[i].real *= -1;
			}
		} 
		else if (filterParams.filterBand == FilterBand.BANDPASS) {
			GoodPoles = doBandpassTransformation(poles, zeros, GoodPoles);
		}
		else if (filterParams.filterBand == FilterBand.BANDSTOP) {
			GoodPoles = doBandStopTransformation(poles, zeros, GoodPoles);
		}

		return GoodPoles;

	}
}
