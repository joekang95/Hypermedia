import java.util.ArrayList;

public class HyperVideo {
     
    private int width, height;
    private ArrayList<HyperFrame> frames;
    private String audio;
    private String name;
     
    HyperVideo(ArrayList<HyperFrame> frames, String audio, String name, int width, int height){
        this.width = width;
        this.height = height;
        this.frames = frames;
        this.audio = audio;
        this.name = name;
    }
     
    String getName() {
        return name;
    }
     
    String getAudio() {
        return audio;
    }
     
    HyperFrame getFrame(int i) {
        return frames.get(i);
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
     
}