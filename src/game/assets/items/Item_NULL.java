package game.assets.items;

import game.enums.ITEM_ID;
import game.enums.ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

public class Item_NULL extends Item {

    public Item_NULL(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = new Texture(TEXTURE_LIST.house_list, 0, 0);
        this.itemGround = new Item_Ground(0, 0, 10, ID.NULL, this);
    }

    @Override
    public boolean place(int x, int y) {
        return false;
    }

}
