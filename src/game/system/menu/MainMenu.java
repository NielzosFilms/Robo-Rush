package game.system.menu;

import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.main.Logger;
import game.system.menu.buttons.ButtonPlay;
import game.system.menu.buttons.ButtonQuit;
import game.system.menu.buttons.ButtonSettings;
import game.system.menu.menuAssets.SliderInput;
import game.system.menu.menuAssets.TextField;
import game.textures.Fonts;
import game.textures.Textures;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class MainMenu extends Menu {
    public MainMenu(MouseInput mouse) {
        super(mouse);
        ButtonQuit btnQuit = new ButtonQuit(0, 200, 64, 32);
        btnQuit.alignCenterX(screenWidth);

        ButtonPlay btnPlay = new ButtonPlay(0, 50, 64, 32);
        btnPlay.alignCenterX(screenWidth);

        ButtonSettings btnSettings = new ButtonSettings(0, 125, 64, 32);
        btnSettings.alignCenterX(screenWidth);

        buttons.add(btnQuit);
        buttons.add(btnPlay);
        buttons.add(btnSettings);
    }

    public void tickAbs() {}

    public void renderBefore(Graphics g, Graphics2D g2d) {
        for(int y = 0;y < screenHeight;y+=16) {
            for(int x = 0;x < screenWidth;x+=16) {
                g.drawImage(Textures.forest_list.get(new Point(6, 19)), x, y, 16, 16, null);
            }
        }
    }

    public void renderAfter(Graphics g, Graphics2D g2d) {
        g2d.setFont(Fonts.default_fonts.get(20));
        g.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics(Fonts.default_fonts.get(20));
        Rectangle2D bounds = fm.getStringBounds("Main Menu", g2d);

        g2d.drawString("Main Menu", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
    }

}
