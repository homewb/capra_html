package dataModel;

import java.util.List;

import com.google.code.geocoder.model.LatLng;

/*
 * Please refer to the JSON result of Google Maps Directions API
 */

public class GDirectionsLeg {
	private List<GDirectionsStep> steps;
	private Distance distance;
	private Duration duration;
	private LatLng startLocation;
	private LatLng endLocation;
	private String startAddress;
	private String endAddress;
	
	public GDirectionsLeg(List<GDirectionsStep> steps, 
			Distance distance, Duration duration, 
			LatLng startLocation, LatLng endLocation,
			String startAddress, String endAddress) {
		this.steps = steps;
		this.distance = distance;
		this.duration = duration;
		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
	}
	
	public List<GDirectionsStep> getSteps() {
		return steps;
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
	
	public String getStartAddress() {
		return startAddress;
	}
	
	public String getEndAddress() {
		return endAddress;
	}

}
