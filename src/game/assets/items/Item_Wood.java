package game.assets.items;

import game.enums.ITEM_ID;
import game.enums.ID;
import game.textures.Textures;

public class Item_Wood extends Item {

    public Item_Wood(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = Textures.tileSetNatureBlocks.get(23);
        this.itemGround = new Item_Ground(0, 0, 1, ID.Item, this);
    }

    @Override
    public boolean place(int x, int y) {
        return false;
    }

}
