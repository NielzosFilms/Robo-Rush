package game.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.ID;

public class ItemRock extends Item {

    public ItemRock(int amount, ID itemType, BufferedImage tex) {
        super(amount, itemType, tex);
        this.itemGround = new ItemGround(0, 0, 1, ID.Item, this);
    }

}
