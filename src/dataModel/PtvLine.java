package dataModel;

/*
 * Please refer to the JSON result of PTV api
 */

public class PtvLine {
	private String transportType;
	private int lineId;
	private String lineName;
	private String lineNumber;
	
	public PtvLine(String transportType, int lineId, 
			String lineName, String lineNumber) {
		this.transportType = transportType;
		this.lineId = lineId;
		this.lineName = lineName;
		this.lineNumber = lineNumber;
	}
	
	public String getTransportType() {
		return transportType;
	}
	
	public int getLineId() {
		return lineId;
	}
	
	public String getLineName() {
		return lineName;
	}
	
	public String getLineNumber() {
		return lineNumber;
	}
}
