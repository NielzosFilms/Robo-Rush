package game.system.systems.inventory.inventoryDef;

import game.assets.items.item.Item;
import game.system.systems.inventory.Inventory;
import game.system.systems.inventory.InventorySystem;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class InventorySlotDef {
    protected Item item;
    protected InventoryDef inv;
    protected int x, y;
    protected int w = InventorySystem.slot_w, h = InventorySystem.slot_h;
    protected boolean hover = false;

    protected Texture background = new Texture(TEXTURE_LIST.gui_list, 1, 1);
    protected Texture background_border = new Texture(TEXTURE_LIST.gui_list, 0, 3);

    public InventorySlotDef(InventoryDef inv, int x, int y) {
        this.inv = inv;
        this.x = x;
        this.y = y;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public void onClick(MouseEvent e, InventoryDef inv, InventorySystem invSys) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                leftClick(inv, invSys);
                break;
            case MouseEvent.BUTTON2:
                middleClick(inv, invSys);
                break;
            case MouseEvent.BUTTON3:
                rightClick(inv, invSys);
                break;
        }
    }

    public abstract void leftClick(InventoryDef inv, InventorySystem invSys);
    public abstract void middleClick(InventoryDef inv, InventorySystem invSys);
    public abstract void rightClick(InventoryDef inv, InventorySystem invSys);

    public abstract Item addItem(Item item);

    public void setItem(Item item) {
        this.item = item;
    }
    public Item getItem() {
        return this.item;
    }
    public void clearItem() {
        this.item = null;
    }
    public boolean hasItem() { return this.item != null; }

    public Rectangle getBounds() {
        return new Rectangle(inv.getX() + x, inv.getY() + y, w, h);
    }
}
