package Yi;

import java.util.Comparator;

public class DijkstraNodeComparator implements Comparator<DijkstraNode>  {
	
	@Override
	public int compare(DijkstraNode o1, DijkstraNode o2) {
		if (o1.getDistFromSource() > o2.getDistFromSource())
			return 1;
		else if (o1.getDistFromSource() == o2.getDistFromSource())
			return 0;
		else
			return -1;
	}
}
