package game.system.systems.menu;

import game.audio.SoundEffect;
import game.enums.MENUSTATES;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.systems.menu.buttons.Button;
import game.textures.COLOR_PALETTE;
import game.textures.Fonts;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class MenuGameOver extends Menu {
    public MenuGameOver(MouseInput mouse) {
        super(mouse);
        buttons.add(new Button(8, 80, 96, 16, "Main menu") {
            @Override
            public void handleClick(MouseEvent e) {
                SoundEffect.menu_forward.play();
                Game.menuSystem.setState(MENUSTATES.Main);
            }
        });
    }

    @Override
    public void tickAbs() {

    }

    @Override
    public void renderBefore(Graphics g, Graphics2D g2d) {
//        renderBgTiles(g);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        g.setColor(COLOR_PALETTE.black.color);
        g.fillRect(0, 0, Game.getGameSize().x, Game.getGameSize().y);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    @Override
    public void renderAfter(Graphics g, Graphics2D g2d) {
        g2d.setFont(Fonts.default_fonts.get(20));
        g.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics(Fonts.default_fonts.get(20));
        Rectangle2D bounds = fm.getStringBounds("Game Over", g2d);

        g.setColor(new Color(38, 43, 68));
        g2d.drawString("Game Over", (int)(Game.getGameSize().x / 2 - bounds.getWidth() / 2)+1, 21);
        g.setColor(new Color(192, 203, 220));
        g2d.drawString("Game Over", (int)(Game.getGameSize().x / 2 - bounds.getWidth() / 2), 20);
    }
}
