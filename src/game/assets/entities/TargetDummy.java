package game.assets.entities;

import game.assets.items.Item;
import game.system.audioEngine.AudioFiles;
import game.system.audioEngine.AudioPlayer;
import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.Collision;
import game.system.systems.gameObject.GameObject;
import game.system.helpers.Settings;
import game.system.systems.gameObject.Hitable;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class TargetDummy extends GameObject implements Collision, Hitable {
    private Texture top_tex;
    private Texture bot_tex;
    public TargetDummy(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        this.top_tex = new Texture(TEXTURE_LIST.house_list, 6, 1);
        this.bot_tex = new Texture(TEXTURE_LIST.house_list, 6, 2);
    }

    public void tick() {
        int player_cenX = (int) Game.world.getPlayer().getBounds().getCenterX();
        int player_cenY = (int) Game.world.getPlayer().getBounds().getCenterY();

        if (player_cenY > getBounds().getCenterY()) {
            this.setZIndex(Game.world.getPlayer().getZIndex() - 1);
        } else {
            this.setZIndex(Game.world.getPlayer().getZIndex() + 1);
        }
    }

    public void render(Graphics g) {
        g.drawImage(top_tex.getTexure(), x, y-height, width, height, null);
        g.drawImage(bot_tex.getTexure(), x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void hit(int damage) {
        AudioPlayer.playSound(AudioFiles.crate_impact, Settings.sound_vol, false, 0);
    }
}
