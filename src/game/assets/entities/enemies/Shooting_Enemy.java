package game.assets.entities.enemies;

import game.assets.HealthBar;
import game.assets.entities.bullets.EnemyBullet;
import game.assets.entities.enemies.ai.AI_ACTION;
import game.assets.entities.enemies.ai.Enemy_AI;
import game.audio.SoundEffect;
import game.enums.ID;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.HUD_Rendering;
import game.system.systems.gameObject.Hitable;
import game.textures.COLOR_PALETTE;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Shooting_Enemy extends GameObject implements Bounds, Hitable, HUD_Rendering {
    Enemy_AI ai = new Enemy_AI(Game.gameController.getPlayer(), this);

    private Timer attack_timer = new Timer(60 + new Random().nextInt(4) * 20);
    private HealthBar health = new HealthBar(0, 0, 0, 3, 1);

    public Shooting_Enemy(int x, int y) {
        super(x, y, Game.gameController.getPlayer().getZIndex(), ID.Enemy);
        attack_timer.resetTimer();
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

        if (ai.inCombat()) {
            attack_timer.tick();
            if (attack_timer.timerOver()) {
                attack();
                attack_timer.resetTimer();
                attack_timer.setDelay(160);
            }
        } else {
            attack_timer.resetTimer();
        }

        health.tick();
        if (health.dead()) {
            Game.gameController.getHandler().findAndRemoveObject(this);
        }
        health.setXY(x-4, y-4);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(COLOR_PALETTE.red.color);
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
        SoundEffect.enemy_attack.play();
        int target_angle = ai.getTargetAngle();
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle, this));
//        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle - 24, this));
//        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle + 24, this));
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
    }

    @Override
    public void hit(int damage, int knockback_angle, float knockback, GameObject hit_by) {
        health.subtractHealth(damage);
        SoundEffect.enemy_hurt.play();
        ai.setVelX((float) (knockback * Math.cos(Math.toRadians(knockback_angle))));
        ai.setVelY((float) (knockback * Math.sin(Math.toRadians(knockback_angle))));
        if (ai.getAction() == AI_ACTION.stand_still) {
            ai.setAction(AI_ACTION.circle_target);
        }
    }

    @Override
    public LinkedList<GameObject> getHudObjects() {
        LinkedList<GameObject> ret = new LinkedList<>();
        ret.add(health);
        return ret;
    }
}
