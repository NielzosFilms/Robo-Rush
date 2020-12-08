package game.assets.items.tools.wood;

import game.assets.items.Item;
import game.assets.items.Item_Ground;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

public class Tool_WoodenSword extends Item {
    public Tool_WoodenSword() {
        super(1, ITEM_ID.Wooden_Sword);
        this.tex = new Texture(TEXTURE_LIST.tools, 1, 0);
        this.itemGround = new Item_Ground(0, 0, 10, ID.Item, this);
        this.setStackable(false);
        this.setDamage(2);
        this.setAttack_speed((int)(60 * 0.25f));
    }

    public boolean place(int x, int y) {
        return false;
    }
}
