package dataModel;

import java.util.List;

import com.google.code.geocoder.model.LatLng;

/*
 * CapraPathLeg records whole route that is calculated by Capra algorithm
 */

public class CapraPathLeg {
	private List<CapraPathStep> steps;
	private Distance distance;
	private LatLng startLocation;
	private LatLng endLocation;
	
	public CapraPathLeg(List<CapraPathStep> steps, Distance distance, 
			LatLng startLocation, LatLng endLocation) {
		this.steps = steps;
		this.distance = distance;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
	}
	
	public Distance getDistance() {
		return distance;
	}
	
	public LatLng getEndLocation() {
		return endLocation;
	}
	
	public LatLng getStartLocation() {
		return startLocation;
	}
	
	public List<CapraPathStep> getSteps() {
		return steps;
	}

}
