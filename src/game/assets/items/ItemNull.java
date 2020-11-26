package game.assets.items;

import game.enums.ITEM_ID;
import game.enums.ID;
import game.textures.Textures;

public class ItemNull extends Item {

    public ItemNull(int amount, ITEM_ID itemType, boolean placeable) {
        super(amount, itemType, placeable);
        this.tex = Textures.placeholder;
        this.itemGround = new ItemGround(0, 0, 1, ID.NULL, this);
    }

    @Override
    public void place() {

    }

}
