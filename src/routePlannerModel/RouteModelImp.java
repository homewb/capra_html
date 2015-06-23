package routePlannerModel;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import static com.googlecode.charts4j.Color.*;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;
import com.googlecode.charts4j.XYLine;
import com.googlecode.charts4j.XYLineChart;

import dataModel.Edge;
import dataModel.LatLngException;
import dataModel.CapraPathNotFoundException;

public class RouteModelImp implements RouteModel {
	private final static int CONTOUR = 0;
	private final static int NO_CONTOUR = 1;
	private final static int DISTANCE = 2;
	RoutePlanner routePlanner = new RoutePlanner();
	
	@Override
	public boolean setOriginAndDestination(String origin, String destination) {
		
		return routePlanner.setOriginAndDestination(origin, destination);
	}
	
	@Override
	public String getOriginCoordinate(String origin) {
		
		return routePlanner.getOriginCoordinate(origin);
	}

	@Override
	public String getDestinationCoordinate(String destination) {
		
		return routePlanner.getDestinationCoordinate(destination);
	}

	@Override
	public double getStartLat() {
		
		return routePlanner.getStartLat();
	}

	@Override
	public double getStartLng() {
		
		return routePlanner.getStartLng();
	}

	@Override
	public double getEndLat() {
		
		return routePlanner.getEndLat();
	}

	@Override
	public double getEndLng() {
		
		return routePlanner.getEndLng();
	}

	@Override
	public double getPathLat(int index, int method) {
		
		return routePlanner.getPathLat(index, method);
	}

	@Override
	public double getPathLng(int index, int method) {
		
		return routePlanner.getPathLng(index, method);
	}

	@Override
	public int getPathSize(int method) {
		
		return routePlanner.getPathSize(method);
	}

	@Override
	public void calcPath(String interval) throws FileNotFoundException, 
	      XMLStreamException, LatLngException, CapraPathNotFoundException {
		
		routePlanner.calcPath(interval);
	}

	@Override
	public double getEdgeStartLat(int index) {
		Edge edge = getEdge(index);
		double lat = edge.getSourceNode()
		                 .getLocation()
		                 .getLat()
		                 .doubleValue();

		return lat;
	}

	@Override
	public double getEdgeStartLng(int index) {
		Edge edge = getEdge(index);
		double lng = edge.getSourceNode()
		                 .getLocation()
		                 .getLng()
		                 .doubleValue();
		
		return lng;
	}

	@Override
	public double getEdgeEndLat(int index) {
		Edge edge = getEdge(index);
		double lat = edge.getTargetNode()
				         .getLocation()
				         .getLat()
				         .doubleValue();
		return lat;
	}

	@Override
	public double getEdgeEndLng(int index) {
		Edge edge = getEdge(index);
		double lng = edge.getTargetNode()
				         .getLocation()
				         .getLng()
				         .doubleValue();
		return lng;
	}

	@Override
	public int getEdgeWeight(int index) {
		Edge edge = getEdge(index);
		
		return edge.getWeight();
	}

	@Override
	public int getEdgeSize() {
		
		return routePlanner.getEdgeSize();
	}
	
	private Edge getEdge(int index) {
		
		return routePlanner.getEdge(index);
	}

	@Override
	public double getRouteLat(int index) {
		
		return routePlanner.getRouteLat(index);
	}

	@Override
	public double getRouteLng(int index) {
		
		return routePlanner.getRouteLng(index);
	}

	@Override
	public int getRouteSize() {
		
		return routePlanner.getRouteSize();
	}

	/*
	 * (non-Javadoc)
	 * Draw line chart. Please refer to http://code.google.com/p/charts4j/.
	 */
	@Override
	public String getPathImageUrl() {
		// path with contour 
		int ele_Contour_Size = routePlanner.getPathElevationList(CONTOUR).size();
		double[] ele_Contour_Path = new double[ele_Contour_Size];
		for (int i = 0; i < ele_Contour_Size; i++) {
			ele_Contour_Path[i] = routePlanner.getPathElevationList(CONTOUR).get(i);
		}
		
		int dist_Contour_Size = routePlanner.getPathDistanceList(CONTOUR).size();
		double[] dist_Contour_Path = new double[dist_Contour_Size];
		for (int i = 0; i < dist_Contour_Size; i++) {
			dist_Contour_Path[i] = routePlanner.getPathDistanceList(CONTOUR).get(i);
		}
		
		// path without contour
//		int ele_No_Contour_Size = routePlanner.getPathElevationList(NO_CONTOUR).size();
//		double[] ele_No_Contour_Path = new double[ele_No_Contour_Size];
//		for (int i = 0; i < ele_No_Contour_Size; i++) {
//			ele_No_Contour_Path[i] = routePlanner.getPathElevationList(NO_CONTOUR).get(i);
//		}
//		
//		int dist_No_Contour_Size = routePlanner.getPathDistanceList(NO_CONTOUR).size();
//		double[] dist_No_Contour_Path = new double[dist_No_Contour_Size];
//		for (int i = 0; i < dist_No_Contour_Size; i++) {
//			dist_No_Contour_Path[i] = routePlanner.getPathDistanceList(NO_CONTOUR).get(i);
//		}
		
		/*
		 * path_Dist
		 */
		int ele_pd_size = routePlanner.getPathDistElevationList().size();
		double[] ele_pd_Path = new double[ele_pd_size];
		for (int i = 0; i < ele_pd_size; i++) {
			ele_pd_Path[i] = routePlanner.getPathDistElevationList().get(i);
		}
		
		int dist_pd_size = routePlanner.getPathDistDistanceList().size();
		double[] dist_pd_Path = new double[dist_pd_size];
		for (int i = 0; i < dist_pd_size; i++) {
			dist_pd_Path[i] = routePlanner.getPathDistDistanceList().get(i);
		}
		
		// Google
		int ele_length = routePlanner.getRouteElevationList().size();
		double[] ele_Route = new double[ele_length];
		for (int i = 0; i < ele_length; i++) {
			ele_Route[i] = routePlanner.getRouteElevationList().get(i);
		}
		
		int dist_length = routePlanner.getRouteDistanceList().size();
		double[] dist_Route = new double[dist_length];
		for (int i = 0; i < dist_length; i++) {
			dist_Route[i] = routePlanner.getRouteDistanceList().get(i);
		}
		
		
		double length = (routePlanner.getPathDistance(CONTOUR) 
				> routePlanner.getPathDistance(NO_CONTOUR) 
				? routePlanner.getPathDistance(CONTOUR)
			    : routePlanner.getPathDistance(NO_CONTOUR));
		XYLine line1 = Plots.newXYLine(DataUtil.scaleWithinRange(
				0, length, dist_Contour_Path), 
				DataUtil.scaleWithinRange(0, 100, ele_Contour_Path), 
				Color.RED, "CAPRA (Normal)");
		
//		XYLine line2 = Plots.newXYLine(DataUtil.scaleWithinRange(
//				0, length, dist_No_Contour_Path), 
//				DataUtil.scaleWithinRange(0, 100, ele_No_Contour_Path), 
//				Color.ORANGE, "CAPRA (Without Contour)");
		
		XYLine line3 = Plots.newXYLine(DataUtil.scaleWithinRange(
				0, length, dist_Route), 
				DataUtil.scaleWithinRange(0, 100, ele_Route), 
				Color.BLUE, "Google");
		
		XYLine line4 = Plots.newXYLine(DataUtil.scaleWithinRange(
				0, length, dist_pd_Path), 
				DataUtil.scaleWithinRange(0, 100, ele_pd_Path), 
				Color.GREEN, "A* (Distance)");
		
		line1.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
		line1.addShapeMarkers(Shape.CIRCLE, YELLOW, 10);
        line1.addShapeMarkers(Shape.CIRCLE, BLACK, 7);
        
//        line2.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
//        line2.addShapeMarkers(Shape.DIAMOND, Color.YELLOWGREEN, 10);
//        line2.addShapeMarkers(Shape.DIAMOND, BLACK, 7);
        
        line3.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
		line3.addShapeMarkers(Shape.CIRCLE, Color.CYAN, 10);
        line3.addShapeMarkers(Shape.CIRCLE, BLACK, 7);
        
        line4.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
        line4.addShapeMarkers(Shape.DIAMOND, Color.YELLOWGREEN, 10);
        line4.addShapeMarkers(Shape.DIAMOND, BLACK, 7);
        
        XYLineChart chart = GCharts.newXYLineChart(line1, line3, line4);
        chart.setSize(600, 450);
        
        AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);
        AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 100);
        yAxis.setAxisStyle(axisStyle);
        AxisLabels yAxis1 = AxisLabelsFactory.newAxisLabels("Elevation (m)", 100.0);
        yAxis1.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 10, AxisTextAlignment.LEFT));
        AxisLabels xAxis2 = AxisLabelsFactory.newNumericRangeAxisLabels(0, length);
        xAxis2.setAxisStyle(axisStyle);
        AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Distance (m)", 100.0);
        xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 10, AxisTextAlignment.LEFT));
        
        chart.addYAxisLabels(yAxis);
        chart.addYAxisLabels(yAxis1);
        chart.addXAxisLabels(xAxis2);
        chart.addXAxisLabels(xAxis3);
        chart.setGrid(10, 6.78, 5, 0);
        
        chart.setBackgroundFill(Fills.newSolidFill(WHITE));
        chart.setAreaFill(Fills.newSolidFill(Color.WHITE));
        String url = chart.toURLString();
		
		return url;
	}

	@Override
	public String getTrainStationName(String origin) {
		
		return routePlanner.getTrainStationName(origin);
	}

	@Override
	public String getOriginLatLng() {
		
		return routePlanner.getOriginLatLng();
	}

	@Override
	public String getDesinLatLng() {
		
		return routePlanner.getDestinLatLng();
	}

	@Override
	public double getFirstPathLat(int method) {
		
		return routePlanner.getFirstPathLat(method);
	}

	@Override
	public double getFirstPathLng(int method) {
		
		return routePlanner.getFirstPathLng(method);
	}

	@Override
	public double getMoaFirstPathLat(int pathIndex) {
		// TODO Auto-generated method stub
		return routePlanner.getMoaFirstPathLat(pathIndex);
	}

	@Override
	public double getMoaFirstPathLng(int pathIndex) {
		// TODO Auto-generated method stub
		return routePlanner.getMoaFirstPathLng(pathIndex);
	}

	@Override
	public double getMoaPathLat(int stepIndex, int pathIndex) {
		// TODO Auto-generated method stub
		return routePlanner.getMoaPathLat(stepIndex, pathIndex);
	}

	@Override
	public double getMoaPathLng(int stepIndex, int pathIndex) {
		// TODO Auto-generated method stub
		return routePlanner.getMoaPathLng(stepIndex, pathIndex);
	}

	@Override
	public int getMoaPathSize(int pathIndex) {
		// TODO Auto-generated method stub
		return routePlanner.getMoaPathSize(pathIndex);
	}

	@Override
	public int getMoaSize() {
		// TODO Auto-generated method stub
		return routePlanner.getMoaSize();
	}

	@Override
	public String getDistanceText(int pathIndex) {
		
		return routePlanner.getDistanceText(pathIndex);
	}
	
	@Override
	public double getDistanceValue(int pathIndex) {
		
		return routePlanner.getDistanceValue(pathIndex);
	}

	@Override
	public String getSystemInfomation() {
		
		return routePlanner.getSystemInfomation();
	}
}
