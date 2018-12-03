import java.io.File;
 
public class Main {
     
    static HyperVideo[] videos;
 
    public static void main(String[] args) {
        File folder = new File("./Film");
        File[] directory = folder.listFiles();
        HyperVideo[] videos = new HyperVideo[directory.length];
        for(int i = 0 ; i < directory.length ; i++) {
            videos[i] = new HyperVideo(directory[i].toString(), 352, 288);
            System.out.println("Finish Importing Video  " + videos[i].getName());
        }
        //VideoPlayer videoPlayer = new VideoPlayer(videos);
        new VideoEditor(videos);
    }
     
}