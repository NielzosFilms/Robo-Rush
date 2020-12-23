package game.system.systems.inventory;

import game.assets.items.item.Item;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.systems.inventory.crafting_enums.CraftingTableDefinitions;
import game.system.systems.inventory.inventoryDef.InventoryDef;
import game.system.systems.inventory.inventoryDef.InventorySlotDef;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class Crafting_Inventory extends InventoryDef {
    private CraftingTableDefinitions tableDef;
    private LinkedList<LinkedList<InventorySlotDef>> pages = new LinkedList<>();
    private boolean moveable = true;
    private int active_page = 0;

    private Texture
            top_left = new Texture(TEXTURE_LIST.gui_list, 0, 0),
            top = new Texture(TEXTURE_LIST.gui_list, 1, 0),
            top_right = new Texture(TEXTURE_LIST.gui_list, 3, 0),
            right = new Texture(TEXTURE_LIST.gui_list, 3, 1),
            bot_right = new Texture(TEXTURE_LIST.gui_list, 3, 2),
            bot = new Texture(TEXTURE_LIST.gui_list, 1, 2),
            bot_left = new Texture(TEXTURE_LIST.gui_list, 0, 2),
            left = new Texture(TEXTURE_LIST.gui_list, 0, 1);

    public Crafting_Inventory(int size_x, int size_y, CraftingTableDefinitions tableDef) {
        this.tableDef = tableDef;
        this.size_x = size_x;
        this.size_y = size_y;
        for(Map<Item, Item[]> page : tableDef.getPages()) {
            LinkedList<InventorySlotDef> page_slots = new LinkedList<>();
            //slots.add(new InventorySlot(this, x * slot_w, y * slot_h));
            for(int y = 0; y < size_y; y++) {
                for(int x = 0; x < size_x; x++) {
                    // translate x, y to screen coords
                    page_slots.add(new Crafting_Slot(this, x * slot_w, y * slot_h));
                }
            }
            int i = 0;
            for(Item key : page.keySet()) {
                ((Crafting_Slot)page_slots.get(i)).setItems(key, page.get(key));
                i++;
            }
            pages.add(page_slots);
        }
    }

    @Override
    public void tick() {
        for(InventorySlotDef slot : pages.get(active_page)) {
            slot.tick();
        }
    }

    @Override
    public void render(Graphics g) {
        for(int y=-1; y<size_y+1; y++) {
            for(int x=-1; x<size_x+1; x++) {
                if(y == -1) {
                    if(x == -1) {
                        g.drawImage(top_left.getTexure(), this.x + x * slot_w, this.y + y * slot_h, slot_w, slot_h, null);
                    } else if(x == size_x) {
                        g.drawImage(top_right.getTexure(), this.x + x * slot_w, this.y + y * slot_h, slot_w, slot_h, null);
                    } else {
                        g.drawImage(top.getTexure(), this.x + x * slot_w, this.y + y * slot_h, slot_w, slot_h, null);
                    }
                } else if(y == size_y) {
                    if(x == -1) {
                        g.drawImage(bot_left.getTexure(), this.x + x * slot_w, this.y + y * slot_h, slot_w, slot_h, null);
                    } else if(x == size_x) {
                        g.drawImage(bot_right.getTexure(), this.x + x * slot_w, this.y + y * slot_h, slot_w, slot_h, null);
                    } else {
                        g.drawImage(bot.getTexure(), this.x + x * slot_w, this.y + y * slot_h, slot_w, slot_h, null);
                    }
                } else {
                    if(x == -1) {
                        g.drawImage(left.getTexure(), this.x + x * slot_w, this.y + y * slot_h, slot_w, slot_h, null);
                    } else if(x == size_x) {
                        g.drawImage(right.getTexure(), this.x + x * slot_w, this.y + y * slot_h, slot_w, slot_h, null);
                    }
                }

            }
        }
        for(InventorySlotDef slot : pages.get(active_page)) {
            slot.render(g);
        }
        if(Game.DEBUG_MODE) {
            g.setColor(Color.magenta);
            g.drawRect(getInventoryBounds().x, getInventoryBounds().y, getInventoryBounds().width, getInventoryBounds().height);
            if(moveable) {
                g.setColor(Color.green);
                g.drawRect(getInventoryMoveBounds().x, getInventoryMoveBounds().y, getInventoryMoveBounds().width, getInventoryMoveBounds().height);
            }
        }
    }

    @Override
    public void mouseClick(MouseEvent e, MouseInput mouseInput, InventorySystem invSys) {
        InventorySlotDef slot = getClickedSlot(e, mouseInput);
        if (slot != null) {
            slot.onClick(e,this,  invSys);
        }
    }

    @Override
    protected InventorySlotDef getClickedSlot(MouseEvent e, MouseInput mouseInput) {
        for (InventorySlotDef slot : pages.get(active_page)) {
            if (mouseInput.mouseOverLocalRect(slot.getBounds())) {
                return slot;
            }
        }
        return null;
    }

    @Override
    public Rectangle getInventoryBounds() {
        int width = size_x * slot_w;
        int height = size_y * slot_h;
        if(moveable) {
            return new Rectangle(x, y - 12, width, height + 12);
        }
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle getInventoryMoveBounds() {
        if(moveable) {
            int width = size_x * slot_w;
            return new Rectangle(x, y - 12, width, 12);
        } else return null;
    }

    @Override
    public ArrayList<InventorySlotDef> getSlots() {
        return null;
    }
}
