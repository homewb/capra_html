package dataModel;

/*
 * Please refer to the JSON result of PTV api
 */

public class PtvRun {
	private String transportType;
	private int runId;
	private int numOfSkipped;
	private int destinationId;
	private String destinationName;
	
	public PtvRun(String transportType, int runId, int numOfSkipped, 
			int destinationId, String destinationName) {
		this.transportType = transportType;
		this.runId = runId;
		this.numOfSkipped = numOfSkipped;
		this.destinationId = destinationId;
		this.destinationName = destinationName;
	}
	
	public String getTransportType() {
		return transportType;
	}
	
	public int getRunId() {
		return runId;
	}
	
	public int getNumOfSkipped() {
		return numOfSkipped;
	}
	
	public int getDestinationId() {
		return destinationId;
	}
	
	public String getDestinationName() {
		return destinationName;
	}
}
