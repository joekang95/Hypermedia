import java.util.ArrayList;

public class HyperFrame {

	private String path;
	private ArrayList<HyperLink> links;
	
	HyperFrame(String path){
		this.path = path;
		links = new ArrayList<HyperLink>();
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String imagePath) {
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
	
}
