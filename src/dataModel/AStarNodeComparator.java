package dataModel;
import java.util.Comparator;

/*
 * AStarNodeComparator decides the order of AStarNode in priority queue.
 */

public class AStarNodeComparator implements Comparator<AStarNode> {

	@Override
	public int compare(AStarNode o1, AStarNode o2) {
		if (o1.getFCost() > o2.getFCost())
			return 1;
		else if (o1.getFCost() == o2.getFCost())
			return 0;
		else
			return -1;
	}

}
