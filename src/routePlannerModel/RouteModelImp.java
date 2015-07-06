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
	public void calcPath(int index) throws FileNotFoundException, 
	      XMLStreamException, LatLngException, CapraPathNotFoundException {
		
		routePlanner.calcPath(index);
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

	@Override
	public String getOriginLatLng() {
		
		return routePlanner.getOriginLatLng();
	}

	@Override
	public String getDesinLatLng() {
		
		return routePlanner.getDestinLatLng();
	}

	@Override
	public double getMoaFirstPathLat(int pathIndex) {
		
		return routePlanner.getMoaFirstPathLat(pathIndex);
	}

	@Override
	public double getMoaFirstPathLng(int pathIndex) {
		
		return routePlanner.getMoaFirstPathLng(pathIndex);
	}

	@Override
	public double getMoaPathLat(int stepIndex, int pathIndex) {
		
		return routePlanner.getMoaPathLat(stepIndex, pathIndex);
	}

	@Override
	public double getMoaPathLng(int stepIndex, int pathIndex) {
		
		return routePlanner.getMoaPathLng(stepIndex, pathIndex);
	}

	@Override
	public int getMoaPathSize(int pathIndex) {
		
		return routePlanner.getMoaPathSize(pathIndex);
	}

	@Override
	public int getMoaSize() {
		
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

	@Override
	public String getEstimateValuesText(int pathIndex) {
		
		return routePlanner.getEstimatedValue(pathIndex);
	}
}
