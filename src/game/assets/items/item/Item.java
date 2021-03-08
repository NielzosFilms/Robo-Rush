package game.assets.items.item;

import game.assets.entities.player.PLAYER_STAT;
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
import java.util.HashMap;
import java.util.Map;

public class Item {
    private Texture tex;
    private ITEM_ID item_id;

    public HashMap<PLAYER_STAT, Float> item_stats;

    public Item(Texture tex, ITEM_ID item_id, HashMap<PLAYER_STAT, Float> item_stats) {
        this.tex = tex;
        this.item_id = item_id;
        this.item_stats = item_stats;
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
