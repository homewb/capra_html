package dataModel;

import java.util.List;

/*
 * Please refer to the JSON result of Google Maps Directions API
 */

public class GDirectionsRoute {
	private String summary;
	private List<GDirectionsLeg> legs;
	
	public GDirectionsRoute(String summary, List<GDirectionsLeg> legs) {
		this.summary = summary;
		this.legs = legs;
	}
	
	public List<GDirectionsLeg> getLegs() {
		return legs;
	}
	
	public String getSummary() {
		return summary;
	}
}
