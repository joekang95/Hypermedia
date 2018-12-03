public class HyperVideo {
     
    private int width, height ;
    private String img[];
    private String audio;
    private String name;
     
    HyperVideo(String[] img, String audio, String name, int width, int height){
        this.width = width;
        this.height = height;
        this.img = img;
        this.audio = audio;
        this.name = name;
    }
     
    String getName() {
        return name;
    }
     
    String getAudio() {
        return audio;
    }
     
    String getFrame(int i) {
        return img[i];
    }
    
    int getFrameSize() {
    	return img.length;
    }
     
    int getWidth() {
        return width;
    }
     
    int getHeight() {
        return height;
    }
     
}