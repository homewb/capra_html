package dataModel;

import com.google.code.geocoder.model.LatLng;

/*
 * This class is used to record basic information of each node in
 * the graph.
 */

public class Node {
	private String id;
	private LatLng location;
	private float elevation;
	
	public Node(String id, LatLng location) {
		this.id = id;
		this.location = location;
	}
	
	public void setElevation(float elevation) {
		this.elevation = elevation;
	}
	
	public float getElevation() {
		return elevation;
	}
	
	public String getId() {
		return id;
	}
	
	public LatLng getLocation() {
		return location;
	}
	
	// node equals another node if they have the same id
	public boolean equals(Node another) {
		return id.equals(another.getId());
	}

}
