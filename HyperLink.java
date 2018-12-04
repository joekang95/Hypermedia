import org.json.JSONException;
import org.json.JSONObject;

public class HyperLink {
	
	private int x, y, width, height;
	private String name = null;
	
	HyperLink(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	HyperLink(int x, int y, int width, int height, String name){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
	}
	
	HyperLink(JSONObject jsonLink) throws JSONException {
		this.x = jsonLink.getInt("x");
		this.y = jsonLink.getInt("y");
		this.width = jsonLink.getInt("width");
		this.height = jsonLink.getInt("height");
		this.name = jsonLink.getString("name");
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("x", x);
		obj.put("y", y);
		obj.put("width", width);
		obj.put("height", height);
		obj.put("name", name);
		return obj;
	}
}
