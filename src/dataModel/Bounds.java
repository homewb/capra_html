package dataModel;

public class Bounds {
	private String minLat;
	private String minLng;
	private String maxLat;
	private String maxLng;
	
	public Bounds(String minLat, String minLng, 
			String maxLat, String maxLng) {
		this.minLat = minLat;
		this.minLng = minLng;
		this.maxLat = maxLat;
		this.maxLng = maxLng;
	}
	
	public String getMaxLat() {
		return maxLat;
	}
	
	public String getMaxLng() {
		return maxLng;
	}
	
	public String getMinLat() {
		return minLat;
	}
	
	public String getMinLng() {
		return minLng;
	}
	
	public float getFloatMaxLat() {
		return Float.parseFloat(maxLat);
	}
	
	public float getFloatMaxLng() {
		return Float.parseFloat(maxLng);
	}
	
	public float getFloatMinLat() {
		return Float.parseFloat(minLat);
	}
	
	public float getFloatMinLng() {
		return Float.parseFloat(minLng);
	}

}
