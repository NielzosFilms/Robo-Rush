package game.assets.objects;

import game.assets.entities.player.Player;
import game.audio.SoundEffect;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Trigger;
import game.textures.COLOR_PALETTE;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class Door extends GameObject implements Bounds {
    private boolean vertical, open, locked, discovered, playedSound;

    private Texture verticalClosed = new Texture(TEXTURE_LIST.dungeon, 4, 6),
            verticalOpen = new Texture(TEXTURE_LIST.dungeon, 5, 6);

    private Texture verticalLockedClosed = new Texture(TEXTURE_LIST.dungeon, 4, 5);

    private Texture horizontalBotClosed = new Texture(TEXTURE_LIST.dungeon, 6, 7),
            horizontalBotOpen = new Texture(TEXTURE_LIST.dungeon, 6, 5),
            horizontalTopClosed = new Texture(TEXTURE_LIST.dungeon, 6, 6),
            horizontalTopOpen = new Texture(TEXTURE_LIST.dungeon, 6, 4);

    private Texture horizontalBotLockedClosed = new Texture(TEXTURE_LIST.dungeon, 5, 5),
            horizontalTopLockedClosed = new Texture(TEXTURE_LIST.dungeon, 5, 4);
    public Door(int x, int y, int z_index, boolean vertical) {
        super(x, y, z_index, ID.Door);
        this.locked = false;
        this.discovered = false;
        this.playedSound = false;
        this.vertical = vertical;
    }

    @Override
    public void tick() {
        Player player = Game.gameController.getPlayer();
        double distanceToPlayer = Helpers.getDistance(new Point(x + 8, y + 8), new Point((int)player.getBounds().getCenterX(), (int)player.getBounds().getCenterY()));

//        if(!locked) {
//            open = distanceToPlayer < 20;
//        } else open = false;

        if(!locked) {
//            if(distanceToPlayer < 20) {
            if(player.getBounds().intersects(new Rectangle(x, y, 16, 16))) {
                open = true;
                discovered = true;
                if(!playedSound) {
                    playedSound = true;
                    SoundEffect.crate_destroy.play();
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(COLOR_PALETTE.red.color);
        if(locked) {
            if (vertical) {
                g.drawImage(verticalLockedClosed.getTexure(), x, y, 16, 16, null);
            } else {
                g.drawImage(horizontalTopLockedClosed.getTexure(), x, y, 16, 16, null);
            }
        } else {
            if (vertical) {
                if (open) {
                    g.drawImage(verticalOpen.getTexure(), x, y, 16, 16, null);
                } else {
                    g.drawImage(verticalClosed.getTexure(), x, y, 16, 16, null);
                }
            } else {
                if (open) {
                    g.drawImage(horizontalTopOpen.getTexure(), x, y, 16, 16, null);
                } else {
                    g.drawImage(horizontalTopClosed.getTexure(), x, y, 16, 16, null);
                }
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return open ? null : new Rectangle(x, y, 16, 16);
    }

    @Override
    public Rectangle getTopBounds() {
        return null;
    }

    @Override
    public Rectangle getBottomBounds() {
        return null;
    }

    @Override
    public Rectangle getLeftBounds() {
        return null;
    }

    @Override
    public Rectangle getRightBounds() {
        return null;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void close() {
        this.open = false;
    }

    public void open() {
        this.open = true;
    }

    public boolean isDiscovered() {
        return this.discovered;
    }
}
