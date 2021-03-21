package game.assets.entities.enemies;

import game.assets.entities.bullets.EnemyBullet;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.Attack;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;

import java.awt.*;

public class Shooting_Enemy extends GameObject implements Bounds {
    Enemy_AI ai = new Enemy_AI(Game.gameController.getPlayer(), this);

    private Timer attack_timer = new Timer(60);
    private boolean attack_state = true;

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

        attack_timer.tick();
        if(attack_timer.timerOver()) {
            attack_timer.resetTimer();
            attack();
        }
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

    public void attack() {
        int target_angle = ai.getTargetAngle();
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle, this));
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle - 24, this));
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle + 24, this));
//        if(attack_state) {
//            for (int i = 0; i < 360; i += 30) {
//                EnemyBullet bullet = new EnemyBullet(x+8, y+8, z_index, i, this);
//                Game.gameController.getHandler().addObject(bullet);
//            }
//        } else {
//            for (int i = 15; i < 360; i += 30) {
//                EnemyBullet bullet = new EnemyBullet(x+8, y+8, z_index, i, this);
//                Game.gameController.getHandler().addObject(bullet);
//            }
//        }
        attack_state = !attack_state;
    }
}
