package game.system.menu.buttons;

import game.enums.BUTTONS;
import game.enums.GAMESTATES;
import game.system.main.Game;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class ButtonPlay extends Button {
    public ButtonPlay(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.btn_type = BUTTONS.Play;
    }

    public void render(Graphics g, Graphics2D g2d) {
        this.setColor(g);
        g.drawImage(Textures.default_btn, x, y, null);
        g.fillRect(x, y, width, height);
    }

    public void handleClick(MouseEvent e) {
        Game.world.generate(new Random().nextLong());
        Game.game_state = GAMESTATES.Game;
    }
}
