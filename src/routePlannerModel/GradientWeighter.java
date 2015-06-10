package routePlannerModel;

/*
 * Give weight to each edge in the graph
 */

public class GradientWeighter {
	public static int getRanking(float elevationDiff, float distance) {
		float gradient = elevationDiff / distance;
		int cost = 99999;
		
		if (gradient <= 0.0303)
			cost = 1;
		else if (gradient > 0.0303 && gradient <= 0.0417)
			cost = 2;
		else if (gradient > 0.0417 && gradient <= 0.0714)
			cost = 3;
		
		return cost;	
	}

}
