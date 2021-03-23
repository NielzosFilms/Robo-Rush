package game.assets.entities.bullets;

import game.system.helpers.Helpers;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class EnemyBulletHoming extends Bullet {
    private GameObject target;
    public EnemyBulletHoming(int x, int y, int z_index, int angle, GameObject created_by, GameObject target) {
        super(x, y, z_index, angle, created_by);
        this.target = target;
        this.tex = new Texture(TEXTURE_LIST.bullets, 1, 2);
        this.max_vel = 1f;
        this.lifeTime = 300;
        updateVelocity();
    }

    @Override
    public void tick() {
        this.angle = (int)Helpers.getAngle(new Point(x, y), new Point((int)((Bounds)target).getBounds().getCenterX(), (int)((Bounds)target).getBounds().getCenterY()));
        this.updateVelocity();
        buffer_x += velX;
        buffer_y += velY;

        x = Math.round(buffer_x);
        y = Math.round(buffer_y);

        if(lifeTime <= 0) {
            destroy();
        }

        lifeTime--;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x-3, y-3, 6, 6);
    }
}
