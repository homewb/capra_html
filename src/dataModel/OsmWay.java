package dataModel;
import java.util.List;


public class OsmWay {
	private String id;
	private List<String> nodeList;
	
	public OsmWay(String id, List<String> nodeList) {
		this.id = id;
		this.nodeList = nodeList;
	}
	
	public String getId() {
		return id;
	}
	
	public List<String> getNodeList() {
		return nodeList;
	}

}
