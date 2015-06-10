package dataModel;

import java.math.BigDecimal;

import com.google.code.geocoder.model.LatLng;

/*
 * Please refer to the JSON result of Google Elevation API
 */

public class GElevationResult {
	private BigDecimal elevation;
	private LatLng location;
	private BigDecimal resolution;
	
	public GElevationResult(BigDecimal elevation, 
			LatLng location, BigDecimal resolution) {
		this.elevation = elevation;
		this.location = location;
		this.resolution = resolution;
	}
	
	public GElevationResult(
			String elevation, LatLng location, String resolution) {
		this.elevation = new BigDecimal(elevation);
		this.location = location;
		this.resolution = new BigDecimal(resolution);
	}
	
	public BigDecimal getElevation() {
		return elevation;
	}
	
	public LatLng getLocation() {
		return location;
	}
	
	public BigDecimal getResolution() {
		return resolution;
	}

}
