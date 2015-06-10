package dataModel;

/*
 * CapraPathStep records each edge of CapraPathLeg
 */

public class CapraPathStep {
	private Distance distance;
	private Node startNode;
	private Node endNode;
	
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

}
