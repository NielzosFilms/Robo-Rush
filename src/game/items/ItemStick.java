package game.items;

import java.awt.image.BufferedImage;

import game.main.ID;
import game.textures.Textures;

public class ItemStick extends Item {

    public ItemStick(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = Textures.wood;
        this.itemGround = new ItemGround(0, 0, 1, ID.Item, this);
    }

}
