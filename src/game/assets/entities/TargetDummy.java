package game.assets.entities;

import game.assets.items.Item;
import game.audioEngine.AudioFiles;
import game.audioEngine.AudioPlayer;
import game.enums.ID;
import game.system.main.Game;
import game.system.main.GameObject;
import game.system.main.Settings;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class TargetDummy extends GameObject {
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

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Item getItem() {
        return null;
    }

    public void interact() {

    }

    public void destroyed() {

    }

    public void hit(int damage) {
        AudioPlayer.playSound(AudioFiles.crate_impact, Settings.sound_vol, false, 0);
    }
}
