package dataModel;

/*
 * This class is used to record time that will be spent between 
 * two node on the graph.
 * For more information, please refer to Google Maps Directions API.
 */

public class Duration {
	private long value;
	private String text;
	
	public Duration(long value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public long getValue() {
		return value;
	}
	
	public String toString() {
		return text;
	}

}
