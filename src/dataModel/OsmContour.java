package dataModel;

import java.util.List;

/*
 * This class is used to record contour line on OpenStreetMap
 */

public class OsmContour extends OsmWay {
	private String contour;
	private String elevation;
	
	public OsmContour(String id, String contour, String elevation, 
			List<String> nodeList) {
		super(id, nodeList);
		this.contour = contour;
		this.elevation = elevation;
	}
	
	public String getContour() {
		return contour;
	}
	
	public String getElevation() {
		return elevation;
	}

}
