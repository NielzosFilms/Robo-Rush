package game.system.menu;

import game.enums.MENUSTATES;
import game.system.inputs.MouseInput;
import game.system.menu.buttons.ButtonMainMenu;
import game.system.menu.buttons.ButtonResume;
import game.system.menu.buttons.ButtonSave;
import game.system.menu.buttons.ButtonSettings;
import game.textures.Fonts;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class PauzedMenu extends Menu {
    public PauzedMenu(MouseInput mouse) {
        super(mouse);
        buttons.add(new ButtonMainMenu(0, 200, 64, 32));
        buttons.get(0).alignCenterX(screenWidth);

        buttons.add(new ButtonSettings(0, 100, 64, 32));
        buttons.get(1).alignCenterX(screenWidth);

        buttons.add(new ButtonResume(0, 50, 64, 32));
        buttons.get(2).alignCenterX(screenWidth);

        buttons.add(new ButtonSave(0, 150, 64, 32));
        buttons.get(3).alignCenterX(screenWidth);
    }

    public void tickAbs() {}

    public void renderBefore(Graphics g, Graphics2D g2d) {
        g.setColor(new Color(0, 0, 0, 0.5f));
        g.fillRect(0, 0, screenWidth, screenHeight);
    }

    public void renderAfter(Graphics g, Graphics2D g2d) {
        g2d.setFont(Fonts.default_fonts.get(20));
        g.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics(Fonts.default_fonts.get(20));
        Rectangle2D bounds = fm.getStringBounds("Pauzed", g2d);

        g2d.drawString("Pauzed", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
    }
}
