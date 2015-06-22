package dataModel;

import routePlannerModel.DistanceCalculator;

/*
 * CapraPathStep records each edge of CapraPathLeg
 */

public class CapraPathStep {
	private Distance distance;
	private Node startNode;
	private Node endNode;
	
	private double upDistance;
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

	public void calcUpDistance() {
		upDistance = 0;
		if (endNode.getElevation() > startNode.getElevation()) {
			upDistance = endNode.getElevation() - startNode.getElevation();
		}
	}
	
	public void calcTangent() {
		tangent = 0;
		if (endNode.getElevation() > startNode.getElevation()) {
			tangent = (endNode.getElevation() - startNode.getElevation()) / 
					DistanceCalculator.getDistance(startNode, endNode);
		}
	}
	
	public double getUpDistance() {
		return upDistance;
	}
	
	public double getTangent() {
		return tangent;
	}
}
