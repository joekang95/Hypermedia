import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
 
public class VideoReader {
     
    static int FRAME_SIZE = 9000;
    static int IMAGE_WIDTH, IMAGE_HEIGHT;
    
    public static HyperVideo importVideo(String directory, int width, int height){
        HyperVideo video;
        String[] img = importFrame(directory, width, height);
        String audio = importAudio(directory);
        String name = importName(directory);
        video = new HyperVideo(img, audio, name, width, height);
        return video;

    }
 
    public static String[] importFrame(String directory, int width, int height) {
         
        System.out.println("Obtaining Frames");
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        String[] rgbFiles = new String[FRAME_SIZE];
        int i = 0;
        for (File f : listOfFiles) {
          if (f.isFile() && f.getName().endsWith(".rgb")) {
              rgbFiles[i] = f.getPath();
              i++;
          } 
        };
        IMAGE_WIDTH = width;
        IMAGE_HEIGHT = height;
        Arrays.sort(rgbFiles);
        System.out.println("Importing Frames");
        return rgbFiles;
    }
     
    public static String importName(String directory){
        String[] parts = directory.split("/");
        return parts[parts.length - 1];
    }
    
    public static String importAudio(String directory) {
        String audioPath = null;
 
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        for (File f : listOfFiles) {
          if (f.isFile() && f.getName().endsWith(".wav")) {
              audioPath = f.getPath();
              break;
          } 
        };
        return audioPath;
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
            for(int y = 0 ; y < IMAGE_HEIGHT ; y++){
                for(int x = 0 ; x < IMAGE_WIDTH ; x++){
                     
                    byte r = bytes[id];
                    byte g = bytes[id + IMAGE_HEIGHT * IMAGE_WIDTH];
                    byte b = bytes[id + IMAGE_HEIGHT * IMAGE_WIDTH * 2];
 
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
}