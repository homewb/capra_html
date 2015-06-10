package dataModel;

/*
 * Please refer to the JSON result of PTV api
 */

public class PtvPlatform {
	private int realtimeId;
	private PtvStop ptvStop;
	private PtvDirection ptvDirection;
	
	public PtvPlatform(int realtimeId, PtvStop ptvStop, PtvDirection ptvDirection) {
		this.realtimeId = realtimeId;
		this.ptvStop = ptvStop;
		this.ptvDirection = ptvDirection;
	}
	
	public int getRealtimeId() {
		return realtimeId;
	}
	
	public PtvDirection getDirection() {
		return ptvDirection;
	}
	
	public PtvStop getStop() {
		return ptvStop;
	}

}
