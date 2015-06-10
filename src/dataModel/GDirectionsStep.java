package dataModel;

import com.google.code.geocoder.model.LatLng;

/*
 * Please refer to the JSON result of Google Maps Directions API
 */

public class GDirectionsStep {
	private String htmlInstructions;
	private Distance distance;
	private Duration duration;
	private LatLng startLocation;
	private LatLng endLocation;
	
	public GDirectionsStep(String htmlInstructions, 
			Distance distance, Duration duration,
			LatLng startLocation, LatLng endLocation) {
		this.htmlInstructions = htmlInstructions;
		this.distance = distance;
		this.duration = duration;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
	}
	
	public String getHtmlInstructions() {
		return htmlInstructions;
	}
	
	public Distance getDistance() {
		return distance;
	}
	
	public Duration getDuration() {
		return duration;
	}
	
	public LatLng getStartLocation() {
		return startLocation;
	}
	
	public LatLng getEndLocation() {
		return endLocation;
	}

}
