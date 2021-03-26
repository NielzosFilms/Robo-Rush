package game.assets.entities.bullets;

import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

public class PlayerBullet extends Bullet {
    public PlayerBullet(int x, int y, int angle, GameObject created_by) {
        super(x, y, Game.gameController.getPlayer().getZIndex(), angle, created_by);
        updateVelocity();
    }

    @Override
    public void destroy() {
        Game.gameController.getHandler().addObject(new BulletBreak(x, y));
        Game.gameController.getHandler().removeObject(this);
    }
}
