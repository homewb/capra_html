package dataModel;

import java.util.List;

import routePlannerModel.DistanceCalculator;

import com.google.code.geocoder.model.LatLng;

/*
 * CapraPathLeg records whole route that is calculated by Capra algorithm
 */

public class CapraPathLeg {
	private List<CapraPathStep> steps;
	private Distance distance;
	private LatLng startLocation;
	private LatLng endLocation;
	
	private double upDistance;
	private double maxTangent;
	
	
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
	
	public void calcUpDistance() {
		upDistance = 0;
		for (CapraPathStep step : getSteps()) {
			step.calcVDistance();
			
			if (step.getVDistance() < 0)
				continue;
			
			upDistance += step.getVDistance();
		}
	}

	public void calcMaxTangent() {
		maxTangent = 0;
		for (CapraPathStep step : getSteps()) {
			step.calcTangent();
			double absTangent = Math.abs(step.getTangent());
			
			if (absTangent > maxTangent) {
				maxTangent = absTangent;
			}
		}
	}
	
	public double getUpDistance() {
		return upDistance;
	}
	
	public double getMaxTangent() {
		return maxTangent;
	}
	
	
	public void printMe() {
		for (CapraPathStep step : getSteps()) {
			System.out.println("From node " + step.getStartNode().getId() + "(" + step.getStartNode().getElevation()
					+ ") to node " + step.getEndNode().getId() + "(" + step.getEndNode().getElevation()
					+ "), dist = " + step.getDistance() + ", up = " + step.getVDistance()
					+ ", tangent = " + step.getTangent());
		}
	}
}
