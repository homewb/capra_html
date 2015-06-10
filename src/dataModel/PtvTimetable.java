package dataModel;

/*
 * Please refer to the JSON result of PTV api
 */

public class PtvTimetable {
	private PtvPlatform ptvPlatform;
	private PtvRun ptvRun;
	private String timetableUTC;
	private String realtimeUTC;
	private String flags;
	
	public PtvTimetable(PtvPlatform ptvPlatform, PtvRun ptvRun, String timetableUTC, 
			String realtimeUTC, String flags) {
		this.ptvPlatform = ptvPlatform;
		this.ptvRun = ptvRun;
		this.timetableUTC = timetableUTC;
		this.realtimeUTC = realtimeUTC;
		this.flags = flags;
	}
	
	public PtvPlatform getPlatform() {
		return ptvPlatform;
	}
	
	public PtvRun getRun() {
		return ptvRun;
	}
	
	public String getTimetableUTC() {
		return timetableUTC;
	}
	
	public String getRealtimeUTC() {
		return realtimeUTC;
	}
	
	public String getFlags() {
		return flags;
	}
}
