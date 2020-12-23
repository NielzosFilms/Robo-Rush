package game.system.systems.inventory;

import game.assets.items.item.Item;
import game.system.main.Game;
import game.system.systems.inventory.inventoryDef.AcceptsItems;
import game.system.systems.inventory.inventoryDef.InventoryDef;
import game.system.systems.inventory.inventoryDef.InventorySlotDef;
import game.textures.Fonts;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Crafting_Slot extends InventorySlotDef {
    private Item return_item;
    private Item[] required_items;

    public Crafting_Slot(InventoryDef inv, int x, int y) {
        super(inv, x, y);
    }

    public void setItems(Item return_item, Item[] required_items) {
        this.return_item = return_item;
        this.required_items = required_items;
    }

    @Override
    public void tick() {
        this.hover = Game.mouseInput.mouseOverLocalRect(this.getBounds());
    }

    @Override
    public void render(Graphics g) {
        int inv_x = inv.getX();
        int inv_y = inv.getY();
        g.drawImage(background.getTexure(), inv_x + x, inv_y + y, InventorySystem.slot_w, InventorySystem.slot_h, null);
        g.drawImage(background_border.getTexure(), inv_x + x, inv_y + y, InventorySystem.slot_w, InventorySystem.slot_h, null);

        int margin = (int)Math.floor((InventorySystem.slot_w - InventorySystem.item_w) / 2);
        if(this.return_item != null) return_item.render(g, inv_x + x + margin, inv_y + y + margin);

        if(hover) {
            g.setColor(InventorySystem.slot_hover);
            g.fillRect(inv_x + x, inv_y + y, w, h);
            g.setFont(Fonts.default_fonts.get(5));
            int y_offset = 10;
            if(required_items != null) {
                for(Item req_item : required_items) {
                    if(canCraftItem()) {
                        g.setColor(Color.green);
                    } else {
                        g.setColor(Color.red);
                    }
                    g.drawString(req_item.toString(), x + inv_x, y+y_offset + inv_y);
                    y_offset+=5;
                }
            }
        }
    }

    @Override
    public void leftClick(InventoryDef inv, InventorySystem invSys) {
        if(canCraftItem()) {
            if(!invSys.isHolding()) {
                invSys.setHolding(return_item);
                InventoryDef player_inv = Game.world.getPlayer().getInventory();
                for(Item req_item : required_items) {
                    boolean result = ((AcceptsItems)player_inv).subtractItem(req_item);
                    if(!result) invSys.setHolding(null);
                }
            }
        }
    }

    @Override
    public void middleClick(InventoryDef inv, InventorySystem invSys) {

    }

    @Override
    public void rightClick(InventoryDef inv, InventorySystem invSys) {

    }

    @Override
    public Item addItem(Item item) {
        return null;
    }

    private boolean canCraftItem() {
        InventoryDef player_inv = Game.world.getPlayer().getInventory();
        int matches = 0;
        for(Item req_item : required_items) {
            if(((AcceptsItems)player_inv).hasItemWithAmount(req_item)) {
                matches++;
            }
        }
        return matches >= required_items.length;
    }
}
