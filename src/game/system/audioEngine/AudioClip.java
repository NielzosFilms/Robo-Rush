package game.system.audioEngine;

import game.system.helpers.Logger;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioClip {

	private File file;
	private Clip clip;
	private long pauzeTime;
	private double vol;
	
	public AudioClip(String path) {
		file = new File(path);
		if(!file.exists()) {
			Logger.printError("AudioClip Not Found!");
		}
	}
	
	public AudioInputStream getAudioStream() {
		try {
			
			return AudioSystem.getAudioInputStream(file);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setClip(Clip clip, double vol) {
		this.clip = clip;
	}
	
	public Clip getClip() {
		return clip;
	}
	
	public void setPauzeTime(long pauzeTime) {
		this.pauzeTime = pauzeTime;
		this.vol = vol;
	}
	
	public long getPauzeTime() {
		return this.pauzeTime;
	}

}
