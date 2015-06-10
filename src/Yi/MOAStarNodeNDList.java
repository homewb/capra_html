package Yi;

import java.util.ArrayList;
import java.util.List;

/***
 * The nondominated list of MOAStarNodes, in terms of the gVector
 * 
 * @author YiMei
 *
 */

public class MOAStarNodeNDList {

	private List<MOAStarNode> list;
	
	public MOAStarNodeNDList() {
		list = new ArrayList<MOAStarNode>();
	}
	
	public List<MOAStarNode> getList() {
		return list;
	}
	
	// add an element and remain nondominated, return whether the new element is added or not
	public boolean add(MOAStarNode o) {
		boolean isDominated = false; // whether the added element is dominated by some existing elements
		
		// remove the elements dominated by the added element
		for (int i = list.size()-1; i > -1; i--) {
			if (list.get(i).getGVector().compareTo(o.getGVector()) == -1) {
				isDominated = true;
				break;
			}
			else if (list.get(i).getGVector().compareTo(o.getGVector()) == 1) {
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
