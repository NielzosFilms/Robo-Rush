package game.assets.entities.enemies;

import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;

import java.awt.*;

public class Shooting_Enemy extends GameObject implements Bounds {
    Enemy_AI ai = new Enemy_AI(Game.gameController.getPlayer(), this);
    public Shooting_Enemy(int x, int y) {
        super(x, y, Game.gameController.getPlayer().getZIndex(), ID.Enemy);
    }

    @Override
    public void tick() {
        ai.tick();

        buffer_x += ai.getVelX();
        buffer_y += ai.getVelY();
        x = Math.round(buffer_x);
        y = Math.round(buffer_y);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(x, y, 16, 16);

        ai.drawAi(g, x + 8, y + 8);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 16, 16);
    }

    @Override
    public Rectangle getTopBounds() {
        return new Rectangle(x+2, y, 12, 8);
    }

    @Override
    public Rectangle getBottomBounds() {
        return new Rectangle(x+2, y+8, 12, 8);
    }

    @Override
    public Rectangle getLeftBounds() {
        return new Rectangle(x, y+2, 2, 12);
    }

    @Override
    public Rectangle getRightBounds() {
        return new Rectangle(x+14, y+2, 2, 12);
    }
}
