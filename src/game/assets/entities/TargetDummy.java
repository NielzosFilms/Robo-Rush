package game.assets.entities;

import game.assets.entities.particles.Particle;
import game.assets.entities.particles.Particle_DamageNumber;
import game.assets.items.Item;
import game.audioEngine.AudioFiles;
import game.audioEngine.AudioPlayer;
import game.enums.ID;
import game.system.main.Game;
import game.system.main.GameObject;
import game.system.main.Settings;
import game.textures.Textures;

import java.awt.*;

public class TargetDummy extends GameObject {
    public TargetDummy(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
    }

    public void tick() {
        int player_cenX = (int) Game.player.getBounds().getCenterX();
        int player_cenY = (int) Game.player.getBounds().getCenterY();

        if (player_cenY > getBounds().getCenterY()) {
            this.setZIndex(Game.player.getZIndex() - 1);
        } else {
            this.setZIndex(Game.player.getZIndex() + 1);
        }
    }

    public void render(Graphics g) {
        g.drawImage(Textures.tileSetHouseBlocks.get(16), x, y-height, width, height, null);
        g.drawImage(Textures.tileSetHouseBlocks.get(26), x, y, width, height, null);
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
        Game.ps.addParticle(new Particle_DamageNumber(x, y-height / 2, 0f, -0.3f, 40, damage));
        AudioPlayer.playSound(AudioFiles.crate_impact, Settings.sound_vol, false, 0);
    }
}
