package recording;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Recorder {

	private String fileName;
	private boolean recording;
	private TargetDataLine targetLine;
	
	public Recorder() throws LineUnavailableException{
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		targetLine = (TargetDataLine)(AudioSystem.getLine(info));
	}
	
	public void setFile(String fileName){
		this.fileName = fileName;
	}
	
	public String getFile(){
		return fileName;
	}
	
	public void record() throws LineUnavailableException{
		recording = true;
		targetLine.open();
		targetLine.start();
		Thread thread = new Thread(){
			public void run(){
				AudioInputStream audioStream = new AudioInputStream(targetLine);
				File audioFile = new File(fileName);
				while(recording){
					try {
						AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
					} catch (IOException e) {}
				}
			}
		};
		thread.start();
	}
	
	public void stopRecording(){
		recording = false;
		targetLine.stop();
		targetLine.close();
	}
	
	public static void main(String[] args) throws LineUnavailableException, InterruptedException{
		Recorder r = new Recorder();
		r.setFile("/home/tzoorp/1.wav");
		r.record();
		Thread.sleep(5000);
		r.stopRecording();
	}
	
}
