package Yi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Test {

	public static void main(String[] args) {
		PriorityQueue<Integer> Q = new PriorityQueue<Integer>();
		
		Q.add(new Integer(5));
		Q.add(new Integer(1));
		Q.add(new Integer(2));
		Q.add(new Integer(3));
		Q.add(new Integer(3));
		Q.add(new Integer(1));
		Q.add(new Integer(3));
		
		List<Integer> removed = new LinkedList<Integer>();
		for (Integer i : Q) {
			if (i.equals(1)) {
				removed.add(i);
			}
		}
		
		System.out.println(removed);
		
		Q.removeAll(removed);
		
		for (int i : Q) {
			System.out.println(i);
		}
	}
}
