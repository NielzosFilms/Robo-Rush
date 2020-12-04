package game.assets.objects.stick;

import game.assets.items.Item;
import game.assets.items.Item_Ground;
import game.enums.ITEM_ID;
import game.enums.ID;
import game.enums.TEXTURE_LIST;
import game.textures.Texture;

public class Item_Stick extends Item {

    public Item_Stick(int amount) {
        super(amount, ITEM_ID.Stick);
        this.tex = new Texture(TEXTURE_LIST.stick, 1, 1);
        this.itemGround = new Item_Ground(0, 0, 1, ID.Item, this);
    }

    @Override
    public boolean place(int x, int y) {
        return false;
    }

}
