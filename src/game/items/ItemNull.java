package game.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.ID;

public class ItemNull extends Item {

    public ItemNull(int amount, ITEM_ID itemType, BufferedImage tex) {
        super(amount, itemType, tex);
        this.itemGround = new ItemGround(0, 0, 1, ID.NULL, this);
    }

}
