package game.system.systems.menu;

import game.enums.MENUSTATES;
import game.enums.TEXTURE_LIST;
import game.system.inputs.MouseInput;
import game.system.systems.menu.buttons.ButtonBack;
import game.system.systems.menu.buttons.ButtonLoad;
import game.textures.Fonts;
import game.textures.Textures;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MenuWorldSelect extends Menu {
	public MenuWorldSelect(MouseInput mouse) {
		super(mouse);
		ButtonBack btn_back = new ButtonBack(0, 200, 64, 32, MENUSTATES.Main);
		btn_back.alignCenterX(screenWidth);

		buttons.add(btn_back);

		for(int i=1; i<=3; i++) {
			ButtonLoad btn_load = new ButtonLoad(0, i * 50, 64, 32, i);
			btn_load.alignCenterX(screenWidth);
			buttons.add(btn_load);
		}
	}

	public void tickAbs() {

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
		Rectangle2D bounds = fm.getStringBounds("Select Save Slot", g2d);

		g2d.drawString("Select Save Slot", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
	}
}
