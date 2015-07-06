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
		   .append("locations=enc:");
		
		double previousLat = Double.NaN;
		double previousLng = Double.NaN;
		for (int i = 0; i < locations.size(); i++) {
			double currentLat = locations.get(i).getLat().doubleValue();
			double currentLng = locations.get(i).getLng().doubleValue();
			String encodedLat;
			String encodedLng;
			
			if (Double.isNaN(previousLat) && Double.isNaN(previousLng)) {
				encodedLat = encode(currentLat, 0.0);
				encodedLng = encode(currentLng, 0.0);			
			}
			else {
				encodedLat = encode(currentLat, previousLat);
				encodedLng = encode(currentLng, previousLng);	
			}
			
			previousLat = currentLat;
			previousLng = currentLng;
			
			url.append(encodedLat);
			url.append(encodedLng);
		}
		
//		for (int i = 0; i < locations.size(); i++) {
//			url.append(locations.get(i).getLat().floatValue());
//			url.append(",");
//			url.append(locations.get(i).getLng().floatValue());
//			if (i != locations.size() - 1)
//				url.append("|");
//		}
		
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
	
	private static String encode(double current, double previous) {
        int difference = (int) Math.floor(current * 1e5) - (int) Math.floor(previous * 1e5);
        
        StringBuffer buffer = new StringBuffer();
        
        int n = difference << 1;
        if (difference < 0) n = ~n;
        
        while (n >= 0x20) {
            int nextValue = (0x20 | (n & 0x1f)) + 63;
            buffer.append((char) (nextValue));
            n >>= 5;
          }

        n += 63;
        buffer.append((char) (n));
        
        return buffer.toString();
    }

}
