package routePlannerModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import com.google.code.geocoder.model.LatLng;

import dataModel.*;

public class AStar {
	
	public CapraPathLeg search(Graph<Node, Edge> graph, Node source, Node target, StringBuffer callback) {
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
				
				int gCost = curAStarNode.getGCost() + 
						graph.getCost(curAStarNode.getNode(), neighbour);
				AStarNode n = openSet.get(neighbour.getId());
				
				if (n == null || n.getGCost() > gCost) {
					
					if (n != null) {
						openSet.remove(neighbour.getId());
					}
					
					// Use heuristic function version 1
//					AStarNode newNode = new AStarNode(neighbour, gCost, 
//							heuristic(graph, neighbour, target));
					
					// Use heuristic function version 2
					AStarNode newNode = new AStarNode(neighbour, gCost, 
							heuristicSimple(graph, neighbour, target));
					
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
			
			// print out total impossible segments
			System.out.println("total distance of impossible segments: " + distance_impossible);
			System.out.println("total distance: " + totalDistanceInLongValue);
			
			callback.append(System.getProperty("line.separator"));
			callback.append("total distance of impossible segments: " + distance_impossible);
			callback.append(System.getProperty("line.separator"));
			callback.append("total distance: " + totalDistanceInLongValue);
			callback.append(System.getProperty("line.separator"));
			
			return new CapraPathLeg(steps, totalDistance, 
					source.getLocation(), target.getLocation());
		}
		
		return null;
	}
	
	/*
	 *  Heuristic function version 1 (normal version)
	 *  - This version requires sending lots of HTTP request to Google
	 *    Elevation server, and therefore, very time-consuming. Apart from
	 *    that, this function will be running out of the limit of requests very
	 *    quickly. 
	 */
	public int heuristic(Graph<Node, Edge> graph, Node next, Node target) {
		ElevationApi elevation = new ElevationApi();
		float heuristicDistance = 2 * graph.getLongestDistanceInEdges();
		float distance = DistanceCalculator.getDistance(next, target);
		float elevation1, elevation2, elevationDiff;
		int numOfSamples;
		ArrayList<LatLng> locations = new ArrayList<LatLng>();
		
		if ((int) (distance / heuristicDistance) < 1)
			numOfSamples = 3;
		else
			numOfSamples = (int) (distance / heuristicDistance) + 1;
		
		locations.add(next.getLocation());
		locations.add(target.getLocation());
		
		List<GElevationResult> result = 
				elevation.getElevationByPath(locations, numOfSamples);
		
		elevation1 = result.get(0).getElevation().floatValue();
		elevation2 = result.get(1).getElevation().floatValue();
		elevationDiff = Math.abs(elevation1 - elevation2);
		
		return GradientWeighter.getRanking(elevationDiff, distance);
	}
	
	/*
	 *  Heuristic function version 2 (simple version)
	 *  - This is a simple version of heuristic function. It won't take lots
	 *    of time compared with the version 1. 
	 */
	public int heuristicSimple(Graph<Node, Edge> graph, Node next, Node target) {
		float distance = DistanceCalculator.getDistance(next, target);
		float hDistance = distance / 15;

		int hCost = Math.round(hDistance);
		
		return hCost;
	}

}
