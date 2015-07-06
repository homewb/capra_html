package routePlannerModel;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import Yi.MOAStar;

import com.google.code.geocoder.model.LatLng;

import dataModel.*;

public class RoutePlanner {	
	private final static int MOASTAR = 4;  // New MOA algorithm;
	
	private final static String FILE_HEIDELBERG_CONTOUR = 
			"osm_contour_Heidelberg_Large.xml";
	private final static String FILE_FILBERT_SA_CONTOUR_U5 = 
			"filbert_san_francisco_u5.xml";
	private final static String FILE_FILBERT_SA_CONTOUR_U10 = 
			"filbert_san_francisco_u10_edited.xml";
	private final static String FILE_BUKIT_TIMAH_SINGAPORE_U5 = 
			"singapore_u5.xml";
	private final static String FILE_LISBON_U5 = "lisbon_u5.xml";
	
	// store path calculated by different methods
	private List<GDirectionsRoute> routes = 
			new ArrayList<GDirectionsRoute>();	
	private List<CapraPathLeg> paths_MOAStar = new ArrayList<CapraPathLeg>();  // Solutions by MOA-Star
		
	private LatLng origin_LatLng;
	private LatLng destination_LatLng;
	
	private GeocodingApi geocoder = new GeocodingApi();
	private MOAStar moastar = new MOAStar();
	
	private GraphXMLLoader graphXmlLoader;
	private Graph<Node, Edge> graph_WithContour;
	
	private StringBuffer buffer = new StringBuffer();
	private List<String> estimateValues = new ArrayList<String>();
	
	public String getOriginCoordinate(String origin) {		
		origin_LatLng = geocoder.getLocation(origin);
		
		return origin_LatLng.toUrlValue();
	}
	
	public String getDestinationCoordinate(String destination) {
		destination_LatLng = geocoder.getLocation(destination);
		
		return destination_LatLng.toUrlValue();
	}
	
	public boolean setOriginAndDestination(String origin, String destination) {
		LatLng start = geocoder.getLocation(origin);
		LatLng end = geocoder.getLocation(destination);
		
		if (start != null && end != null) {
			origin_LatLng = start;
			destination_LatLng = end;
			
			return true;
		}
		
		return false;
	}
	
	public  String getOriginLatLng() {
		
		return origin_LatLng.toUrlValue();
	}
	
	public String getDestinLatLng() {
		
		return destination_LatLng.toUrlValue();
	}

	
	// get step info from Google solution
	// ========= start point ===============
	public double getStartLat() {		
		LatLng startLatLng = routes.get(0)
                .getLegs()
                .get(0)
                .getSteps()
                .get(0)
                .getStartLocation();
		
		return startLatLng.getLat().doubleValue();
	}
	
	public double getStartLng() {		
		LatLng startLatLng = routes.get(0)
                .getLegs()
                .get(0)
                .getSteps()
                .get(0)
                .getStartLocation();
		
		return startLatLng.getLng().doubleValue();
	}
	
	public double getEndLat() {		
		int size = routes.get(0).getLegs().get(0).getSteps().size();
		LatLng endLatLng = routes.get(0)
				                 .getLegs()
				                 .get(0)
				                 .getSteps()
				                 .get(size - 1)
				                 .getEndLocation();
		
		return endLatLng.getLat().doubleValue();
	}
	
	public double getEndLng() {		
		int size = routes.get(0).getLegs().get(0).getSteps().size();
		LatLng endLatLng = routes.get(0)
				                 .getLegs()
				                 .get(0)
				                 .getSteps()
				                 .get(size - 1)
				                 .getEndLocation();
		
		return endLatLng.getLng().doubleValue();
	}
	
    public double getRouteLat(int index) {		
		return routes.get(0)
				     .getLegs()
				     .get(index)
				     .getStartLocation()
				     .getLat()
				     .doubleValue();
	}
	
	public double getRouteLng(int index) {		
		return routes.get(0)
				     .getLegs()
				     .get(index)
				     .getStartLocation()
				     .getLng()
				     .doubleValue();
	}
	
	public int getRouteSize() {		
		return routes.size();
	}
	// ============ End point ====================
	
	// Get steps from MOA solutions
	// ============ Start point ==================
	public double getMoaFirstPathLat(int pathIndex) {
		CapraPathLeg path = getMoaResults().get(pathIndex);
		return path.getSteps()
				   .get(0)
				   .getStartNode()
				   .getLocation()
				   .getLat()
				   .doubleValue();
	}
	
	public double getMoaFirstPathLng(int pathIndex) {
		CapraPathLeg path = getMoaResults().get(pathIndex);
		return path.getSteps()
				   .get(0)
				   .getStartNode()
				   .getLocation()
				   .getLng()
				   .doubleValue();
	}
	
	public double getMoaPathLat(int stepIndex, int pathIndex) {
		CapraPathLeg path = getMoaResults().get(pathIndex);
		return path.getSteps()
				   .get(stepIndex)
				   .getEndNode()
				   .getLocation()
				   .getLat()
				   .doubleValue();
	}
	
	public double getMoaPathLng(int stepIndex, int pathIndex) {
		CapraPathLeg path = getMoaResults().get(pathIndex);
		return path.getSteps()
				   .get(stepIndex)
				   .getEndNode()
				   .getLocation()
				   .getLng()
				   .doubleValue();
	}
	
	public int getMoaPathSize(int pathIndex) {
		CapraPathLeg path = getMoaResults().get(pathIndex);
		return path.getSteps().size();
	}
	
	public int getMoaSize() {
		return paths_MOAStar.size();   // get total number of MOA solutions
	}
	
	// ============ End point ====================
	
	public String getDistanceText(int pathIndex) {
		double  distanceValue;
		
		if (pathIndex == -1) {
			return routes.get(0).getLegs().get(0).getDistance().toString();
		}
		else  {
			distanceValue = paths_MOAStar.get(pathIndex).getDistance().getValue();
		}
		
		String pattern = "###.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		
		return decimalFormat.format(distanceValue / 1000) + "km";
	}
	
	public double getDistanceValue(int pathIndex) {
		double  distanceValue;
		
		if (pathIndex == -1) {
			distanceValue = routes.get(0).getLegs().get(0).getDistance().getValue();
		}
		else  {
			distanceValue = paths_MOAStar.get(pathIndex).getDistance().getValue();
		}
		
		return distanceValue;
	}
	
	public String getEstimatedValue(int pathIndex) {
		
		return estimateValues.get(pathIndex);
	}
	

	// Core function: calculating all possible solutions
	public void calcPath(int selectedIndex) throws FileNotFoundException, 
	      XMLStreamException, LatLngException, CapraPathNotFoundException {
		if (origin_LatLng == null || destination_LatLng == null) {
			throw new LatLngException(
					"Origin or destination cannot be empty!");
		}
		
		/*
		 * ******************************
		 * Print out experimental data
		 * ******************************
		 */
		
		buffer.setLength(0);   // refresh the buffer;
		
		long time1 = System.currentTimeMillis();
		
		buffer.append("*****Start******");
		buffer.append(System.getProperty("line.separator"));
		
        graphXmlLoader = new GraphXMLLoader();
        
        String filename = null;
        switch (selectedIndex) {
        case 1: filename = FILE_FILBERT_SA_CONTOUR_U5; break;
        case 2: filename = FILE_FILBERT_SA_CONTOUR_U10; break;
        case 3: filename = FILE_BUKIT_TIMAH_SINGAPORE_U5; break;
        case 4: filename = FILE_LISBON_U5; break;
        case 5: filename = FILE_HEIDELBERG_CONTOUR; break;
        }
        
		// Load graph from local OSM file
		graph_WithContour = graphXmlLoader.creatGraph(
				origin_LatLng, destination_LatLng, filename);
		
		long time2 = System.currentTimeMillis();
		long duration1 = time2 - time1;
		
		buffer.append("reading finished, spent " + duration1 + "ms.");
		buffer.append(System.getProperty("line.separator"));
		
		// get Google directions
		routes.clear();
		routes = getDirectionsRoutes(origin_LatLng, destination_LatLng);
		
		LatLng startLatLng = routes.get(0)
				                   .getLegs()
				                   .get(0)
				                   .getSteps()
				                   .get(0)
				                   .getStartLocation();
		
		int stepSize = routes.get(0).getLegs().get(0).getSteps().size();
		LatLng endLatLng = routes.get(0)
					               .getLegs()
					               .get(0)
					               .getSteps()
					               .get(stepSize-1)
					               .getEndLocation();
		
		long time3 = System.currentTimeMillis();
		long duration2 = time3 - time2;
		
		buffer.append("Google route finished, spent " + duration2 + "ms.");
		buffer.append(System.getProperty("line.separator"));
		
		// add startPoint and EndPoint into the graph
		addNewLocationToGraph(startLatLng, graph_WithContour);
		addNewLocationToGraph(endLatLng, graph_WithContour);
		
		System.out.println("node size: " + graph_WithContour.getNodeList().size());
		System.out.println("edge size: " + graph_WithContour.getEdgeList().size());
		
		long time4 = System.currentTimeMillis();
		long duration3 = time4 - time3;
		
		buffer.append("Rebuild graph finished, spent " + duration3 + "ms.");
		buffer.append(System.getProperty("line.separator"));
				                
		// get routes by using MOA*
		paths_MOAStar.clear();
		double directDistance = DistanceCalculator.getDistance(startLatLng, endLatLng);
		
		buffer.append(System.getProperty("line.separator"));
		buffer.append("***MOA* (Distance)***");
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Direct distance: " + directDistance);
		buffer.append(System.getProperty("line.separator"));
		
		paths_MOAStar = getPaths(startLatLng, endLatLng, MOASTAR, buffer, estimateValues);
		
		for (int i = 0; i < paths_MOAStar.size(); i++) {
			
			CapraPathLeg currPath = paths_MOAStar.get(i);
			currPath.calcUpDistance();
			currPath.calcMaxTangent();
			
			// print data
			buffer.append("----------------");
			buffer.append(System.getProperty("line.separator"));
			buffer.append("path " + i + ":");
			buffer.append(System.getProperty("line.separator"));
			buffer.append("Elevation at node " + 
					   currPath.getSteps().get(0).getStartNode().getId() + 
			           ": " +
			           currPath.getSteps().get(0).getStartNode().getElevation() +
			           ", tangent = " + currPath.getSteps().get(0).getTangent());
			buffer.append(System.getProperty("line.separator"));
			
			for (CapraPathStep ps : currPath.getSteps()) {
				buffer.append("Elevation at node " + 
						   ps.getEndNode().getId() + 
		                   ": " +
		                   ps.getEndNode().getElevation() + 
		                   ", tangent = " + ps.getTangent());
				buffer.append(System.getProperty("line.separator"));
				
			}
			
			for (CapraPathStep ps : currPath.getSteps()) {
				buffer.append("Length from node " + 
	                       ps.getStartNode().getId() + 
	                       " to node " +
	                       ps.getEndNode().getId() + 
	                       ": " + 
	                       ps.getDistance());
				buffer.append(System.getProperty("line.separator"));
			}
			
//			buffer.append("dist = " + currPath.getDistance() + ", up = " + currPath.getUpDistance()
//					+ ", maxTangent = " + currPath.getMaxTangent());
//			buffer.append(System.getProperty("line.separator"));
		}
		
		// Add rest solutions (by MOA*) to the list
//		pathSolutions.addAll(paths_MOAStar);
		
		long time5 = System.currentTimeMillis();
		long duration4 = time5 - time4;
		
		buffer.append("MOA* finished, spent " + duration4 + "ms.");
		buffer.append(System.getProperty("line.separator"));		
		
		buffer.append(System.getProperty("line.separator"));
		buffer.append("***Google***");
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Direct distance: " + directDistance);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("total distance: " + routes.get(0).getLegs().get(0).getDistance().toString());
		buffer.append(System.getProperty("line.separator"));
		
		// print data
		int count_Node = 1;
		int a = 1;
		int b = 2;
		
		buffer.append("Google");
		buffer.append(System.getProperty("line.separator"));
		buffer.append("-------------------");
		buffer.append(System.getProperty("line.separator"));
		
		ElevationApi eApi = new ElevationApi();
		List<LatLng> list = new ArrayList<LatLng>();
		list.add(routes.get(0).getLegs().get(0).getSteps().get(0).getStartLocation());
		
		for (GDirectionsStep ds : routes.get(0).getLegs().get(0).getSteps()) {
			list.add(ds.getEndLocation());
		}
		
		List<GElevationResult> eResult = eApi.getElevationByLocations(list);
		
		for (GElevationResult ds : eResult) {
			buffer.append("Elevation at node " + 
                    count_Node + 
                    ": " + 
                    ds.getElevation());
			buffer.append(System.getProperty("line.separator"));
			
			count_Node++;
		}
		
		for (GDirectionsStep ds : routes.get(0).getLegs().get(0).getSteps()) {
			buffer.append("Length from node " + 
                    a + 
                    " to node " + 
                    b + 
                    ": " + 
                    ds.getDistance().getValue());
			buffer.append(System.getProperty("line.separator"));
			
			a++;
			b++;
		}
		
		buffer.append(System.getProperty("line.separator"));
		buffer.append("***** End ********");
		buffer.append(System.getProperty("line.separator"));
		
		/*
		 * ************************
		 * Print out end
		 * ************************
		 */
		
		if (paths_MOAStar == null) {
			throw new CapraPathNotFoundException("Cannot find path!");
		}
	}
	
	public String getSystemInfomation() {
		return buffer.toString();
	}
	
	public Edge getEdge(int index) {
		
		return graph_WithContour.getEdgeList().get(index);
	}
	
	public int getEdgeSize() {
		
		return graph_WithContour.getEdgeList().size();
	}

	private List<GDirectionsRoute> getDirectionsRoutes(
			LatLng origin_LatLng, LatLng destination_LatLng) {
		DirectionsApi api = new DirectionsApi();
		
		return api.getRoutes(origin_LatLng, destination_LatLng);
	}
	
	// Get MOAStar solutions
	private List<CapraPathLeg> getPaths(LatLng origin_LatLng, 
			LatLng destination_LatLng, int algorithmIndex, StringBuffer buffer, List<String> estimates) {	
		Node source = getNodeFromGraph(origin_LatLng);
		Node target = getNodeFromGraph(destination_LatLng);
		
		switch (algorithmIndex) {
		case MOASTAR : return moastar.search(graph_WithContour, source, target, buffer, estimates);
		default : return null;
		}
	}
	
	private Node getNodeFromGraph(LatLng location) {
		return graph_WithContour.findNode(location);
	}
	
	// Google
	public List<Double> getRouteElevationList() {
		List<Double> elevations = new ArrayList<Double>();
		ElevationApi api = new ElevationApi();
		
		List<LatLng> points = new ArrayList<LatLng>();
		points.add(routes.get(0)
				         .getLegs()
				         .get(0)
				         .getSteps()
				         .get(0)
				         .getStartLocation());
		
		for (GDirectionsStep ds : routes.get(0)
				                       .getLegs()
				                       .get(0)
				                       .getSteps()) {
			
			points.add(ds.getEndLocation());
		}
		
		List<GElevationResult> results = api.getElevationByLocations(points);
		
		for (GElevationResult ele : results) {
			elevations.add(ele.getElevation().doubleValue());
		}
		
		return elevations;
	}
	
	public List<Double> getRouteDistanceList() {
        List<Double> distances = new ArrayList<Double>();
		
		double curDistance = 0;
		distances.add(curDistance);
		
		for (GDirectionsStep ds : routes.get(0)
				                       .getLegs()
				                       .get(0)
				                       .getSteps()) {
			curDistance += ds.getDistance().getValue();
			distances.add(curDistance);
		}
		
		return distances;
	}
	
	public long getRouteDistance() {
		
		return routes.get(0).getLegs().get(0).getDistance().getValue();
	}
	
	// return MOA result
	private List<CapraPathLeg> getMoaResults() {
		return paths_MOAStar;
	}
	
	private void addNewLocationToGraph(LatLng location, Graph<Node, Edge> graph) {
		Edge closestEdge = graph.findClosestEdge(location);
		
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(closestEdge.getSourceNode());
		String uuid = "" + UUID.randomUUID();    // generate an unique id in case of duplication
		nodes.add(new Node(uuid, location));
		nodes.add(closestEdge.getTargetNode());
		
		graph.removeEdge(closestEdge);
		
		List<Edge> newEdges = graphXmlLoader.createEdgeFragments(nodes);
		
		graph.addNode(nodes, newEdges);
	}
	
	
}
