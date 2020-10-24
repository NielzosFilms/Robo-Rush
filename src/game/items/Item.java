package game.items;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;

import game.main.ID;

public abstract class Item implements Cloneable {
    protected int amount;
    protected ITEM_ID itemType;
    protected BufferedImage tex;
    protected ItemGround itemGround;

    public Item(int amount, ITEM_ID itemType, BufferedImage tex) {
        this.amount = amount;
        this.itemType = itemType;
        this.tex = tex;
        this.itemGround = null;
    }

    public void render(Graphics g, int x, int y) {
        Font font = new Font("SansSerif", Font.PLAIN, 3);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawImage(this.tex, x, y, 16, 16, null);
        g.drawString("" + this.amount, x, y + 16);

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

    public BufferedImage getTexture() {
        return this.tex;
    }

    public void setTexture(BufferedImage tex) {
        this.tex = tex;
    }

    public ItemGround getItemGround() {
        return this.itemGround;
    }

    public void setItemGround(ItemGround itemGround) {
        this.itemGround = itemGround;
    }

}
