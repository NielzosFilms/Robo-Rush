package game.objects;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.items.Item;
import game.main.GameObject;
import game.main.ID;

public class Branch extends GameObject {

    public Branch(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
    }

    public void tick() {

    }

    public void render(Graphics g) {

    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public Item getItem() {
        return null;
    }

}
