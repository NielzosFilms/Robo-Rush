package game.system.systems.menu;

import game.audio.SoundEffect;
import game.enums.MENUSTATES;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.systems.menu.buttons.Button;
import game.textures.TEXTURE_LIST;
import game.system.inputs.MouseInput;
import game.textures.Fonts;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class MainMenu extends Menu {
    public MainMenu(MouseInput mouse) {
        super(mouse);
        buttons.add(new Button(8, 48, 96, 16, "Play") {
            @Override
            public void handleClick(MouseEvent e) {
                SoundEffect.menu_forward.play();
                Game.menuSystem.update();
                Game.menuSystem.setState(MENUSTATES.SaveSlotSelect);
            }
        });
        buttons.add(new Button(8, 64, 96, 16, "Settings") {
            @Override
            public void handleClick(MouseEvent e) {
                SoundEffect.menu_forward.play();
                Game.menuSystem.setState(MENUSTATES.Settings);
            }
        });
        buttons.add(new Button(8, 80, 96, 16, "Quit") {
            @Override
            public void handleClick(MouseEvent e) {
                SoundEffect.menu_back.play();
                Logger.print("(╯°□°）╯︵ ┻━┻");
                System.exit(1);
            }
        });
    }

    public void tickAbs() {}

    public void renderBefore(Graphics g, Graphics2D g2d) {
        renderBgTiles(g);
    }

    public void renderAfter(Graphics g, Graphics2D g2d) {
        g2d.setFont(Fonts.default_fonts.get(20));
        g.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics(Fonts.default_fonts.get(20));
        Rectangle2D bounds = fm.getStringBounds("Main Menu", g2d);

        g.setColor(new Color(38, 43, 68));
        g2d.drawString("Main Menu", (int)(screenWidth / 2 - bounds.getWidth() / 2)+1, 21);
        g.setColor(new Color(192, 203, 220));
        g2d.drawString("Main Menu", (int)(screenWidth / 2 - bounds.getWidth() / 2), 20);
    }

}
