package routePlannerModel;

import java.util.List;

import com.google.code.geocoder.model.LatLng;

import dataModel.GDirectionsRoute;

/*
 * Use Google Maps Directions Api
 */

public class DirectionsApi {
	private final String BASE_URL = 
			"https://maps.googleapis.com/maps/api/directions/";
	private String output = "json";
	private GoogleApiConnection con = new GoogleApiConnection();
	private DirectionsJsonParser parser = new DirectionsJsonParser();
	
	public List<GDirectionsRoute> getRoutes(
			LatLng startLocation, LatLng endLocation) {
		StringBuffer url = new StringBuffer(BASE_URL)
		   .append(output)
		   .append("?")
		   .append("origin=")
		   .append(startLocation.toUrlValue())
		   .append("&destination=")
		   .append(endLocation.toUrlValue())
		   .append("&mode=walking");
		
		String results = con.getResponse(url.toString());
		
		return parser.toRoutes(results);
	}

}
