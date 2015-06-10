package routePlannerModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import dataModel.*;

public class AstarDistance {
	
	public CapraPathLeg search(Graph<Node, Edge> graph, 
			Node source, Node target) {
		Map<String, AStarNode> openSet = new HashMap<String, AStarNode>();
		Map<String, AStarNode> closeSet = new HashMap<String, AStarNode>();
		PriorityQueue<AStarNode> priorityQueue = 
				new PriorityQueue<AStarNode>(11, new AStarNodeComparator());
		AStarNode start = new AStarNode(source, 0, 0);
		
		// calculate total distance of impossible segments
		double distance_impossible = 0;
		
		openSet.put(source.getId(), start);
		priorityQueue.add(start);
		
		AStarNode goal = null;
		while (!priorityQueue.isEmpty()) {
			AStarNode curAStarNode = priorityQueue.poll();
			String currentNodeId = curAStarNode.getNode().getId();
			openSet.remove(currentNodeId);
			
			if (currentNodeId.equals(target.getId())) {
				goal = curAStarNode;
				break;
			}
			
			closeSet.put(currentNodeId, curAStarNode);
			ArrayList<Node> neighbours = graph.getNeighbours(currentNodeId);
			for (Node neighbour: neighbours) {
				AStarNode visited = closeSet.get(neighbour.getId());
				if (visited != null)
					continue;
				
				int gCost = curAStarNode.getGCost() 
						+ Math.round(DistanceCalculator
							.getDistance(curAStarNode.getNode(), neighbour));
				
				AStarNode n = openSet.get(neighbour.getId());
				
				if (n == null || n.getGCost() > gCost) {
					
					if (n != null) {
						openSet.remove(neighbour.getId());
					}
					
					AStarNode newNode = new AStarNode(neighbour, gCost, 
							heuristic(graph, neighbour, target));
					
					newNode.setCameFrom(curAStarNode);
					openSet.put(neighbour.getId(), newNode);
					priorityQueue.add(newNode);
				}	
			}
		}
		
		if (goal != null) {
			Stack<CapraPathStep> stack = new Stack<CapraPathStep>();
			AStarNode current = goal;
			AStarNode parent = goal.getCameFrom();
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
	
	public int heuristic(Graph<Node, Edge> graph, Node next, Node target) {
		float distance = DistanceCalculator.getDistance(next, target);
		int hCost = Math.round(distance);
		
		return hCost;
	}

}
