package routePlannerModel;

import java.io.IOException;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;

/*
 * Use Google Geocoding Api
 */

public class GeocodingApi {
	final Geocoder geocoder = new Geocoder();
	
	public GeocodingApi() {
		
	}
	
	public String getFormattedAddress(String address) {
		if (address == null)
			return null;
		
		GeocoderResult geocoderResult;
		if ((geocoderResult = getGeocoderRequest(address)) != null) {
			return geocoderResult.getFormattedAddress();
		}
		
		return null;
	}
	
	public LatLng getLocation(String address) {
		if (address == null)
			return null;
		
		GeocoderResult geocoderResult;
		if ((geocoderResult = getGeocoderRequest(address)) != null) {
			return geocoderResult.getGeometry().getLocation();
		}
		
		return null;
	}
	
	private GeocoderResult getGeocoderRequest(String address) {
		if (address == null)
			return null;
		
		GeocoderRequest geocoderRequest = 
				new GeocoderRequestBuilder().setAddress(address)
				                            .setLanguage("en")
				                            .getGeocoderRequest();
        GeocodeResponse geocoderResponse;
		try {
			geocoderResponse = geocoder.geocode(geocoderRequest);
			if (geocoderResponse.getStatus() == GeocoderStatus.OK
				      & !geocoderResponse.getResults().isEmpty()) {
				GeocoderResult geocoderResult = geocoderResponse.getResults()
						                                        .iterator()
						                                        .next();
				return geocoderResult;
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
