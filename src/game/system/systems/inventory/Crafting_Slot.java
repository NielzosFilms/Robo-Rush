package game.system.systems.inventory;

import game.assets.items.item.Item;
import game.system.main.Game;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Crafting_Slot {
    private Item return_item;
    private Item[] required_items;
    private Crafting_Inventory inv;
    private int x, y;
    private int w = InventorySystem.slot_w, h = InventorySystem.slot_h;
    private boolean hover = false;

    private Texture background = new Texture(TEXTURE_LIST.gui_list, 2, 1);

    public Crafting_Slot(Crafting_Inventory inv, int x, int y) {
        this.inv = inv;
        this.x = x;
        this.y = y;
    }

    public void tick() {
        this.hover = Game.mouseInput.mouseOverLocalRect(this.getBounds());
    }

    public void render(Graphics g) {
        int inv_x = inv.getX();
        int inv_y = inv.getY();
        g.drawImage(background.getTexure(), inv_x + x, inv_y + y, InventorySystem.item_w, InventorySystem.item_w, null);
        //g.drawImage(background_border.getTexure(), inv_x + x, inv_y + y, InventorySystem.item_w, InventorySystem.item_w, null);

        int margin = (int)Math.floor((InventorySystem.slot_w - InventorySystem.item_w) / 2);
        if(this.return_item != null) return_item.render(g, inv_x + x + margin, inv_y + y + margin);

        if(hover) {
            g.setColor(InventorySystem.slot_hover);
            g.fillRect(inv_x + x, inv_y + y, w, h);
        }
    }

    public void onClick(MouseEvent e, Crafting_Inventory inv, InventorySystem invSys) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> leftClick(inv, invSys);
            case MouseEvent.BUTTON2 -> middleClick(inv, invSys);
            case MouseEvent.BUTTON3 -> rightClick(inv, invSys);
        }
    }

    private void leftClick(Crafting_Inventory inv, InventorySystem invSys) {

    }
    private void middleClick(Crafting_Inventory inv, InventorySystem invSys) {

    }
    private void rightClick(Crafting_Inventory inv, InventorySystem invSys) {

    }

    public void setReturnItem(Item item) {
        this.return_item = item;
    }
    public Item getReturnItem() {
        return this.return_item;
    }

    public boolean hasItem() { return this.return_item != null; }

    public Rectangle getBounds() {
        return new Rectangle(inv.getX() + x, inv.getY() + y, w, h);
    }
}
