package game.assets.objects;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.enums.ITEM_ID;
import game.assets.items.Item;
import game.assets.items.ItemStick;
import game.system.main.Game;
import game.system.main.GameObject;
import game.enums.ID;
import game.textures.Textures;

public class Branch extends GameObject {

    public Branch(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        this.tex = Textures.placeholder;
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
        Game.inventorySystem.pickupItemToPlayerInv(this);
    }

    @Override
    public void destroyed() {

    }

    @Override
    public void hit() {

    }

}
