package generalDatabase;

import PamguardMVC.DataUnitMatcher;
import PamguardMVC.PamDataUnit;

/**
 * Works with a DataUnitFinder to match PamDataUnits by
 * database index. Can be used with one or two input 
 * arguments. The first is the index searched for, the 
 * second is boolean and if it's true, will require that
 * the index is > 0. 
 * @author Doug Gillespie
 *
 */
public class DatabaseIndexUnitFinder implements DataUnitMatcher {

	@Override
	public boolean match(PamDataUnit dataUnit, Object... criteria) {
		int id = dataUnit.getDatabaseIndex();
		if (criteria.length >= 2 && (Boolean) criteria[1] == true) {
			return (id > 0 && id == (Integer) criteria[0]);
		}
		else {
			return (id == (Integer) criteria[0]);
		}
	}

}
