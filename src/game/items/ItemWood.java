package game.items;

import java.awt.image.BufferedImage;

import game.main.ID;

public class ItemWood extends Item {

    public ItemWood(int amount, ITEM_ID itemType, BufferedImage tex) {
        super(amount, itemType, tex);
        this.itemGround = new ItemGround(0, 0, 1, ID.Item, this);
    }

}
