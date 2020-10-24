package game.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.items.ITEM_ID;
import game.items.Item;
import game.items.ItemRock;
import game.main.GameObject;
import game.main.ID;
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
        g.drawImage(tex, x, y, 16, 16, null);
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, 16, 16);
    }

    public Item getItem() {
        return new ItemRock(1, ITEM_ID.Rock, Textures.tileSetNatureBlocks.get(49));
    }

}
