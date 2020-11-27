package game.assets.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.enums.ITEM_ID;
import game.assets.items.Item;
import game.assets.items.ItemGround;
import game.assets.items.ItemRock;
import game.system.main.Game;
import game.system.main.GameObject;
import game.enums.ID;
import game.textures.Textures;

public class Pebble extends GameObject {

    private Random r = new Random();
    private BufferedImage tex;

    public Pebble(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);

        int rand = r.nextInt(1);
        if (rand == 1) {
            this.tex = Textures.tileSetNatureBlocks.get(48);
        } else {
            this.tex = Textures.tileSetNatureBlocks.get(49);
        }
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(tex, x, y, this.width, this.height, null);
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, this.width, this.height);
    }

    public Item getItem() {
        return new ItemRock(5, ITEM_ID.Rock);
    }

    public void interact() {
        Game.handler.addObject(new ItemGround(x, y, 1, ID.Item, new ItemRock(5, ITEM_ID.Rock)));
        Game.handler.findAndRemoveObject(this, Game.world);
        //Game.inventorySystem.pickupItemToPlayerInv(this);
    }

    public void destroyed() {
        //Game.handler.addObject(1, new ItemGround(x, y, 1, ID.Item, new ItemRock(5, ITEM_ID.Rock)));
    }

    @Override
    public void hit(int damage) {

    }

}
