package Yi;

/**
 * The objective vector, 
 * including total horizontal distance, total upward distance, maximal tangent
 * All objectives are to be minimized
 * @author YiMei
 *
 */
public class Objectives implements Comparable<Objectives> {

	private float totalHorizontalDistance;
	private float totalUpDistance;
	private float maxTangent;
	
	public Objectives() { // do nothing
		
	}
	
	public Objectives(float totalHorizontalDistance, float totalUpDistance, float maxTangent) {
		this.totalHorizontalDistance = totalHorizontalDistance;
		this.totalUpDistance = totalUpDistance;
		this.maxTangent = maxTangent;
	}
	
	// a copy of obj
	public Objectives(Objectives obj) {
		this.totalHorizontalDistance = obj.getTotalHorizontalDistance();
		this.totalUpDistance = obj.getTotalUpDistance();
		this.maxTangent = obj.getMaxTangent();
	}
	
	public void setTotalHorizontalDistance(float totalHorizontalDistance) {
		this.totalHorizontalDistance = totalHorizontalDistance;
	}
	
	public void setTotalUpDistance(float totalUpDistance) {
		this.totalUpDistance = totalUpDistance;
	}
	
	public void setMaxTangent(float maxTangent) {
		this.maxTangent = maxTangent;
	}
	
	public float getTotalHorizontalDistance() {
		return totalHorizontalDistance;
	}
	
	public float getTotalUpDistance() {
		return totalUpDistance;
	}
	
	public float getMaxTangent() {
		return maxTangent;
	}
	
	// element-wise addition
	public void selfPlus(Objectives obj) {
		totalHorizontalDistance += obj.getTotalHorizontalDistance();
		totalUpDistance += obj.getTotalUpDistance();
		if (obj.getMaxTangent() > maxTangent) {
			maxTangent = obj.getMaxTangent();
		}
	}
	
	public Objectives plus(Objectives obj) {
		float addedTotalHorizontalDistance = totalHorizontalDistance + obj.getTotalHorizontalDistance();
		float addedTotalUpDistance = totalUpDistance + obj.getTotalUpDistance();
		float addedMaxTangent = maxTangent;
		if (obj.getMaxTangent() > addedMaxTangent) {
			addedMaxTangent = obj.getMaxTangent();
		}
		
		return new Objectives(addedTotalHorizontalDistance, addedTotalUpDistance, addedMaxTangent);
	}
	
	@Override
	public int compareTo(Objectives o) {
		int better = 0;
		int worse = 0;
		
		if (totalHorizontalDistance < 0.99 * o.getTotalHorizontalDistance()) {
			better ++;
		}
		else if (totalHorizontalDistance > 1.01 * o.getTotalHorizontalDistance()) {
			worse ++;
		}
		
		if (totalUpDistance < 0.99 * o.getTotalUpDistance()) {
			better ++;
		}
		else if (totalUpDistance > 1.01 * o.getTotalUpDistance()) {
			worse ++;
		}
		
		if (maxTangent < 0.99 * o.getMaxTangent()) {
			better ++;
		}
		else if (maxTangent > 1.01 * o.getMaxTangent()) {
			worse ++;
		}
		
		if (better > 0 && worse == 0) {
			return -1;
		}
		
		if (worse > 0 && better == 0) {
			return 1;
		}
		
		return 0;
	}
	
	public boolean theSameAs(Objectives o) {
		if (totalHorizontalDistance > 1.01 * o.getTotalHorizontalDistance() ||
				totalHorizontalDistance < 0.99 * o.getTotalHorizontalDistance())
			return false;
		
		if (totalUpDistance > 1.01 * o.getTotalUpDistance() ||
				totalUpDistance < 0.99 * o.getTotalUpDistance())
			return false;
		
		if (maxTangent > 1.01 * o.getMaxTangent() ||
				maxTangent < 0.99 * o.getMaxTangent())
			return false;
		
		return true;
	}
	
	public void printMe() {
		System.out.println("horizontal distance = " + totalHorizontalDistance + 
				", up distance = " + totalUpDistance + ", maxTangent = " + maxTangent);
	}
	
	public void printMeToBuffer(StringBuffer callback) {
		callback.append("horizontal distance = " + totalHorizontalDistance + 
				", up distance = " + totalUpDistance + ", maxTangent = " + maxTangent);
		callback.append(System.getProperty("line.separator"));
	}
	
	public String printMeToString() {
		String string = "horizontal distance = " + totalHorizontalDistance + 
				", up distance = " + totalUpDistance + ", maxTangent = " + maxTangent;
		
		return string;
	}
}
