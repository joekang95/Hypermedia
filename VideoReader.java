import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
 
public class VideoReader {
        
    public static HyperVideo importVideo(String name, String directory, int width, int height, String frameType, String audioType){
    	ArrayList<HyperFrame> frames = null;
    	String audio = null;
    	ArrayList<Integer> scenes = null;
    	try {
    		JSONObject metaJson = importMetaData(directory);
    		JSONArray jsonFrames = metaJson.getJSONArray("frames");
    		JSONArray jsonScenes = metaJson.getJSONArray("scenes");
    		frames = importFrames(jsonFrames);
    		scenes = importScenes(jsonScenes);
    		audio = metaJson.getString("audio");
    	}
    	catch (JSONException | FileNotFoundException e){
    		System.out.printf("Initial %s metadata\n", name);
    		frames = importFrames(directory, frameType);
    		audio = importAudio(directory, audioType);
    		scenes = new ArrayList<Integer>();
    	}	
        return new HyperVideo(directory, frames, scenes, audio, name, width, height);
    }
 
    public static JSONObject importMetaData(String directory) throws FileNotFoundException, JSONException{
    	String metaDataPath = directory + "/" + "metadata.json";
    	JSONObject metaJson = new JSONObject(new JSONTokener(new BufferedReader(new FileReader(metaDataPath))));
		return metaJson;
    }
   
    public static ArrayList<HyperFrame> importFrames(JSONArray jsonFrames) throws JSONException{
    	ArrayList<HyperFrame> frames = new ArrayList<HyperFrame>();
    	for(int i=0; i<jsonFrames.length(); i++) {
			JSONObject jsonFrame = jsonFrames.getJSONObject(i);
			frames.add(new HyperFrame(jsonFrame));
		}
    	return frames;
    }
    
    public static ArrayList<HyperFrame> importFrames(String directory, String fileType) {
        File folder = new File(directory);
        String[] frameNames = folder.list(new FilenameFilter() {
        	public boolean accept(File file, String name) {
				return name.toLowerCase().endsWith(fileType);
			}
        });
        Arrays.sort(frameNames);
        
        ArrayList<HyperFrame> frames = new ArrayList<HyperFrame>();
        for(String frameName: frameNames) {
        	frames.add(new HyperFrame(frameName));
        }
        return frames;
    }
    
    public static ArrayList<Integer> importScenes(JSONArray jsonScenes) throws JSONException{
    	ArrayList<Integer> scenes = new ArrayList<Integer>();
    	for(int i=0; i<jsonScenes.length(); i++) {
    		scenes.add((Integer) jsonScenes.get(i));
    	}
    	return scenes;	
    }
    
    public static String importAudio(String directory, String fileType) {
        File folder = new File(directory);
        String[] audioNames = folder.list(new FilenameFilter() {
        	public boolean accept(File file, String name) {
				return name.toLowerCase().endsWith(fileType);
			}
        });
        Arrays.sort(audioNames);
        
        String audioFile = null;
        for(String audioName: audioNames) {
        	audioFile = audioName;
        	break;
        }
        return audioFile;
    }
     
    public static String importName(String directory){
        String[] parts = directory.split("/");
        return parts[parts.length - 1];
    }
   
    public static void importImage(BufferedImage img, String fileName) {
    	try{      	
            File file = new File(fileName);
            InputStream is = new FileInputStream(file);
     
            long len = file.length();
            byte[] bytes = new byte[(int)len];
             
            int offset = 0, numRead = 0;
             
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0){
                offset += numRead;
            }
             
            int id = 0;
            for(int y = 0 ; y < img.getHeight(); y++){
                for(int x = 0 ; x < img.getWidth(); x++){
                     
                    byte r = bytes[id];
                    byte g = bytes[id + img.getHeight() * img.getWidth()];
                    byte b = bytes[id + img.getHeight() * img.getWidth() * 2];
 
                    int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    img.setRGB(x, y, pix);
                    id++;
                }
            }
            is.close();
            
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public static BufferedImage importImage(String fileName, int width, int height) throws IOException {
    	BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    		
        File file = new File(fileName);
        InputStream is = new FileInputStream(file);
     
        long len = file.length();
        byte[] bytes = new byte[(int)len];
             
        int offset = 0, numRead = 0;
             
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0){
        	offset += numRead;
        }
             
        int id = 0;
        for(int y = 0 ; y < img.getHeight(); y++){
        	for(int x = 0 ; x < img.getWidth(); x++){
                     
                byte r = bytes[id];
                byte g = bytes[id + img.getHeight() * img.getWidth()];
                byte b = bytes[id + img.getHeight() * img.getWidth() * 2];
 
                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                img.setRGB(x, y, pix);
                id++;
            }
        }
        is.close();
        return img;
    }
}