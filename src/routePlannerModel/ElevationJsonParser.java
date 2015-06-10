package routePlannerModel;


import java.util.ArrayList;
import java.util.List;

import dataModel.GElevationResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.code.geocoder.model.LatLng;

/*
 * Parse the JSON results of Google Elevation Api into Java classes
 */

public class ElevationJsonParser {
	private JSONParser parser = new JSONParser();
	
	public ElevationJsonParser() {
		
	}
	
	public List<GElevationResult> toElevationResultList(String jsonText) {
		List<GElevationResult> results = new ArrayList<GElevationResult>();
		
		try {
			Object obj = parser.parse(jsonText);
			JSONObject jsonResult = (JSONObject)obj;
			String subJsonText = 
					jsonResult.get("results").toString();
			Object subObj = parser.parse(subJsonText);
			JSONArray array = (JSONArray)subObj;
			int index = 0;
			
			while (index != array.size()) {
				results.add(this.toElevationResult(
						(JSONObject)array.get(index)));
				index++;
			}
		}
		catch (ParseException ex) {
			
		}
		
		return results;
	}
	
	private GElevationResult toElevationResult(JSONObject json) {
		String elevation = json.get("elevation").toString();
		LatLng location = toLocation((JSONObject)json.get("location"));
		String resolution = json.get("resolution").toString();
		
		return new GElevationResult(elevation, location, resolution);
	}
	
	private LatLng toLocation(JSONObject json) {
		String lat = json.get("lat").toString();
		String lng = json.get("lng").toString();
		
		return new LatLng(lat, lng);
	}
}
