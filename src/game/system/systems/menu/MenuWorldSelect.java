package game.system.systems.menu;

import game.audio.SoundEffect;
import game.enums.GAMESTATES;
import game.system.main.Game;
import game.system.main.GameController;
import game.system.systems.menu.buttons.Button;
import game.system.inputs.MouseInput;
import game.textures.Fonts;

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
				SoundEffect.menu_back.play();
				Game.menuSystem.setState(Game.menuSystem.getPreviousState());
			}
		});

		if(false && new File("saves/save_slot.data").exists()) {
			buttons.add(new Button(8, 48, 96, 16, "Load Game") {
				@Override
				public void handleClick(MouseEvent e) {
					SoundEffect.menu_forward.play();
//					Game.loadChunks();
					GameController.loaded = true;
					Game.game_state = GAMESTATES.Game;
				}
			});
		} else {
			buttons.add(new Button(8, 48, 96, 16, "New Game") {
				@Override
				public void handleClick(MouseEvent e) {
					SoundEffect.menu_forward.play();
//					Game.gameController.setActiveStructure(null);
					Game.gameController.generate();
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
