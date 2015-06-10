package Yi;

import java.util.ArrayList;
import java.util.List;

/***
 * Nondominated list of objectives
 * @author YiMei
 *
 */

public class ObjectivesNDList {

	private List<Objectives> list;
	
	// initialize an empty list
	public ObjectivesNDList() {
		list = new ArrayList<Objectives>();
	}
	
	public List<Objectives> getList() {
		return list;
	}
	
	// add an element and remain nondominated, return whether the new element is added or not
	public boolean add(Objectives o) {
		boolean isDominated = false; // whether the added element is dominated by some existing elements
		
		// remove the elements dominated by the added element
		for (int i = list.size()-1; i > -1; i--) {
			if (list.get(i).compareTo(o) == -1) {
				isDominated = true;
				break;
			}
			else if (list.get(i).compareTo(o) == 1) {
				list.remove(i);
			}
		}
		
		if (!isDominated) {
			list.add(o);
			return true;
		}
		else {
			return false;
		}
	}
}
