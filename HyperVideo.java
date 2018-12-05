import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.String;

public class HyperVideo {
     
    private int width, height;
    private ArrayList<HyperFrame> frames;
    private String prefixPath;
    private String audio;
    private String name;
    private Map<String, ArrayList<Integer>> allLinks;
     
    HyperVideo(String prefixPath, ArrayList<HyperFrame> frames, String audio, String name, int width, int height){
    	this.prefixPath = prefixPath + "/";
        this.width = width;
        this.height = height;
        this.frames = frames;
        this.audio = audio;
        this.name = name;
        this.allLinks = readAllLinks();
        
        try {
			this.saveMetaData();
		} catch (IOException | JSONException e) {
			System.out.println("unable initialize metadata");
		}
    }
     
    String getName() {
        return name;
    }

    String getPath() {
    	return prefixPath;
    }
    
    String getAudioPath() {
        return prefixPath + audio;
    }
     
    HyperFrame getFrame(int i) {
        return frames.get(i);
    }
    
    String getFramePath(int i) {
    	return prefixPath + frames.get(i).getName();
    }
    
    int getFrameSize() {
    	return frames.size();
    }
     
    int getWidth() {
        return width;
    }
     
    int getHeight() {
        return height;
    }
    
    public ArrayList<Integer> getLinkRange(String uuid){
    	return allLinks.get(uuid);
    }
    
    private Map<String, ArrayList<Integer>> readAllLinks() {
    	Map<String, ArrayList<Integer>> allLinks = new HashMap<String, ArrayList<Integer>>();
    	for(int index = 0; index < frames.size(); index++) {
    		HyperFrame frame = frames.get(index);
    		for(HyperLink link : frame.getLinks()) {
    			String uuid = link.getId();
    			if(!allLinks.containsKey(uuid))
    				allLinks.put(uuid, new ArrayList<Integer>());
    			allLinks.get(uuid).add(index);
    		}
    	}
    	return allLinks;
    }
    
    public void updateAllLinks(String uuid, int frameIndex) {
    	if(!allLinks.containsKey(uuid))
			allLinks.put(uuid, new ArrayList<Integer>());
		allLinks.get(uuid).add(frameIndex);
    }
    
    void saveMetaData() throws IOException, JSONException {
    	String metaDataPath = prefixPath + "metadata.json";
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(metaDataPath), "utf-8"));
    	writer.write(this.toJson().toString());
    	writer.close();
    }
     
    JSONObject toJson() throws JSONException {
    	JSONObject obj = new JSONObject();
    	obj.put("name", name);
    	obj.put("width", width);
    	obj.put("height", height);
    	obj.put("audio", audio);
    	
    	JSONArray jsonFrames = new JSONArray();
    	for(HyperFrame frame : frames) 
    		jsonFrames.put(frame.toJson());
    	
    	obj.put("frames", jsonFrames);
    	return obj;
    }
}