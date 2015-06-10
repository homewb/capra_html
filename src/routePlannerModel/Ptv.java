package routePlannerModel;
import java.util.ArrayList;

import dataModel.PtvStop;
import dataModel.PtvTimetable;

/*
 * PTV interface. Please refer to PTV Api document
 */

public interface Ptv {
	public ArrayList<PtvStop> findStopsNearby(float lat, float lon);
	
	public ArrayList<PtvTimetable> findBroadNextDepartures(int mode, 
			int stopId, int limit);
	
	public ArrayList<Integer> findStopsOnALine(int mode, int lineId);
	
	public ArrayList<PtvTimetable> findSpecificNextDepartures(int mode, 
			int lineId, int stopId, int directionId, int limit);
	
	public ArrayList<PtvTimetable> findSpecificNextDepartures(int mode, 
			int lineId, int stopId, int directionId, int limit, String date);
	
	public ArrayList<PtvTimetable> findStoppingPattern(int mode, int runId, 
			int stopId, String date);
	
	public ArrayList<Integer> findLinesPassingAStop(int mode, int stopId);

}
