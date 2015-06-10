package dataModel;

import com.google.code.geocoder.model.LatLng;

public class Boundary {
	private LatLng northeast;
	private LatLng southwest;
	
	public Boundary(LatLng northeast, LatLng southwest) {
		this.northeast = northeast;
		this.southwest = southwest;
	}
	
	public LatLng getNortheast() {
		return northeast;
	}
	
	public LatLng getSouthwest() {
		return southwest;
	}

}
