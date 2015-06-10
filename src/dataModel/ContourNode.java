package dataModel;

import com.google.code.geocoder.model.LatLng;

/*
 * ContourNode records each node crossing between street and contour line
 * on OpenStreetMap
 */

public class ContourNode extends Node {
	private String street;
	
	public ContourNode(String id, LatLng location, String street) {
		super(id, location);
		this.street = street;
	}
	
	public String getStreet() {
		return street;
	}

}
