package game.assets.items;

import game.enums.ITEM_ID;
import game.enums.ID;
import game.textures.Textures;

public class Item_Stick extends Item {

    public Item_Stick(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = Textures.stick.get(3);
        this.itemGround = new Item_Ground(0, 0, 1, ID.Item, this);
    }

    @Override
    public boolean place(int x, int y) {
        return false;
    }

}
