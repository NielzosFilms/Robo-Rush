package game.system.systems.menu;

import game.enums.MENUSTATES;
import game.textures.TEXTURE_LIST;
import game.system.inputs.MouseInput;
import game.system.helpers.Settings;
import game.system.systems.menu.buttons.ButtonBack;
import game.system.systems.menu.menuAssets.SliderInput;
import game.textures.Fonts;
import game.textures.Textures;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SettingsMenu extends Menu {
	public SettingsMenu(MouseInput mouse) {
		super(mouse);
		ButtonBack btnBack = new ButtonBack(0, 200, 64, 32, MENUSTATES.Main);
		btnBack.alignCenterX(screenWidth);

		this.buttons.add(btnBack);

		//textFields.add(new TextField(new Rectangle(50, 50, 50, 50), 10, 25));
		SliderInput slider = new SliderInput(50, 50, 100, mouse, 100);
		slider.alignCenterX(screenWidth);
		this.sliders.add(slider);
	}

	public void tickAbs() {
		int vol = this.sliders.get(0).getValue();
		Settings.setSoundVolPercent(vol);
	}

	public void renderBefore(Graphics g, Graphics2D g2d) {
		for(int y = 0;y < screenHeight;y+=16) {
			for(int x = 0;x < screenWidth;x+=16) {
				g.drawImage(Textures.texture_lists.get(TEXTURE_LIST.forest_list).get(new Point(6, 19)), x, y, 16, 16, null);
			}
		}
	}

	public void renderAfter(Graphics g, Graphics2D g2d) {
		g2d.setFont(Fonts.default_fonts.get(20));
		g.setColor(Color.BLACK);
		FontMetrics fm = g2d.getFontMetrics(Fonts.default_fonts.get(20));
		Rectangle2D bounds = fm.getStringBounds("Settings", g2d);

		g2d.drawString("Settings", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
	}
}
