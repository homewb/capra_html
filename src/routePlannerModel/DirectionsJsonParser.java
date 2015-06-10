package routePlannerModel;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.code.geocoder.model.LatLng;

import dataModel.*;

/*
 * Parse the JSON results of Google Maps Directions Api into Java classes
 */

public class DirectionsJsonParser {
	private JSONParser parser = new JSONParser();
	
	public List<GDirectionsRoute> toRoutes(String jsonText) {
		try {
			Object obj = parser.parse(jsonText);
			JSONObject json = (JSONObject) obj;
			String subJsonTest = json.get("routes").toString();
			Object subObject = parser.parse(subJsonTest);
			JSONArray array = (JSONArray) subObject;
			List<GDirectionsRoute> routes = new ArrayList<GDirectionsRoute>();
			
			for (int i = 0; i < array.size(); i++) {
				routes.add(this.toDirectionsRoute((JSONObject) array.get(i)));
			}
			
			return routes;
		}
		catch (ParseException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public GDirectionsRoute toDirectionsRoute(JSONObject json) {
		try {
			String subJsonText = json.get("legs").toString();
			Object subObject = parser.parse(subJsonText);
			JSONArray array = (JSONArray) subObject;
			List<GDirectionsLeg> legs = new ArrayList<GDirectionsLeg>();
			
			for (int i = 0; i < array.size(); i++) {
				legs.add(toDirectionsLeg((JSONObject) array.get(i)));
			}
			
			String summary = json.get("summary").toString();
			
			return new GDirectionsRoute(summary, legs);
		}
		catch (ParseException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public GDirectionsLeg toDirectionsLeg(JSONObject json) {
		try {
			String subJsonText = json.get("steps").toString();
			Object subObject = parser.parse(subJsonText);
			JSONArray array = (JSONArray) subObject;
			List<GDirectionsStep> steps = new ArrayList<GDirectionsStep>();
			
			for (int i = 0; i < array.size(); i++) {
				steps.add(toDirectionsStep((JSONObject) array.get(i)));
			}
			
			Distance distance = toDistance((JSONObject) json.get("distance"));
			Duration duration = toDuration((JSONObject) json.get("duration"));
			LatLng startLocation = toLocation((JSONObject) json.get("start_location"));
			LatLng endLocation = toLocation((JSONObject) json.get("end_location"));
			String startAddress = json.get("start_address").toString();
			String endAddress = json.get("end_address").toString();
			
			return new GDirectionsLeg(steps, distance, duration, 
					startLocation, endLocation, startAddress, endAddress);
			
		}
		catch (ParseException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public GDirectionsStep toDirectionsStep(JSONObject json) {
		String htmlInstructions = json.get("html_instructions").toString();
		Distance distance = toDistance((JSONObject) json.get("distance"));
		Duration duration = toDuration((JSONObject) json.get("duration"));
		LatLng startLocation = toLocation((JSONObject) json.get("start_location"));
		LatLng endLocation = toLocation((JSONObject) json.get("end_location"));
		
		return new GDirectionsStep(htmlInstructions, distance, duration, 
				startLocation, endLocation);	
	}
	
	public Distance toDistance(JSONObject json) {
		String text = json.get("text").toString();
		long value = Long.parseLong(json.get("value").toString());
		
		return new Distance(value, text);
	}
	
	public Duration toDuration(JSONObject json) {
		String text = json.get("text").toString();
		long value = Long.parseLong(json.get("value").toString());
		
		return new Duration(value, text);
	}
	
	public LatLng toLocation(JSONObject json) {
		String lat = json.get("lat").toString();
		String lng = json.get("lng").toString();
		
		return new LatLng(lat, lng);
	}

}
