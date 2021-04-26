/*
 *  PAMGUARD - Passive Acoustic Monitoring GUARDianship.
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


package rocca;

import PamguardMVC.PamDataBlock;
import PamguardMVC.PamProcess;

/**
 *
 *
 * @author Michael Oswald
 */
public class RoccaLoggingDataBlock extends PamDataBlock<RoccaLoggingDataUnit> {

    public RoccaProcess roccaProcess = null;

    /**
     * Constructor.  Set the natural lifetime to the maximum integer value
     * possible so that the data is never deleted unless the
     */
	public RoccaLoggingDataBlock(PamProcess parentProcess, int channelMap) {
		super(RoccaLoggingDataUnit.class, "Rocca Whistle Stats", parentProcess, channelMap);
        this.roccaProcess = (RoccaProcess) parentProcess;
        this.setNaturalLifetime(Integer.MAX_VALUE/1000);
    }

    /**
     * Override the PamDataBlock clearAll() method, otherwise the detections
     * will be erased everytime the spectrogram is run
     */
    @Override
    public synchronized void clearAll() {
    }



}
