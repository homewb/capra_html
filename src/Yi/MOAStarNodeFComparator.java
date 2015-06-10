package Yi;

import java.util.Comparator;

public class MOAStarNodeFComparator implements Comparator<MOAStarNode> {

	@Override
	public int compare(MOAStarNode o1, MOAStarNode o2) {
		return o1.getFVector().compareTo(o2.getFVector());
	}
}
