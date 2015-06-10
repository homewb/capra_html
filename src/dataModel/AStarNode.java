package dataModel;

/*
 * AStarNode is used for A* algorithm. Apart from information
 * in normal Node, it also records g-cost, h-cost and parent node 
 * for each node.
 */

public class AStarNode {
	private Node node;
	private AStarNode cameFrom;
	private int gCost;
	private int hCost;
	
	public AStarNode(Node node, int gCost, int hCost) {
		this.node = node;
		this.gCost = gCost;
		this.hCost = hCost;
	}
	
	public void setCameFrom(AStarNode cameFrom) {
		this.cameFrom = cameFrom;
	}
	
	public void setgCost(int gCost) {
		this.gCost = gCost;
	}
	
	public void sethCost(int hCost) {
		this.hCost = hCost;
	}
	
	public Node getNode() {
		return node;
	}
	
	public AStarNode getCameFrom() {
		return cameFrom;
	}
	
	public int getGCost() {
		return gCost;
	}
	
	public int getFCost() {
		return gCost + hCost;
	}

}
