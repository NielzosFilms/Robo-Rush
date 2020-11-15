package game.system.menu;

import game.system.inputs.MouseInput;
import game.system.menu.buttons.ButtonPlay;
import game.system.menu.buttons.ButtonQuit;
import game.textures.Fonts;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MainMenu extends Menu {

    public MainMenu(MouseInput mouse) {
        super(mouse);
        ButtonQuit btnQuit = new ButtonQuit(0, 200, 64, 32);
        btnQuit.alignCenterX(screenWidth);

        ButtonPlay btnPlay = new ButtonPlay(0, 50, 64, 32);
        btnPlay.alignCenterX(screenWidth);
        buttons.add(btnQuit);
        buttons.add(btnPlay);
    }

    public void renderBefore(Graphics g, Graphics2D g2d) {
    }

    public void renderAfter(Graphics g, Graphics2D g2d) {
        g2d.setFont(Fonts.gameria_fonts.get(20));
        g.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics(Fonts.gameria_fonts.get(20));
        Rectangle2D bounds = fm.getStringBounds("Main Menu", g2d);

        g2d.drawString("Main Menu", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
    }

}
