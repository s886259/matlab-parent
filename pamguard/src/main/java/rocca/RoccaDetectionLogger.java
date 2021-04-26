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


/**
 *
 * Logs detection data to a database.
 *
 * @author Michael Oswald
 *
 */

package rocca;

import generalDatabase.PamTableItem;
import generalDatabase.SQLLogging;

import java.sql.Types;

import PamguardMVC.PamDataBlock;
import PamguardMVC.PamDataUnit;
import generalDatabase.PamTableDefinition;

/**
 * Databse logging information for Rocca statistics.
 * 
 * @author Michael Oswald
 *
 */
public class RoccaDetectionLogger extends SQLLogging {

	RoccaSidePanel roccaSidePanel;
	
	/**
     * These items are used to form the columns within the Table.  Item names
     * are copied from the enum names in RoccaContourDataBlock for convenience
     */
	PamTableItem sightNum;
    PamTableItem speciesList;
    PamTableItem speciesClassCount;
    PamTableItem speciesTreeVotes;
    PamTableItem sightClass;

    /**
     * The RoccaLoggingDataBlock to be saved to the database
     */
    public RoccaSightingDataBlock rsdb = null;

	private PamTableDefinition tableDefinition;

	public static final int STRING_LENGTH = 128;
	public static final int SHORT_STRING_LENGTH = 20;

    /**
     *
     * @param roccaControl
     * @param pamDataBlock
     */
	public RoccaDetectionLogger(RoccaSidePanel roccaSidePanel, PamDataBlock pamDataBlock) {
		super(pamDataBlock);
		setCanView(true);

		this.roccaSidePanel = roccaSidePanel;
        this.rsdb = (RoccaSightingDataBlock) pamDataBlock;
        setUpdatePolicy(SQLLogging.UPDATE_POLICY_OVERWRITE);

		tableDefinition = new PamTableDefinition(rsdb.getDataName(), getUpdatePolicy());
		tableDefinition.addTableItem(sightNum  = new PamTableItem("sightNum", Types.CHAR, SHORT_STRING_LENGTH));
		tableDefinition.addTableItem(speciesList = new PamTableItem("speciesList", Types.CHAR, STRING_LENGTH));
		tableDefinition.addTableItem(speciesClassCount = new PamTableItem("speciesClassCount", Types.CHAR, STRING_LENGTH));
		tableDefinition.addTableItem(speciesTreeVotes = new PamTableItem("speciesTreeVotes", Types.CHAR, STRING_LENGTH));
		tableDefinition.addTableItem(sightClass = new PamTableItem("sightClass", Types.CHAR, SHORT_STRING_LENGTH));

        setTableDefinition(tableDefinition);
	}

    /**
     * Load up the class fields with the data to save
     *
     * @param pamDataUnit The RoccaSightingDataUnit that contains the
     * detection stats to save
     */
	@Override
	public void setTableData(PamDataUnit pamDataUnit) {

        RoccaSightingDataUnit rsdu = (RoccaSightingDataUnit) pamDataUnit;

        sightNum.setValue(rsdu.getSightNum());
        speciesList.setValue(rsdu.createSpList());
        speciesClassCount.setValue(rsdu.createClassCountList());
        speciesTreeVotes.setValue(rsdu.createVoteList());
        sightClass.setValue(rsdu.getSightClass());
	}

    /**
     * Create a new RoccaSightingDataUnit and fill it with values from the database
     *
     * @param timeMilliseconds parameter from the database
     * @param databaseIndex database index
     * @return boolean indicating method success
     */
	@Override
	protected PamDataUnit createDataUnit(long timeMilliseconds, int databaseIndex) {

        /* create new RoccaSightingDataUnit object */
        RoccaSightingDataUnit rsdu = new RoccaSightingDataUnit(timeMilliseconds, 0,0,0);

        /* set the logging database index */
        rsdu.setDatabaseIndex(databaseIndex);
        
        /* put the values from the database into the RoccaSightingDataUnit object */
        rsdu.setSightNum(sightNum.getStringValue());
        rsdu.parseAndSetSpList(speciesList.getStringValue());
        rsdu.parseAndSetClassCountList(speciesClassCount.getStringValue());
        rsdu.parseAndSetVoteList(speciesTreeVotes.getStringValue());
        rsdu.classifySighting(roccaSidePanel.roccaControl.roccaParameters.getSightingThreshold());
        rsdu.setSightClass(sightClass.getStringValue());

        /* save the RoccaLoggingDataUnit to the RoccaLoggingDataBlock */
        rsdb.addPamData(rsdu);
        return rsdu;
    }

}