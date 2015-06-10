package routePlannerModel;

import dataModel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.google.code.geocoder.model.LatLng;

/*
 * Extract useful information from Osm XML file
 */

public class GraphXMLLoader {
	private XMLInputFactory factory = XMLInputFactory.newInstance();
	private final static int BOUNDARY_RADIUS = 750;
	
	public Graph<Node, Edge> creatGraph(LatLng startLocation, 
			LatLng endLocation, String filename) 
			throws FileNotFoundException, XMLStreamException {
		List<OsmWay> ways = extractWays(filename);
		
		// Boundary
		float midLat = 
				(startLocation.getLat().floatValue() + 
						endLocation.getLat().floatValue()) / 2;
		float midLng = 
				(startLocation.getLng().floatValue() + 
						endLocation.getLng().floatValue()) / 2;
		LatLng midPoint = 
				new LatLng(String.valueOf(midLat), String.valueOf(midLng));
		float bDistance = getBoundaryDistance(startLocation, endLocation);
		
		long time1 = System.currentTimeMillis();
		
		Map<String, Node> nodeMap = 
				extractNodesByCircularBoundary(
						filename, ways, midPoint, bDistance);
		
		List<Node> nodes = new ArrayList<Node>(nodeMap.values());
		List<Edge> edges = extractEdgesList(nodeMap, ways);
		
		// Print Test
		System.out.println("size of nodes = " + nodes.size());
		System.out.println("size of edges = " + edges.size());
		
		long time2 = System.currentTimeMillis();
		long duration1 = time2 - time1;
		System.out.println("extracting nodes and edges takes " + duration1 + "ms.");
		
		calculateElevations(nodes);
		
		long time3 = System.currentTimeMillis();
		long duration2 = time3 - time2;
		System.out.println("getting elevations takes " + duration2 + "ms.");
		
		calculateWeight(edges);	
		
		return new Graph<Node, Edge>(nodes, edges);
	}
	
	public Graph<Node, Edge> creatGraph(String minLng, String minLat, 
			String maxLng, String maxLat, String filename) 
			throws FileNotFoundException, XMLStreamException {
		loadXml(minLng, minLat, maxLng, maxLat, filename);
		List<OsmWay> ways = extractWays(filename);
		Bounds bounds = extractBounds(filename);
		
		Map<String, Node> nodeMap = extractNodes(filename, ways, bounds);
		
		List<Node> nodes = new ArrayList<Node>(nodeMap.values());
		List<Edge> edges = extractEdgesList(nodeMap, ways);
		
		// Print Test
		System.out.println("size of nodes = " + nodes.size());
		System.out.println("size of edges = " + edges.size());
		
		calculateElevations(nodes);
		calculateWeight(edges);	
		
		return new Graph<Node, Edge>(nodes, edges);
	}
	
//	public Graph<Node, Edge> creatGraph(LatLng startLocation, 
//			LatLng endLocation, String filename) 
//			throws FileNotFoundException, XMLStreamException {
//		Boundary boundary = defineBoundary(startLocation, endLocation);
//		
//		loadXml(boundary.getSouthwest().getLng().toString(),
//				boundary.getSouthwest().getLat().toString(),
//				boundary.getNortheast().getLng().toString(), 
//				boundary.getNortheast().getLat().toString(),  
//				filename);
//		List<Way> ways = extractWays(filename);
//		Bounds bounds = 
//				new Bounds(boundary.getSouthwest().getLat().toString(),
//				           boundary.getSouthwest().getLng().toString(), 
//				           boundary.getNortheast().getLat().toString(), 
//				           boundary.getNortheast().getLng().toString());
//		
//		// finding mid point could be moved out
//		float midLat = 
//				(startLocation.getLat().floatValue() + 
//						endLocation.getLat().floatValue()) / 2;
//		float midLng = 
//				(startLocation.getLng().floatValue() + 
//						endLocation.getLng().floatValue()) / 2;
//		LatLng midPoint = 
//				new LatLng(String.valueOf(midLat), String.valueOf(midLng));
//		float bDistance = getBoundaryDistance(startLocation, endLocation);
//		Map<String, Node> nodeMap = 
//				extractNodesByCircularBoundary(
//						filename, ways, midPoint, bDistance);
//		
//		List<Node> nodes = new ArrayList<Node>(nodeMap.values());
//		List<Edge> edges = extractEdgesList(nodeMap, ways);
//		
//		// Print Test		
//		System.out.println("size of nodes = " + nodes.size());
//		System.out.println("size of edges = " + edges.size());
//		
//		calculateElevations(nodes);
//		calculateWeight(edges);	
//		
//		return new Graph<Node, Edge>(nodes, edges);
//	}
	
	public float getBoundaryDistance(LatLng start, LatLng end) {
		float midLat = 
				(start.getLat().floatValue() + end.getLat().floatValue()) / 2;
		float midLng = 
				(start.getLng().floatValue() + end.getLng().floatValue()) / 2;
		LatLng midPoint = 
				new LatLng(String.valueOf(midLat), String.valueOf(midLng));
		
		float distance = BOUNDARY_RADIUS;
		
		return distance;
	}
	
	public void loadXml(String minLng, String minLat, 
			String maxLng, String maxLat, String filename) {
		String baseUrl = "http://api.openstreetmap.org/api/0.6/map?bbox=";
		StringBuffer buffer = new StringBuffer().append(baseUrl);
		buffer.append(minLng);
		buffer.append(",");
		buffer.append(minLat);
		buffer.append(",");
		buffer.append(maxLng);
		buffer.append(",");
		buffer.append(maxLat);
		
		try {
			String destinationFile = filename;
			URL url = new URL(buffer.toString());
			InputStream in = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);
			
			byte[] b = new byte[2048];
			int lengh;
			
			while ((lengh = in.read(b)) != -1) {
				os.write(b, 0, lengh);
			}
			
			in.close();
			os.close();
		}
        catch (IOException ex) {
			
        }	
	}
	
	public Bounds extractBounds(String filename) 
			throws XMLStreamException, FileNotFoundException {
		XMLStreamReader reader = factory.createXMLStreamReader(
				new FileInputStream(filename));
		String minLat = "";
		String minLng = "";
		String maxLat = "";
		String maxLng = "";
		
		while (reader.hasNext()) {
			int event = reader.next();
			
			if (event == XMLStreamConstants.START_ELEMENT) {
				if ("bounds".equals(reader.getLocalName())) {
					minLat = reader.getAttributeValue(0);
					minLng = reader.getAttributeValue(1);
					maxLat = reader.getAttributeValue(2);
					maxLng = reader.getAttributeValue(3);
					
					break;
				}
			}
		}
		
		if (!minLat.equals("") && !minLng.equals("") && 
				!maxLat.equals("") && !maxLng.equals("")) {
			return new Bounds(minLat, minLng, maxLat, maxLng);
		}
		
		return null;
	}
	
	public static List<Edge> extractEdgesList(
			Map<String, Node> nodes, List<OsmWay> ways) {
		List<Edge> edges = new ArrayList<Edge>();
		
		for (OsmWay w : ways) {
			if (w instanceof OsmContour) {
				continue;
			}
			List<String> nodesOnWay = w.getNodeList();
			
			for (int i = 0; i < nodesOnWay.size() - 1; i++) {
				String nodeId_Front = nodesOnWay.get(i);
				Node node_Front = nodes.get(nodeId_Front);
				if (node_Front == null) {
					continue;
				}
				
				for (int j = i + 1; j < nodesOnWay.size(); j++) {
					String nodeId_Back = nodesOnWay.get(j);
					Node node_Back = nodes.get(nodeId_Back);
					if (node_Back == null) {
						continue;
					}
					
                    Edge edge = new Edge(node_Front, node_Back);
					edges.add(edge);
					break;
				}
			}
		}
		
		return edges;
	}
	
	public Map<String, Node> extractNodesByCircularBoundary(
			String filename, List<OsmWay> ways, LatLng midPoint, float bDistance) 
			throws XMLStreamException, FileNotFoundException {
		XMLStreamReader reader = factory.createXMLStreamReader(
				new FileInputStream(filename));
		Map<String, Node> rowList = new TreeMap<String, Node>();
		Map<String, Node> nodeList = new TreeMap<String, Node>();
		
		while (reader.hasNext()) {
			int event = reader.next();
			
			if (event == XMLStreamConstants.START_ELEMENT) {
				if ("node".equals(reader.getLocalName())) {
					String id = reader.getAttributeValue(0);
					String lat, lng;
					if ("modify".equals(reader.getAttributeValue(1))) {
						lat = reader.getAttributeValue(3);
						lng = reader.getAttributeValue(4);
					}
					else {
						lat = reader.getAttributeValue(7);
						lng = reader.getAttributeValue(8);
					}
					
					float lat_Float = Float.parseFloat(lat);
					float lng_Float = Float.parseFloat(lng);
					
					LatLng latLng = new LatLng(lat, lng);
					if (bDistance >= DistanceCalculator.getDistance(latLng, midPoint)) {
						rowList.put(id, new Node(id, latLng));
					}
				}
			}
		}
		
		for (int i = 0; i < ways.size(); i++) {
			Set<String> allNodes = new TreeSet<String>();
			for (int j = 0; j < ways.size(); j++) {
				if (j == i) {
					continue;
				}
				allNodes.addAll(ways.get(j).getNodeList());
			}
			
			Set<String> curNodes = new TreeSet<String>(ways.get(i).getNodeList());
			
			curNodes.retainAll(allNodes);
			
			Map<String, Node> subNodeList = new TreeMap<String, Node>();
			
			for (String nd : curNodes) {
				Node node;
				if ((node = rowList.get(nd)) != null) {
					nodeList.put(nd, node);
					subNodeList.put(nd, node);
				}
			}
		}
		return nodeList;
	}
	
	public Map<String, Node> extractNodes(
			String filename, List<OsmWay> ways, Bounds bounds) 
			throws XMLStreamException, FileNotFoundException {
		XMLStreamReader reader = factory.createXMLStreamReader(
				new FileInputStream(filename));
		Map<String, Node> rowList = new TreeMap<String, Node>();
		Map<String, Node> nodeList = new TreeMap<String, Node>();
		float minLat = bounds.getFloatMinLat();
		float minLng = bounds.getFloatMinLng();
		float maxLat = bounds.getFloatMaxLat();
		float maxLng = bounds.getFloatMaxLng();
		
		while (reader.hasNext()) {
			int event = reader.next();
			
			if (event == XMLStreamConstants.START_ELEMENT) {
				if ("node".equals(reader.getLocalName())) {
					String id = reader.getAttributeValue(0);
					String lat, lng;
					if ("modify".equals(reader.getAttributeValue(1))) {
						lat = reader.getAttributeValue(3);
						lng = reader.getAttributeValue(4);
					}
					else {
						lat = reader.getAttributeValue(7);
						lng = reader.getAttributeValue(8);
					}
					
					float lat_Float = Float.parseFloat(lat);
					float lng_Float = Float.parseFloat(lng);
					
					if (lat_Float >= minLat && lat_Float <= maxLat &&
						lng_Float >= minLng && lng_Float <= maxLng) {
						
						LatLng latLng = new LatLng(lat, lng);
						rowList.put(id, new Node(id, latLng));
					}
				}
			}
		}
		
		for (int i = 0; i < ways.size(); i++) {
			Set<String> allNodes = new TreeSet<String>();
			for (int j = 0; j < ways.size(); j++) {
				if (j == i) {
					continue;
				}
				allNodes.addAll(ways.get(j).getNodeList());
			}
			
			Set<String> curNodes = new TreeSet<String>(ways.get(i).getNodeList());
			
			curNodes.retainAll(allNodes);
			
			Map<String, Node> subNodeList = new TreeMap<String, Node>();
			
			for (String nd : curNodes) {
				Node node;
				if ((node = rowList.get(nd)) != null) {
					nodeList.put(nd, node);
					subNodeList.put(nd, node);
				}
			}
		}
		
		return nodeList;
	}
	
	public List<OsmWay> extractWays(String filename) 
			throws XMLStreamException, FileNotFoundException {
		XMLStreamReader reader = factory.createXMLStreamReader(
				new FileInputStream(filename));
		List<OsmWay> wayList = new ArrayList<OsmWay>();
		
		String[] road_types = {"primary", "secondary", 
				"residential", "tertiary", "service", "unclassified", "pedestrian"};
		
		List<String> types = new ArrayList<String>();
		
		for (int i = 0; i < road_types.length; i++) {
			types.add(road_types[i]);
		}
		
		while (reader.hasNext()) {
			int event = reader.next();
			
			if (event == XMLStreamConstants.START_ELEMENT) {
				if ("way".equals(reader.getLocalName())) {
					String wayId = "";
					String highway = "";
					String name = "";
					String surface = "";
					String contour = "";
					String ele = "";
					List<String> nodeList = new ArrayList<String>();
					
					wayId = reader.getAttributeValue(0);
					
					boolean endWay = false;
					while (reader.hasNext() && !endWay) {
						
						int subEvent = reader.next();
						
						if (subEvent == XMLStreamConstants.START_ELEMENT) {
							if ("nd".equals(reader.getLocalName())) {
								nodeList.add(reader.getAttributeValue(0));
							}
							else if ("tag".equals(reader.getLocalName())) {
								if ("highway".equals(reader.getAttributeValue(0))) {
									highway = reader.getAttributeValue(1);
								}
								else if ("name".equals(reader.getAttributeValue(0))) {
									name = reader.getAttributeValue(1);
								}
								else if ("surface".equals(reader.getAttributeValue(0))) {
									surface = reader.getAttributeValue(1);
								}
								else if ("contour".equals(reader.getAttributeValue(0))) {
									contour = reader.getAttributeValue(1);
								}
								else if ("ele".equals(reader.getAttributeValue(0))) {
									ele = reader.getAttributeValue(1);
								}
							}
							
						}
						else if (subEvent == XMLStreamConstants.END_ELEMENT) {
							
							if ("way".equals(reader.getLocalName())) {
								if (!highway.equals("") &&
								    types.contains(highway)) {
									wayList.add(new OsmRoad(wayId, 
											highway, name, surface, nodeList));
								}
								else if (name.equals("Swanston Street")) {
									wayList.add(new OsmRoad(wayId, 
											highway, name, surface, nodeList));
								}
								else if(contour.equals("elevation")) {
									wayList.add(new OsmContour(wayId, 
											contour, ele, nodeList));
								}
								
								endWay = true;	
							}
						}
					}
				}
			}
		}
		
		return wayList;
	}
	
	public Boundary defineBoundary(LatLng start, LatLng end) {
		float sLat = start.getLat().floatValue();
		float sLng = start.getLng().floatValue();
		float eLat = end.getLat().floatValue();
		float eLng = end.getLng().floatValue();
		
		float NELat = 0;
		float NELng = 0;
		float SWLat = 0;
		float SWLng = 0;
		
		if (sLat < eLat) {
			SWLat = (float) (sLat - 0.005);
			NELat = (float) (eLat + 0.005);
		}
		else {
			SWLat = (float) (eLat - 0.005);
			NELat = (float) (sLat + 0.005);
		}
		
		if (sLng < eLng) {
			SWLng = (float) (sLng - 0.005);
			NELng = (float) (eLng + 0.005);
		}
		else {
			SWLng = (float) (eLng - 0.005);
			NELng = (float) (sLng + 0.005);
		}
		
		LatLng southwest = 
				new LatLng(String.valueOf(SWLat), String.valueOf(SWLng));
		LatLng northeast = 
				new LatLng(String.valueOf(NELat), String.valueOf(NELng));
		
		System.out.println(String.valueOf(SWLat) + "," + String.valueOf(SWLng));
		System.out.println(String.valueOf(NELat) + "," + String.valueOf(NELng));
		
		return new Boundary(northeast, southwest);
	}

	/*
	 * Divide nodes into 11 groups in order to avoid exceeding the limit,
	 * because one elevation HTTP request can only contain 2000 characters.
	 * However, this method can be replaced by using PolyLineEncoding.
	 * For more information, please refer to Google document. 
	 */
	private void calculateElevations(List<Node> nodes) {
        ElevationApi elevationApi = new ElevationApi();
		List<LatLng> locations1 = new ArrayList<LatLng>();
		List<LatLng> locations2 = new ArrayList<LatLng>();
		List<LatLng> locations3 = new ArrayList<LatLng>();
		List<LatLng> locations4 = new ArrayList<LatLng>();
		List<LatLng> locations5 = new ArrayList<LatLng>();
		List<LatLng> locations6 = new ArrayList<LatLng>();
		List<LatLng> locations7 = new ArrayList<LatLng>();
		List<LatLng> locations8 = new ArrayList<LatLng>();
		List<LatLng> locations9 = new ArrayList<LatLng>();
		List<LatLng> locations10 = new ArrayList<LatLng>();
		List<LatLng> locations11 = new ArrayList<LatLng>();
		
		for (int i = 0; i < nodes.size() / 11; i++) {
			locations1.add(nodes.get(i).getLocation());
		}
		List<GElevationResult> result = elevationApi.getElevationByLocations(locations1);
		
		for (int j = nodes.size() / 11; j < nodes.size() * 2 / 11; j++) {
			locations2.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations2));
		
		for (int j = nodes.size() * 2 / 11; j < nodes.size() * 3 / 11; j++) {
			locations3.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations3));
		
		for (int j = nodes.size() * 3 / 11; j < nodes.size() * 4 / 11; j++) {
			locations4.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations4));
		
		for (int j = nodes.size() * 4 / 11; j < nodes.size() * 5 / 11; j++) {
			locations5.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations5));
		
		for (int j = nodes.size() * 5 / 11; j < nodes.size() * 6 / 11; j++) {
			locations6.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations6));
		
		for (int j = nodes.size() * 6 / 11; j < nodes.size() * 7 / 11; j++) {
			locations7.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations7));
		
		for (int j = nodes.size() * 7 / 11; j < nodes.size() * 8 / 11; j++) {
			locations8.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations8));
		
		for (int j = nodes.size() * 8 / 11; j < nodes.size() * 9 / 11; j++) {
			locations9.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations9));
		
		for (int j = nodes.size() * 9 / 11; j < nodes.size() * 10 / 11; j++) {
			locations10.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations10));
		
		for (int j = nodes.size() * 10 / 11; j < nodes.size(); j++) {
			locations11.add(nodes.get(j).getLocation());
		}
		result.addAll(elevationApi.getElevationByLocations(locations11));
		
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).setElevation(result.get(i).getElevation().floatValue());
		}
	}
	
	private void calculateWeight(List<Edge> edges) {
		calculateElevationDifferences(edges);
		calculateDistances(edges);
		
		for (Edge e : edges) {
			int weight = GradientWeighter.getRanking(
					e.getElevationDiffer(), e.getDistance());
			
			e.setWeight(weight);
		}
	}
	
	private void calculateElevationDifferences(List<Edge> edges) {
		for (Edge e : edges) {
			float sourceNodeElevation = e.getSourceNode().getElevation();
			float targetNodeElevation = e.getTargetNode().getElevation();
			
			float elevationDiffer = 
					Math.abs(sourceNodeElevation - targetNodeElevation);
			
			e.setElevationDiffer(elevationDiffer);
		}
	}
	
	private void calculateDistances(List<Edge> edges) {
		for (int i = 0; i < edges.size(); i++) {
			float distance = DistanceCalculator.getDistance(
					edges.get(i).getSourceNode(), 
					edges.get(i).getTargetNode());
			edges.get(i).setDistance(distance);
		}
	}
}
