package Yi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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

public class MOAStar {

	public List<CapraPathLeg> search(Graph<Node, Edge> graph, 
			Node source, Node target, StringBuffer callback) {
		
		System.out.println("from " + source.getId() + " to " + target.getId());
		
		callback.append(System.getProperty("line.separator"));
		callback.append("from " + source.getId() + " to " + target.getId());
		callback.append(System.getProperty("line.separator"));
		
		PriorityQueue<MOAStarNode> openSet = 
				new PriorityQueue<MOAStarNode>(11, new MOAStarNodeFDistComparator());
		
		// whether the node has been visited, initialized by false
		Map<Node, Boolean> visited = new HashMap<Node, Boolean>();
		for (Node node : graph.getNodeList()) {
			visited.put(node, false);
		}
		
		// the nondominated objective vectors for each node, initialized to be empty
		Map<Node, NDList<Objectives>> openNDGVectors = new HashMap<Node, NDList<Objectives>>();
		Map<Node, NDList<Objectives>> closedNDGVectors = new HashMap<Node, NDList<Objectives>>();
		for (Node node : graph.getNodeList()) {
			openNDGVectors.put(node, new NDList<Objectives>(new ObjectivesComparator()));
			closedNDGVectors.put(node, new NDList<Objectives>(new ObjectivesComparator()));
		}
		
		// initialize for the source
		Objectives sourceGVector = new Objectives(0, 0, 0);
		Objectives sourceHVector = heuristic(graph, source, target);
		MOAStarNode start = new MOAStarNode(source, null, sourceGVector, sourceHVector);
		
		// calculate total distance of impossible segments
		double distance_impossible = 0;
		
		// the upper bound of the horizontal distance, initialized to infinitys
		double distUB = Double.POSITIVE_INFINITY;
		
		// add source to the open set
		openSet.add(start);
		openNDGVectors.get(source).add(sourceGVector);
		
		// nondominated set of the goal, initialized to be empty
		NDList<MOAStarNode> goalNDList = new NDList<MOAStarNode>(new MOAStarNodeGComparator());
		
		while (!openSet.isEmpty()) {
			
			MOAStarNode curAStarNode = openSet.poll();
			
//			System.out.println("current node = " + curAStarNode.getNode().getId() + ", elevation = " + curAStarNode.getNode().getElevation());
//			System.out.print("g values: ");
//			curAStarNode.getGVector().printMe();
//			System.out.print("h values: ");
//			curAStarNode.getHVector().printMe();
			
			// remove this node from the open set
			openSet.remove(curAStarNode);
			
			// move the gVector of the current node from open to closed
			openNDGVectors.get(curAStarNode.getNode()).remove(curAStarNode.getGVector());
			closedNDGVectors.get(curAStarNode.getNode()).add(curAStarNode.getGVector());
			
			// if target is reached, update goalNDList
			if (curAStarNode.getNode().equals(target)) {
				goalNDList.add(curAStarNode);
				
				// update the upper bound of horizontal distance
				if (2 * curAStarNode.getGVector().getTotalHorizontalDistance() < distUB) {
					distUB = 2 * curAStarNode.getGVector().getTotalHorizontalDistance();
				}
				
//				System.out.print("final list: ");
//				
//				callback.append(System.getProperty("line.separator"));
//				callback.append("final list: ");
//				callback.append(System.getProperty("line.separator"));
				
//				for (MOAStarNode node : goalNDList.getList()) {
//					node.printMe();
//					
//					node.printMeToBuffer(callback);
//				}
				
				// remove all the elements in the open set,
				// whose F-vector is dominated by the G-vector of the current node
				// or whose F-horizontal distance is larger than distUB
				List<MOAStarNode> toRemove = new LinkedList<MOAStarNode>();
				for (MOAStarNode node : openSet) {
					if (node.getFVector().getTotalHorizontalDistance() > distUB) {
						toRemove.add(node);
					}
					else if (node.getFVector().compareTo(curAStarNode.getGVector()) == 1) {
						toRemove.add(node);
					}
				}
				openSet.removeAll(toRemove);
			}
	
//			System.out.println("current node: " + curAStarNode.getNode().getId());
//			curAStarNode.getGVector().printMe();
//			System.out.println("non-dominated list:");
//			for (Objectives obj : openNDGVectors.get(curAStarNode.getNode()).getList()) {
//				obj.printMe();
//			}
//			for (Objectives obj : closedNDGVectors.get(curAStarNode.getNode()).getList()) {
//				obj.printMe();
//			}
			
			
			visited.put(curAStarNode.getNode(), true); // this node is visited
			
			// update the open and closed nondominated gVectors
			ArrayList<Node> neighbours = graph.getNeighbours(curAStarNode.getNode().getId());
			
			for (Node neighbour: neighbours) {
				
				if (curAStarNode.visited(neighbour)) {
					// the neighbor will produce a cycle, skip
					continue;
				}
				
				// update the GVector for neighbor
				float edgeLength = DistanceCalculator.getDistance(curAStarNode.getNode(), neighbour);
				float edgeHeight = neighbour.getElevation() - curAStarNode.getNode().getElevation();
				float edgeUpHeight = edgeHeight; // neighbor's height minus curAStarNode's height
				if (edgeUpHeight < 0) {
					edgeUpHeight = 0;
				}
				float edgeAbsHeight = edgeHeight;
				if (edgeAbsHeight < 0) {
					edgeAbsHeight = -edgeAbsHeight;
				}
				float edgeTangent = edgeAbsHeight / edgeLength;
				float neighborHorizontalDistance = 
						curAStarNode.getGVector().getTotalHorizontalDistance() + edgeLength;
				float neighborUpDistance = 
						curAStarNode.getGVector().getTotalUpDistance() + edgeHeight;
				float neighborMaxTangent = 
						curAStarNode.getGVector().getMaxTangent();
				if (neighborMaxTangent < edgeTangent) {
					neighborMaxTangent = edgeTangent;
				}
				Objectives neighborGVector = 
						new Objectives(neighborHorizontalDistance, neighborUpDistance, neighborMaxTangent);
				Objectives neighborHVector = heuristic(graph, neighbour, target);
				
				// generate the corresponding node for MOA* search
				MOAStarNode next = new MOAStarNode(neighbour, curAStarNode, neighborGVector, neighborHVector);
				
				if (next.getFVector().getTotalHorizontalDistance() > distUB) {
					// the F-horizontal distance is larger than the upper bound, skip
					continue;
				}
				
				boolean skip = false; // whether this node is skipped				
				for (MOAStarNode node : goalNDList.getList()) {
					if (node.getGVector().compareTo(next.getFVector()) == -1) {
						// next is dominated by the goal list, skip
						skip = true;
						break;
					}
				}
				
				if (skip)
					continue;
				
//				System.out.println("looping...");
				
				if (!visited.get(neighbour)) {
					// neighbor is new
					openSet.add(next);
					openNDGVectors.get(neighbour).add(neighborGVector);
				}
				else {
					// neighbor is not new, i.e., has been visited
					// check if the current g-values is dominated by any existing g-values
					boolean dominated = false;
					if (openNDGVectors.get(neighbour).dominates(neighborGVector)) {
						dominated = true;
					}
					if (!dominated) {
						if (closedNDGVectors.get(neighbour).dominates(neighborGVector)) {
							dominated = true;
						}
					}
					
					if (!dominated) {
						// remove all the elements dominated by the new g-values
						closedNDGVectors.get(neighbour).removeAllDominatedBy(neighborGVector);
						
						openSet.add(next);
						openNDGVectors.get(neighbour).add(neighborGVector);
					}
				}
			}
		}
		
		
		// remove the duplicated paths
		Collection<MOAStarNode> toRemoveGoals = new LinkedList<MOAStarNode>();
		Iterator<MOAStarNode> ite1 = goalNDList.getList().iterator();
		while (ite1.hasNext()) {
			MOAStarNode node1 = ite1.next();
			Iterator<MOAStarNode> ite2 = ite1;		
			boolean duplicated = false;
			
			while (ite2.hasNext()) {
				MOAStarNode node2 = ite2.next();
				if (node2.getGVector().theSameAs(node1.getGVector())) {
					duplicated = true;
					break;
				}
			}
			
			if (duplicated) {
				toRemoveGoals.add(node1);
			}
		}
		
		System.out.println("removed:");
		for (MOAStarNode node : toRemoveGoals) {
			node.getGVector().printMe();
		}
		
		goalNDList.removeAll(toRemoveGoals);
		
		System.out.println("all list:");
		for (MOAStarNode node : goalNDList.getList()) {
			node.getGVector().printMe();
		}
		
		if (!goalNDList.isEmpty()) {
			
			for (MOAStarNode node : goalNDList.getList()) {
				System.out.println("distance = " + node.getGVector().getTotalHorizontalDistance() +
						", up dist = " + node.getGVector().getTotalUpDistance() + 
						", slope = " + node.getGVector().getMaxTangent());
				
				callback.append("distance = " + node.getGVector().getTotalHorizontalDistance() +
						", up dist = " + node.getGVector().getTotalUpDistance() + 
						", slope = " + node.getGVector().getMaxTangent());
				callback.append(System.getProperty("line.separator"));
			}
			
			List<CapraPathLeg> CapraPaths = new ArrayList<CapraPathLeg>();
			
			for (MOAStarNode goal : goalNDList.getList()) {
				Stack<CapraPathStep> stack = new Stack<CapraPathStep>();
				MOAStarNode current = goal;
				MOAStarNode parent = goal.getCameFrom();
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
				
//				// print total impossible segments
//				System.out.println("total distance of impossible segments: " + distance_impossible);
//				System.out.println("total distance: " + totalDistanceInLongValue);
//				
//				callback.append("total distance of impossible segments: " + distance_impossible);
//				callback.append(System.getProperty("line.separator"));
//				callback.append("total distance: " + totalDistanceInLongValue);
//				callback.append(System.getProperty("line.separator"));
				
				CapraPaths.add(new CapraPathLeg(steps, totalDistance, 
						source.getLocation(), target.getLocation()));
			}
			
			for (CapraPathLeg path : CapraPaths) {
				path.calcUpDistance();
				path.calcMaxTangent();
			}
			
			return CapraPaths;
		}
		return null;
	}
	
	public Objectives heuristic(Graph<Node, Edge> graph, Node next, Node target) {
		// estimate the total horizontal distance
		float h1 = DistanceCalculator.getDistance(next, target);
		
		// estimate the total upward distance
		float h2 = 0;
		if (target.getElevation() > next.getElevation()) {
			h2 = target.getElevation() - next.getElevation();
		}
		// estimate the maximal tangent
		float h3 = 0;
		
		return new Objectives(h1, h2, h3);
	}

	
}
