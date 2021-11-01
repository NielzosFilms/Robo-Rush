package game.assets.entities.enemies;

import game.assets.HealthBar;
import game.assets.entities.bullets.EnemyBullet;
import game.assets.entities.enemies.ai.AI_ACTION;
import game.assets.entities.enemies.ai.Enemy_AI;
import game.assets.entities.orbs.EnergyOrb;
import game.assets.entities.orbs.Orb;
import game.audio.SoundEffect;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.HUD_Rendering;
import game.system.systems.gameObject.Hitable;
import game.textures.Animation;
import game.textures.COLOR_PALETTE;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Shooting_Enemy extends GameObject implements Bounds, Hitable, HUD_Rendering {
    Enemy_AI ai = new Enemy_AI(Game.gameController.getPlayer(), this);

    private Animation idle = new Animation(8,
            new Texture(TEXTURE_LIST.enemy_1, 0, 0),
            new Texture(TEXTURE_LIST.enemy_1, 1, 0),
            new Texture(TEXTURE_LIST.enemy_1, 2, 0),
            new Texture(TEXTURE_LIST.enemy_1, 3, 0)
            );

    private Animation idleTargeting = new Animation(8,
            new Texture(TEXTURE_LIST.enemy_1, 0, 1),
            new Texture(TEXTURE_LIST.enemy_1, 1, 1),
            new Texture(TEXTURE_LIST.enemy_1, 2, 1),
            new Texture(TEXTURE_LIST.enemy_1, 3, 1)
    );

    private Animation attacking = new Animation(8,
            new Texture(TEXTURE_LIST.enemy_1, 1, 2),
            new Texture(TEXTURE_LIST.enemy_1, 2, 2),
            new Texture(TEXTURE_LIST.enemy_1, 3, 2)
    );

    private Animation hit = new Animation(3,
            new Texture(TEXTURE_LIST.enemy_1, 1, 3)
    );

    private Animation death = new Animation(5,
            new Texture(TEXTURE_LIST.enemy_1, 0, 4),
            new Texture(TEXTURE_LIST.enemy_1, 1, 4),
            new Texture(TEXTURE_LIST.enemy_1, 2, 4),
            new Texture(TEXTURE_LIST.enemy_1, 3, 4)
    );

    private Animation currentRunningAnimation = idle;

    private Timer attack_timer = new Timer(60 + new Random().nextInt(4) * 20);
    private HealthBar health = new HealthBar(0, 0, 0, 3, 1);

    private boolean hasDroppedItems = false;

    public Shooting_Enemy(int x, int y) {
        super(x, y, Game.gameController.getPlayer().getZIndex(), ID.Enemy);
        attack_timer.resetTimer();
    }

    @Override
    public void tick() {
        buffer_x += velX;
        buffer_y += velY;
        x = Math.round(buffer_x);
        y = Math.round(buffer_y);



        health.tick();
        if (health.dead()) {
            currentRunningAnimation = death;
            health.setHidden(true);
            this.velX = 0;
            this.velY = 0;
            if(death.animationEnded()) {
                Game.gameController.getHandler().findAndRemoveObject(this);
            }
            if(!hasDroppedItems) {
                for(Orb orb : EnemyDrops.getSimpleDrops(x, y, 1, ID.Orb)) {
                    Game.gameController.getActiveLevel().getActiveRoom().addObject(orb);
                }
                hasDroppedItems = true;
            }
        } else {
            ai.tick();
            this.velX = ai.getVelX();
            this.velY = ai.getVelY();
            if (ai.inCombat()) {
                attack_timer.tick();
                if((currentRunningAnimation == attacking || currentRunningAnimation == hit) && currentRunningAnimation.animationEnded()) {
                    currentRunningAnimation = idleTargeting;
                    attacking.resetAnimation();
                    hit.resetAnimation();
                }
                if (attack_timer.timerOver()) {
                    currentRunningAnimation = attacking;
                    attack();
                    attack_timer.resetTimer();
                    attack_timer.setDelay(160);
                }
            } else {
                attack_timer.resetTimer();
                if((currentRunningAnimation == attacking || currentRunningAnimation == hit) && currentRunningAnimation.animationEnded()) {
                    currentRunningAnimation = idle;
                    attacking.resetAnimation();
                    hit.resetAnimation();
                }
            }
        }
        health.setXY(x-4, y-4);

        currentRunningAnimation.runAnimation();
    }

    @Override
    public void render(Graphics g) {
        g.setColor(COLOR_PALETTE.red.color);

        if(!death.animationEnded()) {
            currentRunningAnimation.drawAnimation(g, x, y, 16, 16);
        }
    }

    @Override
    public Rectangle getBounds() {
        if(health.dead()) return null;
        return new Rectangle(x, y, 16, 10);
    }

    @Override
    public Rectangle getTopBounds() {
        if(health.dead()) return null;
        return new Rectangle(x+2, y, 12, 5);
    }

    @Override
    public Rectangle getBottomBounds() {
        if(health.dead()) return null;
        return new Rectangle(x+2, y+5, 12, 5);
    }

    @Override
    public Rectangle getLeftBounds() {
        if(health.dead()) return null;
        return new Rectangle(x, y+2, 2, 6);
    }

    @Override
    public Rectangle getRightBounds() {
        if(health.dead()) return null;
        return new Rectangle(x+14, y+2, 2, 6);
    }

    public void attack() {
        SoundEffect.enemy_attack.play();
        int target_angle = ai.getTargetAngle();
        Game.gameController.getHandler().addObject(new EnemyBullet(x+8, y+8, z_index, target_angle, this));
        ai.setVelX((float) (-1.5f * Math.cos(Math.toRadians(target_angle))));
        ai.setVelY((float) (-1.5f * Math.sin(Math.toRadians(target_angle))));
    }

    @Override
    public void hit(int damage, int knockback_angle, float knockback, GameObject hit_by) {
        hit.resetAnimation();
        currentRunningAnimation = hit;
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
