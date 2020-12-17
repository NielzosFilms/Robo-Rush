package game.assets.items;

import game.assets.items.item.Item;
import game.enums.ITEM_ID;
import game.enums.ID;

public class Item_Wood extends Item {

    public Item_Wood(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = null;
        this.itemGround = new Item_Ground(0, 0, 10, ID.Item, this);
    }

}
