package dataModel;

/*
 * Please refer to the JSON result of PTV api
 */

public class PtvDirection {
	private int linedirId;
	private int directionId;
	private String directionName;
	private PtvLine ptvLine;
	
	public PtvDirection(int linedirId, int directionId, 
			String directionName, PtvLine ptvLine) {
		this.linedirId = linedirId;
		this.directionId = directionId;
		this.directionName = directionName;
		this.ptvLine = ptvLine;
	}
	
	public int getLinedirId() {
		return linedirId;
	}
	
	public int getDirectionId() {
		return directionId;
	}
	
	public String getDirectionName() {
		return directionName;
	}
	
	public PtvLine getLine() {
		return ptvLine;
	}

}
