package routePlannerModel;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import dataModel.LatLngException;
import dataModel.CapraPathNotFoundException;

/*
 * Main interface for view components. 
 */

public interface RouteModel {
	public String getOriginCoordinate(String origin);
	
	public String getDestinationCoordinate(String destination);
	
	public String getTrainStationName(String origin);
	
	public boolean setOriginAndDestination(String origin, String destination);
	
	public String getOriginLatLng();
	
	public String getDesinLatLng();
	
	// get info from Google solution
	// ==== Start point =======
	public double getStartLat();	
	public double getStartLng();	
	public double getEndLat();	
	public double getEndLng();	
    public double getRouteLat(int index);	
	public double getRouteLng(int index);	
	public int getRouteSize();
	// ==== End point =======
	
	// get info from non-MOA solution
	// ==== Start point =======
	public double getFirstPathLat(int method);	
	public double getFirstPathLng(int method);	
	public double getPathLat(int index, int method);	
	public double getPathLng(int index, int method);	
	public int getPathSize(int method);
	// ==== End point =======
	
	// get info from MOA solution
	// ==== Start point =======
	public double getMoaFirstPathLat(int pathIndex);	
	public double getMoaFirstPathLng(int pathIndex);	
	public double getMoaPathLat(int stepIndex, int pathIndex);	
	public double getMoaPathLng(int stepIndex, int pathIndex);	
	public int getMoaPathSize(int pathIndex);
	public int getMoaSize();   // get total number of MOA solutions
	// ==== End point =======
	
	public String getDistenceText(int pathIndex);
	
	public String getSystemInfomation();
	
	public void calcPath(String interval) throws FileNotFoundException, 
	      XMLStreamException, LatLngException, CapraPathNotFoundException;
	
	public double getEdgeStartLat(int index);
	
	public double getEdgeStartLng(int index);
	
	public double getEdgeEndLat(int index);
	
	public double getEdgeEndLng(int index);
	
	public int getEdgeSize();
	
	public int getEdgeWeight(int index);
	
	public String getPathImageUrl();
	
}
