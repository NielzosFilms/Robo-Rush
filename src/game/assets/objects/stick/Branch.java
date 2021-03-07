package game.assets.objects.stick;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import game.assets.items.item.Item;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.enums.ID;
import game.system.systems.gameObject.HasItem;
import game.system.systems.gameObject.Interactable;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

public class Branch extends GameObject implements Interactable, HasItem {

    public Branch(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        int xx = new Random().nextInt(2);
        int yy = new Random().nextInt(2);
        this.tex = new Texture(TEXTURE_LIST.stick, xx, yy);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(this.tex.getTexure(), x, y, this.width, this.height, null);
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, this.width, this.height);
    }

    public Item getItem() {
        return new Item_Stick(1);
    }

    public void interact() {
//        Game.gameController.getInventorySystem().pickupItemToPlayerInv(this);
    }

}
