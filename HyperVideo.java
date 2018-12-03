public class HyperVideo {
     
    private int IMAGE_WIDTH, IMAGE_HEIGHT ;
    private String img[] = new String[9000];
    private String audio;
    private String name;
     
    HyperVideo(String[] img, String audio, String name,int width, int height){
        IMAGE_WIDTH = width;
        IMAGE_HEIGHT = height;
        this.img = img;
        this.audio = audio;
        //String[] parts = directory.split("/");
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
     
    int getWidth() {
        return IMAGE_WIDTH;
    }
     
    int getHeight() {
        return IMAGE_HEIGHT;
    }
     
}