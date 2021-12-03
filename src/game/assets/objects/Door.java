package game.assets.objects;

import game.assets.entities.player.Player;
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
    private boolean vertical, open, mayOpen;

    private Texture verticalClosed = new Texture(TEXTURE_LIST.dungeon, 4, 6),
            verticalOpen = new Texture(TEXTURE_LIST.dungeon, 5, 6);
    public Door(int x, int y, int z_index, boolean vertical) {
        super(x, y, z_index, ID.Door);
        this.mayOpen = true;
    }

    @Override
    public void tick() {
        Player player = Game.gameController.getPlayer();
        double distanceToPlayer = Helpers.getDistance(new Point(x + 8, y + 8), new Point((int)player.getBounds().getCenterX(), (int)player.getBounds().getCenterY()));

        if(mayOpen) {
            open = distanceToPlayer < 20;
        } else open = false;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(COLOR_PALETTE.red.color);
        if(open) {
            g.drawImage(verticalOpen.getTexure(), x, y, 16, 16, null);
        } else {
            g.drawImage(verticalClosed.getTexure(), x, y, 16, 16, null);
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
}
