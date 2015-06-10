package Yi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import routePlannerModel.DistanceCalculator;
import routePlannerModel.Graph;
import dataModel.CapraPathLeg;
import dataModel.CapraPathStep;
import dataModel.Distance;
import dataModel.Edge;
import dataModel.Node;

public class Dijkstra {
	
	public CapraPathLeg search(Graph<Node, Edge> graph, 
			Node source, Node target) {
		
		// calculate total distance of impossible segments
		double distance_impossible = 0;
		
		Map<Node, DijkstraNode> labels = new HashMap<Node, DijkstraNode>();
		for (Node node : graph.getNodeList()) {
			DijkstraNode dijNode = new DijkstraNode(node, Float.POSITIVE_INFINITY);
			
			if (node.equals(source)) {
				dijNode.setDistFromSource(0);
			}
			
			labels.put(node, dijNode);
		}

		PriorityQueue<DijkstraNode> priorityQueue = 
				new PriorityQueue<DijkstraNode>(11, new DijkstraNodeComparator());
		
		for (Node node : graph.getNodeList()) {
			
			// add the label of the node to Q
			priorityQueue.add(labels.get(node));
		}
		
		DijkstraNode goal = null;
		while (!priorityQueue.isEmpty()) {
			DijkstraNode curDijNode = priorityQueue.poll();
			priorityQueue.remove(curDijNode);
			
			if (curDijNode.getNode().equals(target)) {
				goal = curDijNode;
				break;
			}
			
			ArrayList<Node> neighbours = graph.getNeighbours(curDijNode.getNode().getId());
			for (Node neighbour: neighbours) {
				float alt = curDijNode.getDistFromSource() + 
						DistanceCalculator.getDistance(curDijNode.getNode(), neighbour);
				
				if (alt < labels.get(neighbour).getDistFromSource()) {
					labels.get(neighbour).setDistFromSource(alt);
					labels.get(neighbour).setCameFrom(curDijNode);
				}
			}
		}
		
		if (goal != null) {
			Stack<CapraPathStep> stack = new Stack<CapraPathStep>();
			DijkstraNode current = goal;
			DijkstraNode parent = goal.getCameFrom();
			double totalDistanceInDoubleValue = 0;
			while (parent != null) {
				double distanceInDoubleValue = 
						DistanceCalculator.getDistance(
								parent.getNode(), current.getNode());
				
				// calculate total distance of impossible segments
				int segmentCost = graph.getCost(parent.getNode(), current.getNode());
				if (segmentCost == 99999) {
					distance_impossible += distanceInDoubleValue;
				}
				
				totalDistanceInDoubleValue += distanceInDoubleValue;
				long distanceInLongValue = 
						Math.round(distanceInDoubleValue);
				String distanceInString = 
						String.valueOf(distanceInLongValue);
				Distance distance = 
						new Distance(distanceInLongValue, distanceInString);
				stack.push(new CapraPathStep(distance, 
						parent.getNode(), current.getNode()));
				current = parent;
				parent = current.getCameFrom();			
			}
			List<CapraPathStep> steps = new ArrayList<CapraPathStep>();
			while (stack.size() > 0) {
				steps.add(stack.pop());
			}
			long totalDistanceInLongValue = 
					Math.round(totalDistanceInDoubleValue);
			String totalDistanceInString = 
					String.valueOf(totalDistanceInLongValue);
			Distance totalDistance = 
					new Distance(totalDistanceInLongValue, 
							totalDistanceInString);
			
			// print total impossible segments
			System.out.println("total distance of impossible segments: " + distance_impossible);
			System.out.println("total distance: " + totalDistanceInLongValue);
			
			return new CapraPathLeg(steps, totalDistance, 
					source.getLocation(), target.getLocation());
		}
		
		return null;
	}
}
