package Yi;

import dataModel.Node;

public class DijkstraNode {

	private Node node;
	private float distFromSource;
	private DijkstraNode cameFrom;
	
	public DijkstraNode(Node node, float dist) {
		this.node = node;
		this.distFromSource = dist;
	}
	
	public void setCameFrom(DijkstraNode cameFrom) {
		this.cameFrom = cameFrom;
	}
	
	public void setDistFromSource(float dist) {
		this.distFromSource = dist;
	}
	
	public Node getNode() {
		return node;
	}
	
	public DijkstraNode getCameFrom() {
		return cameFrom;
	}
	
	public float getDistFromSource() {
		return distFromSource;
	}
}
