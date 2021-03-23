package game.assets.entities.enemies;

import game.assets.HealthBar;
import game.assets.entities.bullets.EnemyBullet;
import game.assets.entities.bullets.EnemyBulletHoming;
import game.assets.entities.enemies.ai.AI_ACTION;
import game.assets.entities.enemies.ai.Enemy_AI;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Hitable;
import game.textures.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

enum AttackType {
    normal,
    circle,
    predict,
    shotgun,
    homing,
}

public class Boss_Enemy extends GameObject implements Bounds, Hitable {
    Enemy_AI ai = new Enemy_AI(Game.gameController.getPlayer(), this);

    Animation walking = new Animation(6,
            new Texture(TEXTURE_LIST.joris_mage, 0, 0),
            new Texture(TEXTURE_LIST.joris_mage, 1, 0),
            new Texture(TEXTURE_LIST.joris_mage, 2, 0),
            new Texture(TEXTURE_LIST.joris_mage, 3, 0));

    private Timer attack_timer = new Timer(120),
            circle_attack_timer = new Timer(20),
            normal_attack_timer = new Timer(20),
            attack_cooldown = new Timer(30);
    private HealthBar health = new HealthBar(0, 0, 0, 100, 1);
    private AttackType attack_type = AttackType.normal;
    private boolean circle_attack_state = false;

    public Boss_Enemy(int x, int y) {
        super(x, y, Game.gameController.getPlayer().getZIndex(), ID.Boss);
        ai.setMax_vel(1.3f);
        ai.setCombat_vel(0.8f);
        ai.setWander_vel(0.2f);
        ai.setAcceleration(0.05f);
        ai.setDeceleration(0.1f);

        attack_timer.resetTimer();
    }

    @Override
    public void tick() {
        ai.tick();
        buffer_x += ai.getVelX();
        buffer_y += ai.getVelY();
        x = Math.round(buffer_x);
        y = Math.round(buffer_y);

        attack();

        health.tick();
        if(health.dead()) {
            health.destroy();
            for(GameObject bullet : Game.gameController.getHandler().getObjectsWithIds(ID.Bullet)) {
                Game.gameController.getHandler().removeObject(bullet);
            }
            Game.gameController.getHandler().findAndRemoveObject(this);
        }
        health.setXY(x-2, y-16);

        walking.runAnimation();
    }

    @Override
    public void render(Graphics g) {
//        g.setColor(COLOR_PALETTE.cherry_red.color);
//        g.fillRect(x, y, 16, 16);

        g.drawImage(Textures.entity_shadow, x-8, y+8, 32, 32, null);

        if(ai.getVelX() < 0) {
            walking.drawAnimationMirroredH(g, x-8, y-8, 32, 32);
        } else {
            walking.drawAnimation(g, x-8, y-8, 32, 32);
        }

        ai.drawAi(g, x+8, y+8);
    }

    private void attack() {
        if(attack_cooldown.timerOver()) {
            switch (attack_type) {
                case normal:
                    attackNormal();
                    attack_timer.tick();
                    if (attack_timer.timerOver()) {
                        chooseNewAttack();
                        attack_timer.resetTimer();
                    }
                    break;
                case circle:
                    attackCircle();
                    break;
                case shotgun:
                    attackShotgun();
                    chooseNewAttack();
                    break;
                case predict:
                    attackPredict();
                    chooseNewAttack();
                    break;
                case homing:
                    attackHoming();
                    chooseNewAttack();
                    break;
            }
        } else {
            attack_cooldown.tick();
        }
    }

    private void attackNormal() {
        normal_attack_timer.tick();
        if(normal_attack_timer.timerOver()) {
            normal_attack_timer.resetTimer();
            int target_angle = ai.getTargetAngle();
            Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle, this));
        }
    }

    private void attackPredict() {
        EnemyBullet bullet = new EnemyBullet(x + 8, y + 8, z_index, ai.predictBulletDirection(1.5f), this);
        Game.gameController.getHandler().addObject(bullet);
    }

    private void attackShotgun() {
        int target_angle = ai.getTargetAngle();
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle, this));
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle-8, this));
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle+8, this));
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle-16, this));
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle+16, this));
    }

    private void attackCircle() {
        if(!circle_attack_state) {
            for(int i=0; i<360; i+= 30) {
                Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, i, this));
            }
            circle_attack_state = true;
        } else {
            circle_attack_timer.tick();
            if(circle_attack_timer.timerOver()) {
                for(int i=15; i<360; i+= 30) {
                    Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, i, this));
                }
                circle_attack_timer.resetTimer();
                circle_attack_state = false;
                chooseNewAttack();
            }
        }
    }

    private void attackHoming() {
        EnemyBulletHoming bullet = new EnemyBulletHoming(x + 8, y + 8, z_index, ai.getTargetAngle(), this, ai.getTarget());
        Game.gameController.getHandler().addObject(bullet);
    }

    private void chooseNewAttack() {
        attack_cooldown.resetTimer();
        LinkedList<AttackType> types = new LinkedList<>();
        for(AttackType attackType : AttackType.values()) {
            if(attackType != attack_type) {
                types.add(attackType);
            }
        }
        this.attack_type = types.get(new Random().nextInt(types.size()));
    }

    @Override
    public void hit(int damage, int knockback_angle, float knockback, GameObject hit_by) {
        health.subtractHealth(damage);
        ai.setVelX(0);
        ai.setVelY(0);
        if(ai.getAction() == AI_ACTION.stand_still) {
            ai.setAction(AI_ACTION.circle_target);
        }
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
