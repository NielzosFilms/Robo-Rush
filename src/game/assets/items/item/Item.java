package game.assets.items.item;

import game.assets.items.Item_Ground;
import game.enums.ITEM_ID;
import game.textures.ImageFilters;
import game.textures.Texture;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class Item implements Cloneable, Serializable {
    protected int amount;
    protected ITEM_ID itemType;
    protected Texture tex;
    protected Item_Ground itemGround;
    protected int damage = 0;
    protected boolean stackable = true;

    public Item(int amount, ITEM_ID itemType) {
        this.amount = amount;
        this.itemType = itemType;
        this.itemGround = null;
    }

    public void render(Graphics g, int x, int y) {
        Font font = new Font("SansSerif", Font.PLAIN, 3);
        g.setFont(font);
        g.setColor(Color.WHITE);

        g.drawImage(this.tex.getTexure(), x, y, 16, 16, null);
        //ImageFilters.renderImageWithRotationFromCenter(g, tex.getTexure(), x, y, 16, 16, -45);

        if(stackable) g.drawString("" + this.amount, x, y + 16);

        g.drawString(this.itemType.toString(), x, y + 2);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ITEM_ID getItemType() {
        return this.itemType;
    }

    public void setItemType(ITEM_ID itemType) {
        this.itemType = itemType;
    }

    public Texture getTexture() {
        return this.tex;
    }

    public void setTexture(Texture tex) {
        this.tex = tex;
    }

    public Item_Ground getItemGround() {
        return new Item_Ground(itemGround.getX(), itemGround.getY(), itemGround.getZIndex(), itemGround.getId(), this);
    }

    public void setItemGround(Item_Ground itemGround) {
        this.itemGround = itemGround;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }
}
