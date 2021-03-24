package game.assets.entities.bullets;

import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class BulletBreak extends GameObject {
    private Animation bullet_break = new Animation(6,
            new Texture(TEXTURE_LIST.bullets, 0, 3),
            new Texture(TEXTURE_LIST.bullets, 1, 3),
            new Texture(TEXTURE_LIST.bullets, 2, 3));

    public BulletBreak(int x, int y) {
        super(x, y, Game.gameController.getPlayer().getZIndex(), ID.NULL);
    }

    @Override
    public void tick() {
        bullet_break.runAnimation();
        if(bullet_break.animationEnded()) {
            Game.gameController.getHandler().removeObject(this);
        }
    }

    @Override
    public void render(Graphics g) {
        bullet_break.drawAnimation(g, x-8, y-8);
    }
}
