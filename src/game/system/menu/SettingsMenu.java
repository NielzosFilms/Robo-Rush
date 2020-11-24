package game.system.menu;

import game.enums.MENUSTATES;
import game.system.inputs.MouseInput;
import game.system.menu.buttons.ButtonBack;
import game.system.menu.buttons.ButtonQuit;
import game.system.menu.menuAssets.SliderInput;
import game.system.menu.menuAssets.TextField;
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
		SliderInput slider = new SliderInput(50, 50, 100, mouse);
		slider.alignCenterX(screenWidth);
		this.sliders.add(slider);
	}

	public void tickAbs() {

	}

	public void renderBefore(Graphics g, Graphics2D g2d) {
		for(int y = 0;y < screenHeight;y+=16) {
			for(int x = 0;x < screenWidth;x+=16) {
				g.drawImage(Textures.tileSetForestBlocks.get(15*19 + 6), x, y, 16, 16, null);
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
