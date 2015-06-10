package routePlannerModel;

/*
 * Use Google Elevation Api
 */

import dataModel.GElevationResult;

import java.util.List;

import com.google.code.geocoder.model.LatLng;

public class ElevationApi {
	private final String BASE_URL = 
			"https://maps.googleapis.com/maps/api/elevation/";
	private String output = "json";
	private GoogleApiConnection con = new GoogleApiConnection();
	private ElevationJsonParser parser = new ElevationJsonParser();
	
	public ElevationApi() {
		
	}
	
	public List<GElevationResult> getElevationByLocations(
			List<LatLng> locations) {
		StringBuffer url = new StringBuffer(BASE_URL)
		   .append(output)
		   .append("?")
		   .append("locations=");
		
		for (int i = 0; i < locations.size(); i++) {
			url.append(locations.get(i).getLat().floatValue());
			url.append(",");
			url.append(locations.get(i).getLng().floatValue());
			if (i != locations.size() - 1)
				url.append("|");
		}
		String results = con.getResponse(url.toString());
		
		return parser.toElevationResultList(results);
	}
	
	public List<GElevationResult> getElevationByPath(
			List<LatLng> locations, int sample) {
		StringBuffer url = new StringBuffer(BASE_URL)
		   .append(output)
		   .append("?")
		   .append("path=");
		
		for (int i = 0; i < locations.size(); i++) {
			url.append(locations.get(i).getLat().floatValue());
			url.append(",");
			url.append(locations.get(i).getLng().floatValue());
			if (i != locations.size() - 1)
				url.append("|");
		}
		url.append("&samples=");
		url.append(sample);
		
		String results = con.getResponse(url.toString());
		return parser.toElevationResultList(results);
		
	}

}
