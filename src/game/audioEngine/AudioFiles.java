package game.audioEngine;

public class AudioFiles {

	public static AudioClip futureopolis;

	public static AudioClip crate_impact, crete_destroy;
	
	public AudioFiles() {
		futureopolis = new AudioClip("assets/audio/world/Futureopolis.wav");
		crate_impact = new AudioClip("assets/audio/objects/crate/wood_impact.wav");
		crete_destroy = new AudioClip("assets/audio/objects/crate/destroy.wav");
	}
	
}
