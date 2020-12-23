package game.system.systems.inventory;

import game.assets.items.item.Item;
import game.system.inputs.MouseInput;
import game.system.systems.inventory.crafting_enums.CraftingTableDefinitions;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class Crafting_Inventory {
    private final int slot_w = InventorySystem.slot_w, slot_h = InventorySystem.slot_h;
    private int x = 100, y = 100;
    private int init_x = 100, init_y = 100;
    private int size_x, size_y;
    private CraftingTableDefinitions tableDef;
    private LinkedList<LinkedList<Crafting_Slot>> pages = new LinkedList<>();
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

    public Crafting_Inventory(CraftingTableDefinitions tableDef) {
        this.tableDef = tableDef;
        for(Map<Item, Item[]> page : tableDef.getPages()) {
            LinkedList<InventorySlot> page_slots = new LinkedList<>();
            for(Item item_key : page.keySet()) {
            }
        }
    }

    public void tick() {

    }

    public void render() {

    }

    public void mouseClick(MouseEvent e, MouseInput mouseInput, InventorySystem invSys) {
        Crafting_Slot slot = getClickedSlot(e, mouseInput);
        if (slot != null) {
            slot.onClick(e,this,  invSys);
        }
    }

    private Crafting_Slot getClickedSlot(MouseEvent e, MouseInput mouseInput) {
        for(Crafting_Slot slot : pages.get(active_page)) {
            if(mouseInput.mouseOverLocalRect(slot.getBounds())) {
                return slot;
            }
        }
        return null;
    }

    public Rectangle getInventoryBounds() {
        int width = size_x * slot_w;
        int height = size_y * slot_h;
        if(moveable) {
            return new Rectangle(x, y - 12, width, height + 12);
        }
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getInventoryMoveBounds() {
        if(moveable) {
            int width = size_x * slot_w;
            return new Rectangle(x, y - 12, width, 12);
        } else return null;
    }

    public void setInitXY(int x, int y) {
        this.init_x = x;
        this.init_y = y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
