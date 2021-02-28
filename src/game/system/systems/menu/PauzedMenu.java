package game.system.systems.menu;

import game.audio.SoundEffect;
import game.enums.GAMESTATES;
import game.enums.MENUSTATES;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.systems.menu.buttons.Button;
import game.textures.Fonts;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class PauzedMenu extends Menu {
    public PauzedMenu(MouseInput mouse) {
        super(mouse);
        buttons.add(new Button(8, 48, 96, 16, "Resume Game") {
            @Override
            public void handleClick(MouseEvent e) {
                SoundEffect.menu_forward.play();
                Game.game_state = GAMESTATES.Game;
            }
        });

        buttons.add(new Button(8, 64, 96, 16, "Settings") {
            @Override
            public void handleClick(MouseEvent e) {
                SoundEffect.menu_forward.play();
                Game.menuSystem.setState(MENUSTATES.Settings);
            }
        });

        buttons.add(new Button(8, 80, 96, 16, "Save Game") {
            @Override
            public void handleClick(MouseEvent e) {
                SoundEffect.menu_back.play();
                //Game.saveChunks();
                //Game.game_state = GAMESTATES.Game;
            }
        });

        buttons.add(new Button(8, 96, 96, 16, "Save and Exit") {
            @Override
            public void handleClick(MouseEvent e) {
                SoundEffect.menu_back.play();
                //Game.saveChunks();
                Game.game_state = GAMESTATES.Menu;
                Game.menuSystem.setState(MENUSTATES.Main);
            }
        });
    }

    public void tickAbs() {}

    public void renderBefore(Graphics g, Graphics2D g2d) {
        g.setColor(new Color(24, 20, 37, 128));
        g.fillRect(0, 0, screenWidth, screenHeight);
    }

    public void renderAfter(Graphics g, Graphics2D g2d) {
        g2d.setFont(Fonts.default_fonts.get(20));
        FontMetrics fm = g2d.getFontMetrics(Fonts.default_fonts.get(20));
        Rectangle2D bounds = fm.getStringBounds("Pauzed", g2d);

        g.setColor(new Color(38, 43, 68));
        g2d.drawString("Pauzed", (int)(screenWidth / 2 - bounds.getWidth() / 2)+1, 21);
        g.setColor(new Color(192, 203, 220));
        g2d.drawString("Pauzed", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
    }
}
