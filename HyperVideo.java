public class HyperVideo {
     
    private int IMAGE_WIDTH, IMAGE_HEIGHT ;
    private String img[] = new String[9000];
    private String audio;
    private String name;
     
    HyperVideo(String directory, int width, int height){
        IMAGE_WIDTH = width;
        IMAGE_HEIGHT = height;
        img = VideoReader.importFrame(directory, width, height);
        audio = VideoReader.importAudio(directory);
        String[] parts = directory.split("/");
        name = parts[parts.length - 1];
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