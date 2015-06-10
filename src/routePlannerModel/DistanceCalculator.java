package routePlannerModel;
import com.google.code.geocoder.model.LatLng;

import dataModel.Distance;
import dataModel.Node;

/*
 * Calculate distance between two locations
 */

public class DistanceCalculator {
	
	public static float getDistance(
			LatLng sourceLocation, LatLng targetLocation) {
		float lat1, lat2, lng1, lng2, dLat, dLng;
		float a, c, d;
		float radius = 6371 * 1000;
		
		lat1 = sourceLocation.getLat().floatValue();
		lat2 = targetLocation.getLat().floatValue();
		lng1 = sourceLocation.getLng().floatValue();
		lng2 = targetLocation.getLng().floatValue();
		
		dLat = degToRad(lat2 - lat1);
		dLng = degToRad(lng2 - lng1);
		
		a = (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			    Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) * 
			    Math.sin(dLng / 2) * Math.sin(dLng / 2));
		c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
		d = radius * c;
		
		return d;
	}
	
	public static float getDistance(Node sourceNode, Node targetNode) {
		float lat1, lat2, lng1, lng2, dLat, dLng;
		float a, c, d;
		float radius = 6371 * 1000;
		
		lat1 = sourceNode.getLocation().getLat().floatValue();
		lat2 = targetNode.getLocation().getLat().floatValue();
		lng1 = sourceNode.getLocation().getLng().floatValue();
		lng2 = targetNode.getLocation().getLng().floatValue();
		
		dLat = degToRad(lat2 - lat1);
		dLng = degToRad(lng2 - lng1);
		
		a = (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			    Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) * 
			    Math.sin(dLng / 2) * Math.sin(dLng / 2));
		c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
		d = radius * c;
		
		return d;		
	}
	
	private static float degToRad(float deg) {
		return (float) (deg * (Math.PI / 180));
	}

}
