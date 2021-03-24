package game.audio;

import game.enums.SETTING;
import game.system.helpers.Logger;
import game.system.main.Game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public enum SoundEffect {
	hurt_1("hurt_1.wav"),
	hurt_2("hurt_2.wav"),
	explosion("explosion.wav"),

	crate_impact("objects/crate/wood_impact.wav"),
	crate_destroy("objects/crate/destroy.wav"),
	tree_broken("objects/tree/tree_broken.wav"),
	swing("combat/swing.wav"),
	player_attack("combat/shoot.wav"),

	enemy_attack("combat/shoot.wav"),
	enemy_hurt("combat/enemy_hurt.wav"),

	boss_attack("combat/shoot.wav"),
	boss_circle("combat/boss_circle.wav"),
	boss_homing("combat/boss_homing.wav"),
	boss_shotgun("combat/boss_shotgun.wav"),

	menu_back("menu/click_back.wav"),
	menu_forward("menu/click_forward.wav"),
	menu_move_bar("menu/move_bar.wav"),

	inv_select_1("inventory/select.wav"),
	inv_select_2("inventory/select_2.wav"),
	inv_select_3("inventory/select_3.wav"),
	inv_pickup_item("inventory/pickup_item.wav"),

	futureopolis("world/Futureopolis.wav");

	private Clip clip;

	SoundEffect(String filepath) {
		try {
			URL url = ClassLoader.getSystemResource("assets/audio/" + filepath);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void play() {
		if (clip.isRunning()) clip.stop();
		clip.setFramePosition(0);
		setVol(Game.settings.getSetting(SETTING.sound_vol), clip);
		clip.start();
	}

	private static void setVol(double vol, Clip clip) {
		FloatControl gain = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		float dB = (float) (Math.log(vol) / Math.log(10) * 20);
		gain.setValue(dB);
	}

	/**
	 * Calls the constructor for all the elements.
	 * A.K.A loading all the sound effects.
	 */
	public static void init() {
		values();
		Logger.print("SoundEffects Loaded");
	}
}
