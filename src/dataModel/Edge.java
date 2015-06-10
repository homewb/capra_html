package dataModel;

/*
 * This class refers to each edge in the graph
 */

public class Edge implements Comparable<Edge> {
	private Node sourceNode;
	private Node targetNode;
	private int weight;              
	private float elevationDiffer;
	private float distance;
	
	public Edge(Node sourceNode, Node targetNode) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
	}
	
	public void setElevationDiffer(float elevationDiffer) {
		this.elevationDiffer = elevationDiffer;
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public Node getSourceNode() {
		return sourceNode;
	}
	
	public Node getTargetNode() {
		return targetNode;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public float getElevationDiffer() {
		return elevationDiffer;
	}

	@Override
	public int compareTo(Edge edge) {
		if (distance > edge.getDistance())
			return -1;
		else if (distance == edge.getDistance())
			return 0;
		else
			return 1;
	}

}
