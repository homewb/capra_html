package routePlannerModel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import Yi.MOAStar;

import com.google.code.geocoder.model.LatLng;

import dataModel.*;

public class RoutePlanner {
	private final static int CONTOUR = 0;
	private final static int NO_CONTOUR = 2;
	private final static int DISTANCE = 1;
	
	private final static int MOASTAR = 4;  // New MOA algorithm;
	
	private final static String FILE_HEIDELBERG_CONTOUR = 
			"osm_contour_Heidelberg_Large.xml";
	private final static String FILE_HEIDELBERG = 
			"osm_Heidelberg_Large.xml";
	
	// store path calculated by different methods
	private List<GDirectionsRoute> routes = 
			new ArrayList<GDirectionsRoute>();
	private CapraPathLeg path_WithContour;
	private CapraPathLeg path_Distance;
	private CapraPathLeg path_WithoutContour;
	
	// A list for storing all path solutions
	private List<CapraPathLeg> pathSolutions = new ArrayList<CapraPathLeg>();
	
	private List<CapraPathLeg> paths_MOAStar;  // Solutions by MOA-Star
		
	private LatLng origin_LatLng;
	private LatLng destination_LatLng;
	
	private GeocodingApi geocoder = new GeocodingApi();
	private AStar astar = new AStar();
	private MOAStar moastar = new MOAStar();
	
	// compare with A* algorithm
	private AstarDistance astarDistance = new AstarDistance();
	
	private GraphXMLLoader graphXmlLoader;
	private Graph<Node, Edge> graph_WithContour;
	private Graph<Node, Edge> graph_WithoutContour;
	
	public String getOriginCoordinate(String origin) {		
		origin_LatLng = geocoder.getLocation(origin);
		
		return origin_LatLng.toUrlValue();
	}
	
	public String getTrainStationName(String origin) {
		Ptv ptv = new PtvTimetableApi();
		origin_LatLng = geocoder.getLocation(origin);
		
		float lat = origin_LatLng.getLat().floatValue();
		float lng = origin_LatLng.getLng().floatValue();
		
		List<PtvStop> trainStations = new ArrayList<PtvStop>();
		ArrayList<PtvStop> ptvStops = ptv.findStopsNearby(lat, lng);
		for (PtvStop s : ptvStops) {
			if (s.getTransportType().equals("train")) {
				trainStations.add(s);
			}
		}
		
		if (trainStations.isEmpty()) {
			System.out.println("No Station nearby");
		}
		
		String name = trainStations.get(0).getLocationName();
		float station_lat = trainStations.get(0).getLat();
		float station_lng = trainStations.get(0).getLon();
		
		destination_LatLng = new LatLng(String.valueOf(station_lat), 
				String.valueOf(station_lng));
		
		return name;
	}
	
	public  String getOriginLatLng() {
		
		return origin_LatLng.toUrlValue();
	}
	
	public String getDestinLatLng() {
		
		return destination_LatLng.toUrlValue();
	}

	public String getDestinationCoordinate(String destination) {
		destination_LatLng = geocoder.getLocation(destination);
		
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
	
	// Get step info from non-MOA solution
	// ============ Start point ==================
	public double getFirstPathLat(int method) {
		CapraPathLeg path = getPathResult(method);
		return path.getSteps()
				   .get(0)
				   .getStartNode()
				   .getLocation()
				   .getLat()
				   .doubleValue();
	}
	
	public double getFirstPathLng(int method) {
		CapraPathLeg path = getPathResult(method);
		return path.getSteps()
				   .get(0)
				   .getStartNode()
				   .getLocation()
				   .getLng()
				   .doubleValue();
	}
	
	public double getPathLat(int index, int method) {
		CapraPathLeg path = getPathResult(method);
		return path.getSteps()
				   .get(index)
				   .getEndNode()
				   .getLocation()
				   .getLat()
				   .doubleValue();
	}
	
	public double getPathLng(int index, int method) {
		CapraPathLeg path = getPathResult(method);
		return path.getSteps()
				   .get(index)
				   .getEndNode()
				   .getLocation()
				   .getLng()
				   .doubleValue();
	}
	
	public int getPathSize(int method) {
		CapraPathLeg path = getPathResult(method);
		return path.getSteps().size();
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
	

	// Core function: calculating all possible solutions
	public void calcPath() throws FileNotFoundException, 
	      XMLStreamException, LatLngException, CapraPathNotFoundException {
		if (origin_LatLng == null || destination_LatLng == null) {
			throw new LatLngException(
					"Origin or destination cannot be empty!");
		}
		
		long time1 = System.currentTimeMillis();
		System.out.println("start");
		
        graphXmlLoader = new GraphXMLLoader();
		
		// Load graph from local OSM file
		graph_WithContour = graphXmlLoader.creatGraph(
				origin_LatLng, destination_LatLng, FILE_HEIDELBERG_CONTOUR);
//		graph_WithoutContour = graphXmlLoader.creatGraph(
//				origin_LatLng, destination_LatLng, FILE_HEIDELBERG);
		
		// Load graph from OSM.org
//		String minLng = "144.9664";
//		String minLat = "-37.8172";
//		String maxLng = "144.9756";
//		String maxLat = "-37.8088";
//		graph = graphXmlLoader.creatGraph(
//				minLng, minLat, maxLng, maxLat, testFile);
		
		// Dynamically load graph from OSM.org
//		graph = graphXmlLoader.creatGraph(
//				origin_LatLng, destination_LatLng, testFile);
		
		long time2 = System.currentTimeMillis();
		long duration1 = time2 - time1;
		System.out.println("reading finished, spent " + duration1 + "ms.");
		
		// get Google directions
		routes.clear();
		routes = getDirectionsRoutes(origin_LatLng, destination_LatLng);
		
		LatLng startLatLng = routes.get(0)
				                   .getLegs()
				                   .get(0)
				                   .getSteps()
				                   .get(0)
				                   .getStartLocation();
		
		long time3 = System.currentTimeMillis();
		long duration2 = time3 - time2;
		System.out.println("Google route finished, spent " + duration2 + "ms.");
				                
		/*
		 * ******************************
		 * Print out experimental data
		 * ******************************
		 */
		
		// get routes by using different methods
		pathSolutions.clear();
		double directDistance = DistanceCalculator.getDistance(startLatLng, destination_LatLng);
		System.out.println();
		System.out.println("***CAPRA (Normal)***");
		System.out.println("Direct distance: " + directDistance);
		path_WithContour = getPath(startLatLng, destination_LatLng, CONTOUR);
		
		// Add first solution (by Capra with contour) to the list
		pathSolutions.add(path_WithContour);
		
		// print data
		System.out.println("----------------");
		System.out.println("Elevation at node " + 
				path_WithContour.getSteps().get(0).getStartNode().getId() + 
				           ": " +
				           path_WithContour.getSteps().get(0).getStartNode().getElevation());
		for (CapraPathStep ps : path_WithContour.getSteps()) {
			System.out.println("Elevation at node " + 
					ps.getEndNode().getId() + 
			                   ": " +
			                   ps.getEndNode().getElevation());
		}
		
		for (CapraPathStep ps : path_WithContour.getSteps()) {
			System.out.println("Length from node " + 
		                       ps.getStartNode().getId() + 
		                       " to node " +
		                       ps.getEndNode().getId() + 
		                       ": " + 
		                       ps.getDistance());
		}
		
		long time4 = System.currentTimeMillis();
		long duration3 = time4 - time3;
		System.out.println("CAPRA path with contour finished, spent " + duration3 + "ms.");
		
		/////////////////////////////////////////////////
		
//		System.out.println();
//		System.out.println("***CAPRA (without contour)***");
//		System.out.println("Direct distance: " + directDistance);
//		path_WithoutContour = getPath(startLatLng, destination_LatLng, NO_CONTOUR);
//		
//		// print data
//		System.out.println("----------------");
//		System.out.println("Elevation at node " + 
//				path_WithoutContour.getSteps().get(0).getStartNode().getId() + 
//				           ": " +
//				           path_WithoutContour.getSteps().get(0).getStartNode().getElevation());
//		for (CapraPathStep ps : path_WithoutContour.getSteps()) {
//			System.out.println("Elevation at node " + 
//					ps.getEndNode().getId() + 
//			                   ": " +
//			                   ps.getEndNode().getElevation());
//		}
//		
//		for (CapraPathStep ps : path_WithoutContour.getSteps()) {
//			System.out.println("Length from node " + 
//		                       ps.getStartNode().getId() + 
//		                       " to node " +
//		                       ps.getEndNode().getId() + 
//		                       ": " + 
//		                       ps.getDistance());
//		}
//		
		long time5 = System.currentTimeMillis();
		long duration4 = time5 - time3;
		System.out.println("CAPRA path without contour finished, spent " + duration4 + "ms.");
		
		/////////////////////////////////////////////////
		
		System.out.println();
		System.out.println("***A* (Distance)***");
		System.out.println("Direct distance: " + directDistance);
		path_Distance = getPath(startLatLng, destination_LatLng, DISTANCE);
		
		// Add second solution (by A* Distance) to the list
		pathSolutions.add(path_Distance);
		
		// print data
		System.out.println("----------------");
		System.out.println("Elevation at node " + 
				path_Distance.getSteps().get(0).getStartNode().getId() + 
				           ": " +
				           path_Distance.getSteps().get(0).getStartNode().getElevation());
		for (CapraPathStep ps : path_Distance.getSteps()) {
			System.out.println("Elevation at node " + 
					           ps.getEndNode().getId() + 
			                   ": " +
			                   ps.getEndNode().getElevation());
		}
		
		for (CapraPathStep ps : path_Distance.getSteps()) {
			System.out.println("Length from node " + 
		                       ps.getStartNode().getId() + 
		                       " to node " +
		                       ps.getEndNode().getId() + 
		                       ": " + 
		                       ps.getDistance());
		}
		
		long time6 = System.currentTimeMillis();
		long duration5 = time6 - time5;
		System.out.println("A* finished, spent " + duration5 + "ms.");
		
		/////////////////////////////////////////////////
		
		System.out.println();
		System.out.println("***MOA* (Distance)***");
		System.out.println("Direct distance: " + directDistance);
		paths_MOAStar = getPaths(startLatLng, destination_LatLng, MOASTAR);
		
		for (int i = 0; i < paths_MOAStar.size(); i++) {
			
			CapraPathLeg currPath = paths_MOAStar.get(i);
			
			// print data
			System.out.println("----------------");
			System.out.println("path " + i + ":");
			System.out.println("Elevation at node " + 
					currPath.getSteps().get(0).getStartNode().getId() + 
					           ": " +
					           currPath.getSteps().get(0).getStartNode().getElevation());
			for (CapraPathStep ps : currPath.getSteps()) {
				System.out.println("Elevation at node " + 
								   ps.getEndNode().getId() + 
				                   ": " +
				                   ps.getEndNode().getElevation());
			}
			
			for (CapraPathStep ps : currPath.getSteps()) {
				System.out.println("Length from node " + 
			                       ps.getStartNode().getId() + 
			                       " to node " +
			                       ps.getEndNode().getId() + 
			                       ": " + 
			                       ps.getDistance());
			}
		}
		
		// Add rest solutions (by MOA*) to the list
		pathSolutions.addAll(paths_MOAStar);
		
		long time7 = System.currentTimeMillis();
		long duration6 = time7 - time6;
		System.out.println("MOA* finished, spent " + duration6 + "ms.");
		
		/////////////////////////////////////////////////
		
		System.out.println();
		System.out.println("***Google***");
		System.out.println("Direct distance: " + directDistance);
		System.out.println("total distance: " + routes.get(0).getLegs().get(0).getDistance().toString());
		
		// print data
		int count_Node = 1;
		int a = 1;
		int b = 2;
		System.out.println("Google");
		System.out.println("-------------------");
		
		ElevationApi eApi = new ElevationApi();
		List<LatLng> list = new ArrayList<LatLng>();
		list.add(routes.get(0).getLegs().get(0).getSteps().get(0).getStartLocation());
		
		for (GDirectionsStep ds : routes.get(0).getLegs().get(0).getSteps()) {
			list.add(ds.getEndLocation());
		}
		
		List<GElevationResult> eResult = eApi.getElevationByLocations(list);
		
		for (GElevationResult ds : eResult) {
			System.out.println("Elevation at node " + 
		                       count_Node + 
		                       ": " + 
		                       ds.getElevation());
			count_Node++;
		}
		
		for (GDirectionsStep ds : routes.get(0).getLegs().get(0).getSteps()) {
			System.out.println("Length from node " + 
		                       a + 
		                       " to node " + 
		                       b + 
		                       ": " + 
		                       ds.getDistance().getValue());
			a++;
			b++;
		}
		
		System.out.println();
		
		/*
		 * ************************
		 * Print out end
		 * ************************
		 */
		
		if (path_WithContour == null) {
			throw new CapraPathNotFoundException("Cannot find path!");
		}
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

	private CapraPathLeg getPath(LatLng origin_LatLng, 
			LatLng destination_LatLng, int algorithmIndex) {
		Node source = getNearestNodeFromOrigin(origin_LatLng, algorithmIndex);
		Node target = getNearestNodeFromDestination(destination_LatLng, algorithmIndex);
		
		switch (algorithmIndex) {
		case CONTOUR : return astar.search(graph_WithContour, source, target);
		case NO_CONTOUR : return astar.search(graph_WithoutContour, source, target);
		case DISTANCE : return astarDistance.search(graph_WithContour, source, target);
		default : return null;
		}
	}
	
	// Get MOAStar solutions
	private List<CapraPathLeg> getPaths(LatLng origin_LatLng, 
			LatLng destination_LatLng, int algorithmIndex) {
		Node source = getNearestNodeFromOrigin(origin_LatLng, algorithmIndex);
		Node target = getNearestNodeFromDestination(destination_LatLng, algorithmIndex);
		
		switch (algorithmIndex) {
		case MOASTAR : return moastar.search(graph_WithContour, source, target);
		default : return null;
		}
	}

	private Node getNearestNodeFromOrigin(LatLng latLng_origin, int method) {
		if (method == CONTOUR)
			return graph_WithContour.findNearestNode(latLng_origin);
		else if (method == NO_CONTOUR)
			return graph_WithoutContour.findNearestNode(latLng_origin);
		else
			return graph_WithContour.findNearestNode(latLng_origin);
		
		
	}

	private Node getNearestNodeFromDestination(LatLng latLng_destination, int method) {
		if (method == CONTOUR)
			return graph_WithContour.findNearestNode(latLng_destination);
		else if (method == NO_CONTOUR)
			return graph_WithoutContour.findNearestNode(latLng_destination);
		else
			return graph_WithContour.findNearestNode(latLng_destination);
	}
	
	public List<Double> getPathElevationList(int method) {
		List<Double> elevations = new ArrayList<Double>();
		CapraPathLeg path = getPathResult(method);
		
		float eleInFloat = path.getSteps().get(0).getStartNode().getElevation();
		double ele = Math.round(eleInFloat);
		elevations.add(ele);
		for (CapraPathStep s : path.getSteps()) {
			eleInFloat = s.getEndNode().getElevation();
			ele = Math.round(eleInFloat);
			elevations.add(ele);
		}
		
		return elevations;
	}
	
	public List<Double> getPathDistanceList(int method) {
		List<Double> distances = new ArrayList<Double>();
		CapraPathLeg path = getPathResult(method);
		
		double curDistance = 0;
		distances.add(curDistance);
		
		for (CapraPathStep s : path.getSteps()) {
			curDistance += s.getDistance().getValue();
			distances.add(curDistance);
		}
		
		return distances;
	}
	
	public long getPathDistance(int method) {
		CapraPathLeg path = getPathResult(method);
		
		return path.getDistance().getValue();
	}
	
	/*
	 *  path_Dist
	 */
	public List<Double> getPathDistElevationList() {
		List<Double> elevations = new ArrayList<Double>();
		
		float eleInFloat = path_Distance.getSteps().get(0).getStartNode().getElevation();
		double ele = Math.round(eleInFloat);
		elevations.add(ele);
		for (CapraPathStep s : path_Distance.getSteps()) {
			eleInFloat = s.getEndNode().getElevation();
			ele = Math.round(eleInFloat);
			elevations.add(ele);
		}
		
		return elevations;
	}
	
	public List<Double> getPathDistDistanceList() {
		List<Double> distances = new ArrayList<Double>();
		
		double curDistance = 0;
		distances.add(curDistance);
		
		for (CapraPathStep s : path_Distance.getSteps()) {
			curDistance += s.getDistance().getValue();
			distances.add(curDistance);
		}
		
		return distances;
	}
	
	public long getPathDistDistance() {
		
		return path_WithContour.getDistance().getValue();
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
	
	// return non-MOA result
	private CapraPathLeg getPathResult(int method) {
		CapraPathLeg path;
		if (method == CONTOUR)
//			path = path_WithContour;
			path = pathSolutions.get(CONTOUR);
		else if (method == DISTANCE)
//			path = path_WithoutContour;
		    path = pathSolutions.get(DISTANCE);
		else
//			path = path_Distance;
			path = path_WithoutContour;
		
		return path;
	}
	
	// return MOA result
	private List<CapraPathLeg> getMoaResults() {
		return pathSolutions.subList(2, pathSolutions.size());
	}
	
	
	
	
}
