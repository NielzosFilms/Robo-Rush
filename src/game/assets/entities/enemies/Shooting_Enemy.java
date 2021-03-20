package game.assets.entities.enemies;

import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;

import java.awt.*;

public class Shooting_Enemy extends GameObject {
    Enemy_AI ai = new Enemy_AI(Game.gameController.getPlayer());
    public Shooting_Enemy(int x, int y) {
        super(x, y, Game.gameController.getPlayer().getZIndex(), ID.Enemy);
    }

    @Override
    public void tick() {
        ai.tick();
        this.velX = ai.getVelX();
        this.velY = ai.getVelY();

        buffer_x += velX;
        buffer_y += velY;
        x = Math.round(buffer_x);
        y = Math.round(buffer_y);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(x, y, 16, 24);
    }
}
