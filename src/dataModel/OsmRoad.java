package dataModel;

import java.util.List;

/*
 * This class is used to record street on OpenStreetMap
 */

public class OsmRoad extends OsmWay {
	private String highway;
	private String name;
	private String surface;
	
	public OsmRoad(String id, String highway, String name, 
			String surface, List<String> nodeList) {
		super(id, nodeList);
		this.highway = highway;
		this.name = name;
		this.surface = surface;
	}
	
	public String getHighway() {
		return highway;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurface() {
		return surface;
	}

}
