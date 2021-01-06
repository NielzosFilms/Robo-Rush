package game.system.systems.menu;

import game.enums.GAMESTATES;
import game.enums.MENUSTATES;
import game.system.audioEngine.AudioFiles;
import game.system.audioEngine.AudioPlayer;
import game.system.main.Game;
import game.system.systems.menu.buttons.Button;
import game.system.world.World;
import game.textures.TEXTURE_LIST;
import game.system.inputs.MouseInput;
import game.textures.Fonts;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class MenuWorldSelect extends Menu {
	public MenuWorldSelect(MouseInput mouse) {
		super(mouse);

		buttons.add(new Button(8, 80, 96, 16, "Back") {
			@Override
			public void handleClick(MouseEvent e) {
				AudioPlayer.playSound(AudioFiles.menu_back, 0.7f, false, 0);
				Game.menuSystem.setState(Game.menuSystem.getPreviousState());
			}
		});

		if(new File("saves/save_slot.data").exists()) {
			buttons.add(new Button(8, 48, 96, 16, "Load Game") {
				@Override
				public void handleClick(MouseEvent e) {
					AudioPlayer.playSound(AudioFiles.menu_forward, 0.7f, false, 0);
					Game.loadChunks();
					World.loaded = true;
					Game.game_state = GAMESTATES.Game;
				}
			});
		} else {
			buttons.add(new Button(8, 48, 96, 16, "New Game") {
				@Override
				public void handleClick(MouseEvent e) {
					AudioPlayer.playSound(AudioFiles.menu_forward, 0.7f, false, 0);
					Game.world.setActiveStructure(null);
					Game.world.generate();
					Game.game_state = GAMESTATES.Game;
				}
			});
		}
	}

	public void tickAbs() {

	}

	public void renderBefore(Graphics g, Graphics2D g2d) {
		renderBgTiles(g);
	}

	public void renderAfter(Graphics g, Graphics2D g2d) {
		g2d.setFont(Fonts.default_fonts.get(20));
		g.setColor(Color.BLACK);
		FontMetrics fm = g2d.getFontMetrics(Fonts.default_fonts.get(20));
		Rectangle2D bounds = fm.getStringBounds("Select Save Slot", g2d);

		g.setColor(new Color(38, 43, 68));
		g2d.drawString("Select Save Slot", (int)(screenWidth / 2 - bounds.getWidth() / 2)+1, 21);
		g.setColor(new Color(192, 203, 220));
		g2d.drawString("Select Save Slot", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
	}
}
