package Yi;

import java.util.Comparator;

public class MOAStarNodeFDistComparator implements Comparator<MOAStarNode> {

	@Override
	public int compare(MOAStarNode o1, MOAStarNode o2) {
		if (o1.getFVector().getTotalHorizontalDistance() <
				o2.getFVector().getTotalHorizontalDistance()) {
			return -1;
		}
		
		if (o1.getFVector().getTotalHorizontalDistance() >
				o2.getFVector().getTotalHorizontalDistance()) {
			return 1;
		}
		
		return 0;
	}
}
