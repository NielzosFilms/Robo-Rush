package game.assets.items.tools.wood;

import game.assets.items.item.CanAttack;
import game.assets.items.item.Item;
import game.assets.items.Item_Ground;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

public class Tool_WoodenAxe extends Item implements CanAttack {
    int damage = 4;
    int attack_speed = (int)(60 * 1.5f);
    public Tool_WoodenAxe() {
        super(1, ITEM_ID.Wooden_Axe);
        this.tex = new Texture(TEXTURE_LIST.tools, 1, 2);
        this.itemGround = new Item_Ground(0, 0, 10, ID.Item, this);
        this.setStackable(false);
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public int getAttack_speed() {
        return attack_speed;
    }

    @Override
    public void setDamage(int val) {
        this.damage = val;
    }

    @Override
    public void setAttack_speed(int val) {
        attack_speed = val;
    }
}
