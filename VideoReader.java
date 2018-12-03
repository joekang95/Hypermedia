import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
 
public class VideoReader {
        
    public static HyperVideo importVideo(String name, String directory, int width, int height, String frameType, String audioType){
        String[] img = importFrames(directory, width, height, frameType);
        String audio = importAudio(directory, audioType);
        return new HyperVideo(img, audio, name, width, height);

    }
 
    public static String[] importFrames(String directory, int width, int height, String fileType) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles(new FileFilter(){
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(fileType);
			}
        });
        
        String[] rgbFiles = new String[listOfFiles.length];
        for(int i = 0 ; i < listOfFiles.length ; i++) {
        	rgbFiles[i] = listOfFiles[i].getPath();
        }
        
        Arrays.sort(rgbFiles);
        return rgbFiles;
    }
    
    public static String importAudio(String directory, String fileType) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles(new FileFilter(){
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(fileType);
			}
        });
        
        String audioFile = null;
        for(int i = 0 ; i < listOfFiles.length ; i++) {
        	audioFile = listOfFiles[i].getPath();
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
}