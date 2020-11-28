package game.assets.items;

import game.enums.ITEM_ID;
import game.enums.ID;
import game.textures.Textures;

public class Item_NULL extends Item {

    public Item_NULL(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = Textures.placeholder;
        this.itemGround = new Item_Ground(0, 0, 1, ID.NULL, this);
    }

    @Override
    public boolean place(int x, int y) {
        return false;
    }

}
