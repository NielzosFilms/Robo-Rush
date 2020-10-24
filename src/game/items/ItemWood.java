package game.items;

import java.awt.image.BufferedImage;

import game.main.ID;
import game.textures.Textures;

public class ItemWood extends Item {

    public ItemWood(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = Textures.tileSetNatureBlocks.get(23);
        this.itemGround = new ItemGround(0, 0, 1, ID.Item, this);
    }

}
