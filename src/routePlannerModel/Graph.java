package routePlannerModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.google.code.geocoder.model.LatLng;

import dataModel.Edge;
import dataModel.Node;

/*
 * A graph for Capra as well as normal A* algorithm 
 */

public class Graph<V extends Node, E extends Edge> {
	private List<V> nodeList;
	private List<E> edgeList;
	private Map<String, ArrayList<V>> neighbours = new HashMap<String, ArrayList<V>>();
	
	// the adjacent edges
	private Map<String, ArrayList<E>> adjEdges = new HashMap<String, ArrayList<E>>();
	
	public Graph(List<V> nodes, List<E> edges) {
		this.nodeList = nodes;
		this.edgeList = edges;
		
		createAdjacencyLists(nodes, edges);
	}
	
	@SuppressWarnings("unchecked")
	private void createAdjacencyLists(List<V> nodes, List<E> edges) {
		for (int i = 0; i < nodes.size(); i++) {
			neighbours.put(nodes.get(i).getId(), new ArrayList<V>());
			
			adjEdges.put(nodes.get(i).getId(), new ArrayList<E>());
		}
		
		for (E e: edges) {
			String nodeId = e.getSourceNode().getId();
			neighbours.get(nodeId).add((V) e.getTargetNode());
			
			// Only used by GraphXMLLoader
			String nodeId_reverse = e.getTargetNode().getId();
			neighbours.get(nodeId_reverse).add((V) e.getSourceNode());
			
			// add the adjacent edges
			adjEdges.get(nodeId).add(e);
			adjEdges.get(nodeId_reverse).add(e);
		}
	}
	
	public int getCost(Node currentNode, Node nextNode) {
		for (Edge e : edgeList) {
			if (e.getSourceNode().getId() == currentNode.getId() && 
					e.getTargetNode().getId() == nextNode.getId()) {
				return e.getWeight();
			}
			// Only used by GraphXMLLoader
			else if (e.getSourceNode().getId() == nextNode.getId() && 
					e.getTargetNode().getId() == currentNode.getId()) {
				return e.getWeight();
			}
		}
		
		System.out.println("No cost");
		
		return -1;
	}
	
	public float getLongestDistanceInEdges() {
		PriorityQueue<E> pq = new PriorityQueue<E>(edgeList);
		float distance = pq.peek().getDistance();
		return distance;
	}
	
	public List<E> getEdgeList() {
		return edgeList;
	}
	
	public List<V> getNodeList() {
		return nodeList;
	}
	
	public ArrayList<V> getNeighbours(String nodeId) {
		return neighbours.get(nodeId);
	}
	
	public ArrayList<E> getAdjEdges(String nodeId) {
		return adjEdges.get(nodeId);
	}
	
	public Node findNearestNode(LatLng location) {
		Node nearestNode = nodeList.get(0);
		float minDistance = DistanceCalculator.getDistance(
				location, nearestNode.getLocation());
		
		for (int i = 1; i < nodeList.size(); i++) {
			float curDistance = DistanceCalculator.getDistance(
					location, nodeList.get(i).getLocation());
			if (minDistance > curDistance) {
				nearestNode = nodeList.get(i);
				minDistance = curDistance;
			}
		}
		
		return nearestNode;
	}

}
