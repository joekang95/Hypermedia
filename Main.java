import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class Main {
     
    HyperVideo[] videos;
    static final int IMAGE_WIDTH = 352;
    static final int IMAGE_HEIGHT = 288;
    static final String FRAME_TYPE = ".rgb";
    static final String AUDIO_TYPE = "wav";
    
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
            videos[i] = VideoReader.importVideo(
            		directory[i].getName(), 
            		directory[i].getPath(),  
            		IMAGE_WIDTH, 
            		IMAGE_HEIGHT, 
            		FRAME_TYPE, 
            		AUDIO_TYPE);
            
            System.out.printf("Finish Importing Video %s with %d Frames - From <%s>\n", 
            		videos[i].getName(), 
            		videos[i].getFrameSize(),
            		directory[i].toString()
            );
        }
        
        //VideoPlayer videoPlayer = new VideoPlayer(videos);
        new VideoEditor(videos);
    }
     
}