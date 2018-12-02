import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PlayWaveFile extends Thread{
	
    private String filename = "";
    
    PlayWaveFile(String f){
    	filename = f;
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
		    playSound.play();
		} catch (PlayWaveException e) {
		    e.printStackTrace();
		    return;
		}
    }
	
	public void stopMusic() {
		playSound.stop();
	}
	
	public void pauseMusic() {
		playSound.stop();
	}

}
