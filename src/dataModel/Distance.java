package dataModel;

/*
 * This class is used to record the distance between two node in the graph.
 * For more information, please refer to Google Map Directions API.
 */

public class Distance {
	private long value;
	private String text;
	
	public Distance(long value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public String toString() {
		return text;
	}
	
	public long getValue() {
		return value;
	}

}
