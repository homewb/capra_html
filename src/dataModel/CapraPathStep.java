package dataModel;

import routePlannerModel.DistanceCalculator;

/*
 * CapraPathStep records each edge of CapraPathLeg
 */

public class CapraPathStep {
	private Distance distance;
	private Node startNode;
	private Node endNode;
	
	private double vDistance; // the vertical distance
	private double tangent;
	
	public CapraPathStep(Distance distance, Node startNode, Node endNode) {
		this.distance = distance;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public Distance getDistance() {
		return distance;
	}
	
	public Node getEndNode() {
		return endNode;
	}
	
	public Node getStartNode() {
		return startNode;
	}

	public void calcVDistance() {
		vDistance = endNode.getElevation() - startNode.getElevation();
	}
	
	public void calcTangent() {
		tangent = (endNode.getElevation() - startNode.getElevation()) / 
					DistanceCalculator.getDistance(startNode, endNode);
	}
	
	public double getVDistance() {
		return vDistance;
	}
	
	public double getTangent() {
		return tangent;
	}
}
