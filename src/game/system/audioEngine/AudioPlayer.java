package game.system.audioEngine;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioPlayer {

	//vol = 0 > 1
	public static synchronized void playSound(AudioClip sfx, double vol, boolean loop, int start_ms) {
		Thread thread = new Thread() {
			public void run() {
				try {
					
					AudioInputStream stream = sfx.getAudioStream();
					Clip clip = AudioSystem.getClip();
					
					clip.open(stream);
					setVol(vol, clip);
					sfx.setClip(clip, vol);
					clip.setMicrosecondPosition((long)(start_ms*1000));
					clip.start();
					
					clip.getMicrosecondLength();
					
					if(loop) {
						clip.loop(Clip.LOOP_CONTINUOUSLY);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		}; thread.start();
	}
	
	private static void setVol(double vol, Clip clip) {
		FloatControl gain = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		float dB = (float) (Math.log(vol) / Math.log(10) * 20);
		gain.setValue(dB);
	}
	
	public static void stopSound(AudioClip sfx) {
		sfx.getClip().stop();
	}
	
	public static void pauzeSound(AudioClip sfx) {
		Clip clip = sfx.getClip();
		long clipTimePosition = clip.getMicrosecondPosition();
		sfx.setPauzeTime(clipTimePosition);
		clip.stop();
	}
	
	public static void resumeSound(AudioClip sfx) {
		Clip clip = sfx.getClip();
		clip.setMicrosecondPosition(sfx.getPauzeTime());
		clip.start();
	}
	
	public static void updateVolume(AudioClip sfx, double vol) {
		Clip clip = sfx.getClip();
		long clipTimePosition = clip.getMicrosecondPosition();
		sfx.setPauzeTime(clipTimePosition);
		clip.stop();
		setVol(vol, clip);
		clip.setMicrosecondPosition(clipTimePosition);
		clip.start();
	}
	
	public static boolean audioEnded(AudioClip sfx) {
		Clip clip = sfx.getClip();
		if(clip != null) {
			long clipTimePosition = clip.getMicrosecondPosition();
			return clipTimePosition >= clip.getMicrosecondLength();
		}
		return false;
	}
	
}
