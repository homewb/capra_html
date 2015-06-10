package routePlannerModel;
import java.util.ArrayList;

import dataModel.PtvStop;
import dataModel.PtvTimetable;


public class PtvTimetableApi implements Ptv {
	private static final boolean MODE_NEARBY = true;
	private static final boolean MODE_LINE = false;
	private static final int UNLIMITED = 0;
	private PtvConnection con = new PtvConnection();
	private PtvJsonParser converter = new PtvJsonParser();
	
	public PtvTimetableApi() {
		
	}

	@Override
	public ArrayList<PtvStop> findStopsNearby(
			float searchLat, float searchLon) {
		String uri = "/v2/nearme/latitude/" + 
			searchLat + "/longitude/" + searchLon;
		String jsonText = con.getResponse(uri);
		
		return converter.toStopList(jsonText, MODE_NEARBY);
	}

	@Override
	public ArrayList<PtvTimetable> findBroadNextDepartures(int mode, int stopId,
			int limit) {
		String uri = "/v2/mode/" + mode + "/stop/" + stopId + 
				"/departures/by-destination/limit/" + limit;
		String jsonText = con.getResponse(uri);
		
		return converter.toTimetableList(jsonText);
	}

	@Override
	public ArrayList<Integer> findStopsOnALine(int mode, int lineId) {
		String uri = "/v2/mode/" + mode + 
				"/line/" + lineId + "/stops-for-line";
		String jsonText = con.getResponse(uri);
		
		ArrayList<Integer> stopIds = new ArrayList<Integer>();
		ArrayList<PtvStop> ptvStops = converter.toStopList(jsonText, MODE_LINE);
		
		for (int i = 0; i < ptvStops.size(); i++)
			stopIds.add(ptvStops.get(i).getStopId());
		
		return stopIds;
	}

	@Override
	public ArrayList<PtvTimetable> findSpecificNextDepartures(int mode,
			int lineId, int stopId, int directionId, int limit) {
		String uri = "/v2/mode/" + mode + "/line/" + lineId +
				"/stop/" + stopId + "/directionid/" + directionId + 
				"/departures/all/limit/" + limit;
		String jsonText = con.getResponse(uri);
		
		return converter.toTimetableList(jsonText);
	}

	@Override
	public ArrayList<PtvTimetable> findSpecificNextDepartures(int mode,
			int lineId, int stopId, int directionId, int limit, String date) {
		String uri = "/v2/mode/" + mode + "/line/" + lineId + 
				"/stop/" + stopId + "/directionid/" + directionId + 
				"/departures/all/limit/" + limit + "?for_utc=" + date;
		String jsonText = con.getResponse(uri);
		
		return converter.toTimetableList(jsonText);
	}

	@Override
	public ArrayList<PtvTimetable> findStoppingPattern(int mode, int runId,
			int stopId, String date) {
		String uri = "/v2/mode/" + mode + "/run/" + runId + 
				"/stop/" + stopId + "/stopping-pattern?for_utc=" + date;
		String jsonText = con.getResponse(uri);
		
		return converter.toTimetableList(jsonText);
	}

	@Override
	public ArrayList<Integer> findLinesPassingAStop(int mode, int stopId) {
		ArrayList<PtvTimetable> ptvTimetables = 
				findBroadNextDepartures(mode, stopId, UNLIMITED);
		
		ArrayList<Integer> lineIds = new ArrayList<Integer>();
		for (PtvTimetable t : ptvTimetables) {
			int lineId = t.getPlatform()
					      .getDirection()
					      .getLine()
					      .getLineId();
			
			if (!lineIds.contains(lineId))
				lineIds.add(lineId);
		}
		
		return lineIds;
	}
	
	

}
