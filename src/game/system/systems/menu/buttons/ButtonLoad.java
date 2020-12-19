package game.system.systems.menu.buttons;

import game.enums.BUTTONS;
import game.enums.GAMESTATES;
import game.system.audioEngine.AudioFiles;
import game.system.audioEngine.AudioPlayer;
import game.system.inputs.KeyInput;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.helpers.Logger;
import game.system.world.World;
import game.textures.Fonts;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;

public class ButtonLoad extends Button {
	private static String directory = "saves/";
	private int slot;
	private boolean slot_exists = false;

	public ButtonLoad(int x, int y, int width, int height, int slot) {
		super(x, y, width, height);
		this.btn_type = BUTTONS.Load;
		this.slot = slot;
		this.slot_exists = new File(directory + "save_slot_" + slot + ".data").exists();
	}

	public void render(Graphics g, Graphics2D g2d) {
		this.setColor(g);
		g.drawImage(Textures.default_btn, x, y, null);
		g.fillRect(x, y, width, height);

		g.setColor(Color.BLACK);
		g.setFont(Fonts.default_fonts.get(10));
		if(slot_exists) {
			g.drawString("Load Slot " + slot, x, y + height / 2);
		} else {
			g.drawString("Start Slot " + slot, x, y + height / 2);
		}
	}

	public void handleClick(MouseEvent e) {
		AudioPlayer.playSound(AudioFiles.menu_forward, 0.7f, false, 0);
		if(slot_exists) {
			loadChunks();
			World.loaded = true;
		} else {
			// TODO be able to inject seed
			Game.world.setActiveStructure(null);
			Game.world.generate(new Random().nextLong());
		}
		Game.current_loaded_save_slot = slot;
		Game.game_state = GAMESTATES.Game;
	}

	public void loadChunks() {
		Game.loadingAnimation.setLoading(true);
		if(!Game.NO_LOAD) {
			try {
				FileInputStream fis = new FileInputStream(directory + "save_slot_" + slot + ".data");
				ObjectInputStream ois = new ObjectInputStream(fis);

				World loaded_world = (World) ois.readObject();
				Textures textures = Game.textures;
				KeyInput keyInput = Game.keyInput;
				MouseInput mouseInput = Game.mouseInput;
				Game.world = loaded_world;
				Game.world.setRequirements(loaded_world.getPlayer(), textures, keyInput, mouseInput);

				ois.close();
				fis.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		Game.loadingAnimation.setLoading(false);
	}
}
