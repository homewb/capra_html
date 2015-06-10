package dataModel;

/*
 * Please refer to the JSON result of PTV api
 */

public class PtvStop {
	private String suburb;
	private String transportType;
	private int stopId;
	private String locationName;
	private float lat;
	private float lon;
	private float distance;
	
	public PtvStop(String suburb, String transportType, int stopId,
			String locationName, float lat, float lon, float distance) {
		this.suburb = suburb;
		this.transportType = transportType;
		this.stopId = stopId;
		this.locationName = locationName;
		this.lat = lat;
		this.lon = lon;
		this.distance = distance;
	}
	
	public String getSuburb() {
		return suburb;
	}
	
	public String getTransportType() {
		return transportType;
	}
	
	public int getStopId() {
		return stopId;
	}
	
	public String getLocationName() {
		return locationName;
	}
	
	public float getLat() {
		return lat;
	}
	
	public float getLon() {
		return lon;
	}
	
	public float getDistance() {
		return distance;
	}
}
