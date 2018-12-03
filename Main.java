import java.io.File;
import java.io.FileFilter;
 
public class Main {
     
    static HyperVideo[] videos;
 
    public static void main(String[] args) {
        File folder = new File("./Film");
        File[] directory = folder.listFiles(new FileFilter(){
            public boolean accept(File file){
                return file.isDirectory();
            }
        });
        
        Arrays.sort(directory);
        
        HyperVideo[] videos = new HyperVideo[directory.length];
        for(int i = 0 ; i < directory.length ; i++) {
            videos[i] = new HyperVideo(directory[i].toString(), 352, 288);
            System.out.println("Finish Importing Video  " + videos[i].getName());
        }
        
        VideoPlayer videoPlayer = new VideoPlayer(videos);
        //new VideoEditor(videos);
    }
     
}