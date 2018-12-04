import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HyperFrame {

	private String path;
	private ArrayList<HyperLink> links;
	
	HyperFrame(String path){
		this.path = path;
		links = new ArrayList<HyperLink>();
	}
	
	HyperFrame(JSONObject jsonFrame) throws JSONException {
		this.path = jsonFrame.getString("path");
		this.links = new ArrayList<HyperLink>();
		JSONArray jsonLinks = jsonFrame.getJSONArray("links");
		for(int i=0; i<jsonLinks.length(); i++) {
			JSONObject jsonLink = jsonLinks.optJSONObject(i);
			links.add(new HyperLink(jsonLink));
		}
	}
	
	public String getName() {
		return path;
	}
	
	public void setName(String imagePath) {
		this.path = imagePath;
	}
	
	public ArrayList<HyperLink> getLinks(){
		return links;
	}
	
	public int getLinksSize() {
		return links.size();
	}
	
	public void addLink(HyperLink link) {
		links.add(link);
	}
	
	public void removeLink(HyperLink link) {
		links.remove(link);
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("path", path);
		
		JSONArray jsonLinks = new JSONArray();
		for(HyperLink link : links) {
			jsonLinks.put(link.toJson());
		}
		
		obj.put("links", jsonLinks);
		return obj;
	}
	
}
