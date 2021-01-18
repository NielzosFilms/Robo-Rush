package game.system.systems.menu;

import game.audio.SoundEffect;
import game.enums.MENUSTATES;
import game.system.main.Game;
import game.system.systems.menu.buttons.Button;
import game.system.systems.menu.buttons.ImageButton;
import game.textures.TEXTURE_LIST;
import game.system.inputs.MouseInput;
import game.system.helpers.Settings;
import game.system.systems.menu.menuAssets.SliderInput;
import game.textures.Fonts;
import game.textures.Texture;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class SettingsMenu extends Menu {
	private HashMap<Point, String> texts = new HashMap<>();
	public SettingsMenu(MouseInput mouse) {
		super(mouse);

		buttons.add(new Button(8, 64, 96, 16, "Save and Exit") {
			@Override
			public void handleClick(MouseEvent e) {
				SoundEffect.menu_forward.play();
				Game.settings.save();
				Game.menuSystem.setState(Game.menuSystem.getPreviousState());
			}
		});

		buttons.add(new Button(8, 80, 96, 16, "Cancel") {
			@Override
			public void handleClick(MouseEvent e) {
				SoundEffect.menu_back.play();
				Game.menuSystem.setState(Game.menuSystem.getPreviousState());
			}
		});

		//textFields.add(new TextField(new Rectangle(50, 50, 50, 50), 10, 25));
		SliderInput soundSlider = new SliderInput(48, 48, 96, mouse, Game.settings.getSound_vol());
		soundSlider.alignCenterX(screenWidth);
		texts.put(new Point(soundSlider.getX(), 44), "Sound Volume");
		this.sliders.add(soundSlider);

		SliderInput musicSlider = new SliderInput(48, 80, 96, mouse, Game.settings.getMusic_vol());
		musicSlider.alignCenterX(screenWidth);
		texts.put(new Point(musicSlider.getX(), 76), "Music Volume");
		this.sliders.add(musicSlider);

		int listSize = Textures.texture_lists.get(TEXTURE_LIST.cursors).size();
		texts.put(new Point(musicSlider.getX(), 156), "Cursor");
		int cursors_x = screenWidth/2-((24*listSize)/2);
		for(int i=0; i<listSize; i++) {
			buttons.add(new ImageButton(cursors_x + i*24, 160, 16, 16, new Texture(TEXTURE_LIST.cursors, i)) {
				public void renderAfter(Graphics g) {
					if(texture.getIndex() == Game.settings.getCursor().getIndex()) selected.renderSelection(g, new Rectangle(x, y, width, height), 4);
				}
				public void handleClick(MouseEvent e) {
					SoundEffect.menu_move_bar.play();
					Game.settings.setCursor(this.texture);
				}
			});
		}
	}

	public void tickAbs() {
		Game.settings.setSoundVolFloat(this.sliders.get(0).getValue());
		Game.settings.setMusicVolFloat(this.sliders.get(1).getValue());
	}

	public void renderBefore(Graphics g, Graphics2D g2d) {
		renderBgTiles(g);
	}

	public void renderAfter(Graphics g, Graphics2D g2d) {
		g2d.setFont(Fonts.default_fonts.get(8));
		for(Point key : texts.keySet()) {
			g.setColor(new Color(38, 43, 68));
			g.drawString(texts.get(key), key.x+1, key.y+1);
			g.setColor(new Color(90, 105, 136));
			g.drawString(texts.get(key), key.x, key.y);
		}

		g2d.setFont(Fonts.default_fonts.get(20));
		FontMetrics fm = g2d.getFontMetrics(Fonts.default_fonts.get(20));
		Rectangle2D bounds = fm.getStringBounds("Settings", g2d);
		g.setColor(new Color(38, 43, 68));
		g2d.drawString("Settings", (int)(screenWidth / 2 - bounds.getWidth() / 2)+1, 21);
		g.setColor(new Color(192, 203, 220));
		g2d.drawString("Settings", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
	}
}
