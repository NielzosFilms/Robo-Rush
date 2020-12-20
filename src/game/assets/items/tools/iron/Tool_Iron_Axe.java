package game.assets.items.tools.iron;

import game.assets.items.Item_Ground;
import game.assets.items.item.CanAttack;
import game.assets.items.item.Item;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

public class Tool_Iron_Axe extends Item implements CanAttack {
    int damage = 4;
    int attack_speed = (int)(60 * 1.5f);
    public Tool_Iron_Axe(int amount) {
        super(amount, ITEM_ID.Iron_Axe);
        this.tex = new Texture(TEXTURE_LIST.tools, 0, 2);
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
        damage = val;
    }

    @Override
    public void setAttack_speed(int val) {
        attack_speed = val;
    }
}
