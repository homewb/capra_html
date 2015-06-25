package Yi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/***
 * Nondominated list of a generic type
 * @author YiMei
 *
 * @param <E>
 */

public class NDList<E> {

	private LinkedList<E> list;
	private Comparator<E> comparator;
	
	// initialize an empty list based on comparator
	public NDList(Comparator<E> comparator1) {
		list = new LinkedList<E>();
		this.comparator = comparator1;
	}
	
	public LinkedList<E> getList() {
		return list;
	}
	
	// add an element and remain nondominated, return whether the new element is added or not
	public boolean add(E o) {
		boolean isDominated = false; // whether the added element is dominated by some existing elements
		
		// remove the elements dominated by the added element
		List<E> toRemove = new LinkedList<E>();
		for (E e : list) {
			if (comparator.compare(e,o) == -1) {
				isDominated = true;
				break;
			}
			else if (comparator.compare(e, o) == 1) {
				toRemove.add(e);
			}
		}
		
		list.removeAll(toRemove);
		
		if (!isDominated) {
			list.add(o);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void remove(E o) {
		list.remove(o);
	}
	
	public void remove(int index) {
		list.remove(index);
	}
	
	public void removeAll(Collection<E> toRemove) {
		list.removeAll(toRemove);
	}
	
	// remove all the elements dominated by o
	public void removeAllDominatedBy(E o) {
		List<E> toRemove = new LinkedList<E>();
		for (E e : list) {
			if (comparator.compare(e, o) == 1) {
				toRemove.add(e);
			}
		}
		
		list.removeAll(toRemove);
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	// whether the list dominates the given element
	public boolean dominates(E o) {
		for (E e : list) {
			if (comparator.compare(e, o) == -1) {
				return true;
			}
		}
		
		return false;
	}
	
	public void printMe() {
		System.out.println("List members:");
		for (E e: list) {
//			System.out.println(e);
			
		}
	}
}
