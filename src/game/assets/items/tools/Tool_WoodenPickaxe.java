package game.assets.items.tools;

import game.assets.items.Item;
import game.assets.items.Item_Ground;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;

public class Tool_WoodenPickaxe extends Item {
    public Tool_WoodenPickaxe(ITEM_ID itemType) {
        super(1, itemType);
        this.tex = new Texture(TEXTURE_LIST.tools, 1, 1);
        this.itemGround = new Item_Ground(0, 0, 1, ID.Item, this);
        this.setStackable(false);
        this.setDamage(2);
        this.setAttack_speed((int)(60 * 0.75f));
    }

    public boolean place(int x, int y) {
        return false;
    }
}
