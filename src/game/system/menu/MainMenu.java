package game.system.menu;

import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.menu.buttons.ButtonPlay;
import game.system.menu.buttons.ButtonQuit;
import game.system.menu.menuAssets.TextField;
import game.textures.Fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class MainMenu extends Menu {
    private TextField textField;
    public MainMenu(MouseInput mouse) {
        super(mouse);
        ButtonQuit btnQuit = new ButtonQuit(0, 200, 64, 32);
        btnQuit.alignCenterX(screenWidth);

        ButtonPlay btnPlay = new ButtonPlay(0, 50, 64, 32);
        btnPlay.alignCenterX(screenWidth);
        buttons.add(btnQuit);
        buttons.add(btnPlay);
        textField = new TextField(new Rectangle(50, 50, 50, 50), 10, 25);
    }

    public void tickAfter() {
        textField.tick();
    }

    public void renderBefore(Graphics g, Graphics2D g2d) {
    }

    public void renderAfter(Graphics g, Graphics2D g2d) {
        g2d.setFont(Fonts.gameria_fonts.get(20));
        g.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics(Fonts.gameria_fonts.get(20));
        Rectangle2D bounds = fm.getStringBounds("Main Menu", g2d);

        g2d.drawString("Main Menu", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
        textField.render(g, g2d);
    }

    public void mouseReleasedExtra(MouseEvent e) {
        if(mouse.mouseOverLocalRect(textField.getBounds())) {
            textField.setFocus(true);
        } else {
            textField.setFocus(false);
        }
    }

    public void keyPressed(KeyEvent e) {
    }

}
