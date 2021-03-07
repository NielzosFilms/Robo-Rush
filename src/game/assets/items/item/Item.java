package game.assets.items.item;

import game.assets.items.Item_Ground;
import game.enums.ITEM_ID;
import game.system.systems.gameObject.GameObject;
import game.textures.ImageFilters;
import game.textures.Texture;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Item {
    private Texture tex;
    private ITEM_ID item_id;

    public int bullet_spread, rate_of_fire;

    public Item(Texture tex, ITEM_ID item_id, int bullet_spread, int rate_of_fire) {
        this.tex = tex;
        this.item_id = item_id;
        this.bullet_spread = bullet_spread;
        this.rate_of_fire = rate_of_fire;
    }

    public void drawItemForInventory(Graphics g, int x, int y) {
        g.drawImage(this.tex.getTexure(), x, y, null);
    }

    public void drawItemForGame(Graphics g, int x, int y) {
        g.drawImage(this.tex.getTexure(), x, y, null);
    }

    public Texture getTexture() {
        return this.tex;
    }

    public void setTexture(Texture tex) {
        this.tex = tex;
    }

    public ITEM_ID getItem_id() {
        return item_id;
    }

    public void setItem_id(ITEM_ID item_id) {
        this.item_id = item_id;
    }
}
