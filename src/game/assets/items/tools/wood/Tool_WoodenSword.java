package game.assets.items.tools.wood;

import game.assets.items.item.CanAttack;
import game.assets.items.item.Item;
import game.assets.items.Item_Ground;
import game.assets.items.item.Placeable;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

public class Tool_WoodenSword extends Item implements CanAttack {
    private int damage = 2;
    private int attack_speed = (int)(60 * 0.25f);
    public Tool_WoodenSword() {
        super(1, ITEM_ID.Wooden_Sword);
        this.tex = new Texture(TEXTURE_LIST.tools, 1, 0);
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
