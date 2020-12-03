package game.assets.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.enums.ITEM_ID;
import game.assets.items.Item;
import game.assets.items.Item_Ground;
import game.assets.items.Item_Rock;
import game.system.main.Game;
import game.system.main.GameObject;
import game.enums.ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;

public class Pebble extends GameObject {

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

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, this.width, this.height);
    }

    public Item getItem() {
        return new Item_Rock(5, ITEM_ID.Rock);
    }

    public void interact() {
        Game.world.getHandler().addObject(new Item_Ground(x, y, 1, ID.Item, new Item_Rock(5, ITEM_ID.Rock)));
        Game.world.getHandler().findAndRemoveObject(this);
        //Game.inventorySystem.pickupItemToPlayerInv(this);
    }

    public void destroyed() {
        //Game.handler.addObject(1, new ItemGround(x, y, 1, ID.Item, new ItemRock(5, ITEM_ID.Rock)));
    }

    @Override
    public void hit(int damage) {

    }

}
