package game.objects;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.items.ITEM_ID;
import game.items.Item;
import game.items.ItemStick;
import game.main.Game;
import game.main.GameObject;
import game.main.ID;
import game.textures.Textures;

public class Branch extends GameObject {

    public Branch(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        this.tex = Textures.wood;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(this.tex, x, y, this.width, this.height, null);
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, this.width, this.height);
    }

    public Item getItem() {
        return new ItemStick(1, ITEM_ID.Stick);
    }

    public void interact() {
        Game.inventoryOLD.pickupItem(this);
    }

}
