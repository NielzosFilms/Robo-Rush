package game.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.ID;
import game.textures.Textures;

public class ItemNull extends Item {

    public ItemNull(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = Textures.placeholder;
        this.itemGround = new ItemGround(0, 0, 1, ID.NULL, this);
    }

}
