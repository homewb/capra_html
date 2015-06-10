package Yi;

import dataModel.Node;

public class MOAStarNode {
	private Node node;
	private MOAStarNode cameFrom;
	private Objectives gVector;
	private Objectives hVector;
	private Objectives fVector;
	
	public MOAStarNode(Node node, MOAStarNode from, Objectives gVector, Objectives hVector) {
		this.node = node;
		this.cameFrom = from;
		this.gVector = gVector;
		this.hVector = hVector;
		this.fVector = gVector.plus(hVector);
	}
	
	public void setCameFrom(MOAStarNode cameFrom) {
		this.cameFrom = cameFrom;
	}
	
	public void setGVector(Objectives gVector) {
		this.gVector = gVector;
	}
	
	public void setHVector(Objectives hVector) {
		this.hVector = hVector;
	}
	
	public Node getNode() {
		return node;
	}
	
	public MOAStarNode getCameFrom() {
		return cameFrom;
	}
	
	public Objectives getGVector() {
		return gVector;
	}
	
	public Objectives getHVector() {
		return hVector;
	}
	
	public Objectives getFVector() {
		return fVector;
	}
	
	// whether the partial path up to here has visited the node
	public boolean visited(Node node) {
		MOAStarNode currNode = cameFrom;
		while (currNode != null) {
			if (currNode.getNode().equals(node)) {
				return true;
			}
			currNode = currNode.getCameFrom();
		}
		
		return false;
	}
	
	public void printMe() {
		System.out.println("Node " + node.getId() + ": from " + cameFrom.getNode().getId());
		System.out.print("g: ");
		gVector.printMe();
		System.out.print("h: ");
		hVector.printMe();
		System.out.print("f: ");
		fVector.printMe();
	}
}
