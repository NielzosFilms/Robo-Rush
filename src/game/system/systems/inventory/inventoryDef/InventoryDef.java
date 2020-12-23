package game.system.systems.inventory.inventoryDef;

import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.systems.inventory.InventorySlot;
import game.system.systems.inventory.InventorySystem;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class InventoryDef {
    protected final int slot_w = InventorySystem.slot_w, slot_h = InventorySystem.slot_h;
    protected int x = 100, y = 100;
    protected int init_x = x, init_y = y;
    protected int size_x, size_y;
    protected boolean moveable = true;

    protected Texture
            top_left = new Texture(TEXTURE_LIST.gui_list, 0, 0),
            top = new Texture(TEXTURE_LIST.gui_list, 1, 0),
            top_right = new Texture(TEXTURE_LIST.gui_list, 2, 0),
            right = new Texture(TEXTURE_LIST.gui_list, 2, 1),
            bot_right = new Texture(TEXTURE_LIST.gui_list, 2, 2),
            bot = new Texture(TEXTURE_LIST.gui_list, 1, 2),
            bot_left = new Texture(TEXTURE_LIST.gui_list, 0, 2),
            left = new Texture(TEXTURE_LIST.gui_list, 0, 1);

    public InventoryDef() {}

    public abstract void tick();
    public abstract void render(Graphics g);

    public abstract void mouseClick(MouseEvent e, MouseInput mouseInput, InventorySystem invSys);
    protected abstract InventorySlotDef getClickedSlot(MouseEvent e, MouseInput mouseInput);

    public abstract Rectangle getInventoryBounds();
    public abstract Rectangle getInventoryMoveBounds();

    public abstract ArrayList<InventorySlotDef> getSlots();

    public void setInitXY(int x, int y) {
        this.init_x = x;
        this.init_y = y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getSizeX() {
        return this.size_x;
    }

    public int getSizeY() {
        return this.size_y;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void open() {
        this.setXY(init_x, init_y);
        Game.world.getInventorySystem().addOpenInventory(this);
    }

    public void close() {
        Game.world.getInventorySystem().removeOpenInventory(this);
    }
}
