package Final;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
 
public class PlayWaveFile extends Thread{
     
    private String filename = "";
    private boolean pause = false;
    private int frame = 0;
     
    PlayWaveFile(String f, boolean r, int c){
        filename = f;
        pause = r;
        frame = c;
    }
     
    PlaySound playSound;
    public void run() {
        // opens the inputStream
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }   
         
        // initializes the playSound Object
        playSound = new PlaySound(inputStream);
 
        // plays the sound
        try {
            playSound.play(pause, frame);
        } catch (PlayWaveException e) {
            e.printStackTrace();
            return;
        }
    }
     
    public void stopMusic() {
        playSound.stop();
    }
     
    public void pauseMusic(int frame) {
        playSound.pause();
    }
 
}