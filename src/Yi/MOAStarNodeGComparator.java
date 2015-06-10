package Yi;

import java.util.Comparator;

public class MOAStarNodeGComparator implements Comparator<MOAStarNode> {

	@Override
	public int compare(MOAStarNode o1, MOAStarNode o2) {
		return o1.getGVector().compareTo(o2.getGVector());
	}
}
