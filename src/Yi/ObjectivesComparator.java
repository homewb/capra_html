package Yi;

import java.util.Comparator;

public class ObjectivesComparator implements Comparator<Objectives> {

	@Override
	public int compare(Objectives o1, Objectives o2) {
		return o1.compareTo(o2);
	}

}
