package game.assets.entities;

import game.audio.SoundEffect;
import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.system.helpers.Settings;
import game.system.systems.gameObject.Hitable;
import game.system.systems.hitbox.HitboxContainer;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class TargetDummy extends GameObject implements Hitable {
    private Texture top_tex;
    private Texture bot_tex;
    public TargetDummy(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        this.top_tex = new Texture(TEXTURE_LIST.house_list, 6, 1);
        this.bot_tex = new Texture(TEXTURE_LIST.house_list, 6, 2);
    }

    public void tick() {
//        int player_cenX = (int) Game.gameController.getPlayer().getBounds().getCenterX();
//        int player_cenY = (int) Game.gameController.getPlayer().getBounds().getCenterY();
//
//        if (player_cenY > getBounds().getCenterY()) {
//            this.setZIndex(Game.gameController.getPlayer().getZIndex() - 1);
//        } else {
//            this.setZIndex(Game.gameController.getPlayer().getZIndex() + 1);
//        }
    }

    public void render(Graphics g) {
        g.drawImage(top_tex.getTexure(), x, y-height, width, height, null);
        g.drawImage(bot_tex.getTexure(), x, y, width, height, null);
        g.setColor(new Color(254, 174, 52));
        g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void hit(int damage, int knockback_angle, float knockback, GameObject hit_by) {
        SoundEffect.crate_impact.play();
    }
}
