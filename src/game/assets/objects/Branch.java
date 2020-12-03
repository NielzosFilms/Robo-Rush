package game.assets.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import game.enums.ITEM_ID;
import game.assets.items.Item;
import game.assets.items.Item_Stick;
import game.system.main.Game;
import game.system.main.GameObject;
import game.enums.ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;

public class Branch extends GameObject {

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

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, this.width, this.height);
    }

    public Item getItem() {
        return new Item_Stick(1, ITEM_ID.Stick);
    }

    public void interact() {
        Game.world.getInventorySystem().pickupItemToPlayerInv(this);
    }

    @Override
    public void destroyed() {

    }

    @Override
    public void hit(int damage) {

    }

}
