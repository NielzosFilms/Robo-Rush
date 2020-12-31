package game.system.audioEngine;

public class AudioFiles {

	public static AudioClip futureopolis;

	public static AudioClip crate_impact, crate_destroy, tree_broken;

	public static AudioClip menu_back, menu_forward, menu_move_bar;

	public static AudioClip inv_select_1, inv_select_2, inv_select_3, inv_pickup_item;

	public static AudioClip swing;

	public static AudioClip
			hurt_1 = new AudioClip("assets/audio/hurt_1.wav"),
			hurt_2 = new AudioClip("assets/audio/hurt_2.wav"),
			explosion = new AudioClip("assets/audio/explosion.wav");
	
	public AudioFiles() {
		futureopolis = new AudioClip("assets/audio/world/Futureopolis.wav");
		crate_impact = new AudioClip("assets/audio/objects/crate/wood_impact.wav");
		crate_destroy = new AudioClip("assets/audio/objects/crate/destroy.wav");
		tree_broken = new AudioClip("assets/audio/objects/tree/tree_broken.wav");
		swing = new AudioClip("assets/audio/combat/swing.wav");

		menu_back = new AudioClip("assets/audio/menu/click_back.wav");
		menu_forward = new AudioClip("assets/audio/menu/click_forward.wav");
		menu_move_bar = new AudioClip("assets/audio/menu/move_bar.wav");

		inv_select_1 = new AudioClip("assets/audio/inventory/select.wav");
		inv_select_2 = new AudioClip("assets/audio/inventory/select_2.wav");
		inv_select_3 = new AudioClip("assets/audio/inventory/select_3.wav");
		inv_pickup_item = new AudioClip("assets/audio/inventory/pickup_item.wav");
	}
	
}
