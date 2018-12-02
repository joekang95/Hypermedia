import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;

public class PlaySound {

  private InputStream waveStream;
  private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb
  private boolean pause = false;
  private int bytesRead = 0;
  private float seconds;

  public PlaySound(InputStream waveStream) {
		this.waveStream = waveStream;
  }

  SourceDataLine dataLine = null;
  public void play(boolean paused, int frame) throws PlayWaveException {
	  	pause = paused;
		seconds = (float)frame / 30.0f;
		
		AudioInputStream audioInputStream = null;
		try {
			InputStream bufferedIn = new BufferedInputStream(this.waveStream); // new
		    audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
		    //System.out.println(bufferedIn.available());
		} catch (UnsupportedAudioFileException e1) {
		    throw new PlayWaveException(e1);
		} catch (IOException e1) {
		    throw new PlayWaveException(e1);
		}

		// Obtain the information about the AudioInputStream
		AudioFormat audioFormat = audioInputStream.getFormat();
		Info info = new Info(SourceDataLine.class, audioFormat);

		// opens the audio channel
		try {
		    dataLine = (SourceDataLine) AudioSystem.getLine(info);
		    dataLine.open(audioFormat, this.EXTERNAL_BUFFER_SIZE);
		} catch (LineUnavailableException e1) {
		    throw new PlayWaveException(e1);
		}

		// Starts the music :P
		dataLine.start();
		int offset = 0;
		int readBytes = 0;
		byte[] audioBuffer = new byte[this.EXTERNAL_BUFFER_SIZE];
		try {
			while (readBytes != -1) {
				if(pause){
					readBytes = audioInputStream.read(audioBuffer, 0, audioBuffer.length);
					pause = false;
					float b = audioFormat.getFrameSize() * audioFormat.getFrameRate() * seconds;
					// round to nearest full frame
					long n = (long) ((b / audioFormat.getFrameSize()) * audioFormat.getFrameSize());
					offset = (int)n % EXTERNAL_BUFFER_SIZE;
					System.out.println(seconds + "  " + b + "   " + n);
					audioInputStream.skip(n);
				}
				else{
					readBytes = audioInputStream.read(audioBuffer, 0, audioBuffer.length);
					offset = 0;	
				}
				if (readBytes >= 0){
					dataLine.write(audioBuffer, offset, readBytes - offset);
					//bytesRead += readBytes;
					//long totalFrames = audioInputStream.getFrameLength();	
					//long framesRead = bytesRead / audioFormat.getFrameSize();
					//int totalSeconds = (int) (totalFrames / audioFormat.getSampleRate());
					//double elapsedSeconds = ((double) framesRead / (double) totalFrames) * totalSeconds;
					//System.out.println(n);
				}
			}
		}catch (IOException e1) {
			throw new PlayWaveException(e1);
		}finally {
			// plays what's left and and closes the audioChannel
			dataLine.drain();
			dataLine.close();
		}
  }
  
  public void stop() {
	  dataLine.close();	  
  }
  
  public void pause() {
	  dataLine.close();
  }
}
