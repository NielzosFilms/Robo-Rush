package game.assets.items.tools;

import game.assets.items.Item;
import game.assets.items.Item_Ground;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.textures.Textures;

public class Tool_WoodenAxe extends Item {
    public Tool_WoodenAxe(ITEM_ID itemType) {
        super(1, itemType);
        this.tex = Textures.tools.get(5);
        this.itemGround = new Item_Ground(0, 0, 1, ID.Item, this);
        this.setStackable(false);
        this.setDamage(4);
    }

    public boolean place(int x, int y) {
        return false;
    }
}
