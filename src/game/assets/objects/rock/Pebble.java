package game.assets.objects.rock;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import game.assets.items.item.Item;
import game.assets.items.Item_Ground;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.enums.ID;
import game.system.systems.gameObject.HasItem;
import game.system.systems.gameObject.Interactable;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

public class Pebble extends GameObject implements Interactable, HasItem {

    private Random r = new Random();

    public Pebble(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        int xx = new Random().nextInt(2);
        int yy = new Random().nextInt(2);
        this.tex = new Texture(TEXTURE_LIST.stone, xx, yy);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(tex.getTexure(), x, y, this.width, this.height, null);
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, this.width, this.height);
    }

    public Item getItem() {
        return new Item_Rock(1);
    }

    public void interact() {
        Game.world.getInventorySystem().pickupItemToPlayerInv(this);
        //Game.inventorySystem.pickupItemToPlayerInv(this);
    }

}
