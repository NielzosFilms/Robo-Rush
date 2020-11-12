package game.assets.items;

import game.enums.ITEM_ID;
import game.enums.ID;
import game.textures.Textures;

public class ItemRock extends Item {

    public ItemRock(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = Textures.tileSetNatureBlocks.get(49);
        this.itemGround = new ItemGround(0, 0, 1, ID.Item, this);
    }

}
