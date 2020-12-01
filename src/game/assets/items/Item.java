package game.assets.items;

import game.enums.ITEM_ID;
import game.textures.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class Item implements Cloneable, Serializable {
    protected int amount;
    protected ITEM_ID itemType;
    protected Texture tex;
    protected Item_Ground itemGround;
    protected int durability = 100;
    protected int damage = 0;
    protected int attack_speed = 0;
    protected boolean placeable = false;
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

    public boolean isPlaceable() {
        return placeable;
    }

    public void setPlaceable(boolean placeable) {
        this.placeable = placeable;
    }

    public boolean placeable() {
        return this.placeable;
    }

    public abstract boolean place(int x, int y);

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public void subDurability(int durability) {
        this.durability -= durability;
    }

    public void addDurability(int durability) {
        this.durability += durability;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public int getAttack_speed() {
        return attack_speed;
    }

    public void setAttack_speed(int attack_speed) {
        this.attack_speed = attack_speed;
    }
}
