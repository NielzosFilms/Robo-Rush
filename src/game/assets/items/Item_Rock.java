package game.assets.items;

import game.enums.ITEM_ID;
import game.enums.ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;

public class Item_Rock extends Item {

    public Item_Rock(int amount, ITEM_ID itemType) {
        super(amount, itemType);
        this.tex = new Texture(TEXTURE_LIST.stone, 1, 0);
        this.itemGround = new Item_Ground(0, 0, 1, ID.Item, this);
    }

    @Override
    public boolean place(int x, int y) {
        return false;
    }

}
