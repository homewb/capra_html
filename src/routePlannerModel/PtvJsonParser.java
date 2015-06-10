package routePlannerModel;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import dataModel.PtvDirection;
import dataModel.PtvLine;
import dataModel.PtvPlatform;
import dataModel.PtvRun;
import dataModel.PtvStop;
import dataModel.PtvTimetable;

/*
 * Parse the JSON results of PTV api into Java classes
 */

public class PtvJsonParser {
	private JSONParser parser = new JSONParser();
	
	public PtvJsonParser() {
		
	}
	
	public ArrayList<PtvStop> toStopList(String jsonText, boolean mode_Nearby) {
		ArrayList<PtvStop> ptvStops = new ArrayList<PtvStop>();
		
		try {
			Object obj = parser.parse(jsonText);
			JSONArray array = (JSONArray)obj;
			JSONObject jsonStopResult;
			int index = 0;
			
			while (index != array.size()) {
				jsonStopResult = (JSONObject)array.get(index);
				if (mode_Nearby)
					ptvStops.add(this.toStop(
							(JSONObject)jsonStopResult.get("result")));
				else
					ptvStops.add(this.toStop(jsonStopResult));
				index++;
			}
		}
		catch (ParseException ex) {
			
		}
		
		return ptvStops;
	}
	
	public ArrayList<PtvTimetable> toTimetableList(String jsonText) {
		ArrayList<PtvTimetable> ptvTimetables = new ArrayList<PtvTimetable>();
		
		try {
			Object obj = parser.parse(jsonText);
			JSONObject jsonTimetableResult = (JSONObject)obj;
			String subJsonText = 
					jsonTimetableResult.get("values").toString();
			Object subObj = parser.parse(subJsonText);
			JSONArray array = (JSONArray)subObj;
			int index = 0;
			
			while (index != array.size()) {
				ptvTimetables.add(this.toTimetable(
						(JSONObject)array.get(index)));
				index++;
			}
		}
		catch (ParseException ex) {
			
		}
		
		return ptvTimetables;
	}
	
	public PtvTimetable toTimetable(JSONObject json) {
		
		PtvPlatform ptvPlatform = toPlatform((JSONObject)json.get("platform"));
		PtvRun ptvRun = toRun((JSONObject)json.get("run"));
		String timetableUTC = json.get("time_timetable_utc").toString();
		
//		String realtimeUTC = json.get("time_realtime_utc").toString();
		String realtimeUTC = null;
		
		String flags = json.get("flags").toString();
				
		return new PtvTimetable(ptvPlatform, ptvRun, timetableUTC, 
			realtimeUTC, flags);
	}
	
	public PtvRun toRun(JSONObject json) {
		String transportType = json.get("transport_type").toString();
		int runId = Integer.parseInt(json.get("run_id").toString());
		int numOfSkipped = 
				Integer.parseInt(json.get("num_skipped").toString());
		int destinationId = 
				Integer.parseInt(json.get("destination_id").toString());
		String destinationName = json.get("destination_name").toString();
		
		return new PtvRun(transportType, runId, numOfSkipped, 
			destinationId, destinationName);
	}
	
	public PtvPlatform toPlatform(JSONObject json) {
		int realtimeId = 
				Integer.parseInt(json.get("realtime_id").toString());
		PtvStop ptvStop = toStop((JSONObject)json.get("stop"));
		PtvDirection ptvDirection = toDirection((JSONObject)json.get("direction"));
		
		return new PtvPlatform(realtimeId, ptvStop, ptvDirection);
	}
	
	public PtvStop toStop(JSONObject json) {
		String suburb = json.get("suburb").toString();
		String transportType = json.get("transport_type").toString();
		int stopId = Integer.parseInt(json.get("stop_id").toString());
		String locationName = json.get("location_name").toString();;
		float lat = Float.parseFloat(json.get("lat").toString());
		float lon = Float.parseFloat(json.get("lon").toString());
		float distance = 
				Float.parseFloat(json.get("distance").toString());
		
		return new PtvStop(suburb, transportType, stopId, 
				locationName, lat, lon, distance);
	}
	
	public PtvDirection toDirection(JSONObject json) {
		int linedirId = 
				Integer.parseInt(json.get("linedir_id").toString());
		int directionId = 
				Integer.parseInt(json.get("direction_id").toString());
		String directionName = json.get("direction_name").toString();
		PtvLine ptvLine = toLine((JSONObject)json.get("line"));
		
		return new PtvDirection(linedirId, directionId, directionName, ptvLine);
	}
	
	public PtvLine toLine(JSONObject json) {
		String transportType = json.get("transport_type").toString();
		int lineId = Integer.parseInt(json.get("line_id").toString());
		String lineName = json.get("line_name").toString();
		String lineNumber = json.get("line_number").toString();
		
		return new PtvLine(transportType, lineId, lineName, lineNumber);
	}
}
