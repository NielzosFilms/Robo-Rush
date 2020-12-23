package game.system.systems.inventory;

import game.assets.items.Item_Ground;
import game.assets.items.item.Item;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.systems.inventory.inventoryDef.AcceptsItems;
import game.system.systems.inventory.inventoryDef.InventoryDef;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Inventory extends InventoryDef implements AcceptsItems {
    private ArrayList<InventorySlot> slots = new ArrayList<>();

    public Inventory(int size_x, int size_y) {
        this.size_x = size_x;
        this.size_y = size_y;

        for(int y = 0; y < size_y; y++) {
            for(int x = 0; x < size_x; x++) {
                // translate x, y to screen coords
                slots.add(new InventorySlot(this, x * slot_w, y * slot_h));
            }
        }
    }

    @Override
    public void tick() {
        for(InventorySlot slot : slots) {
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
        for(InventorySlot slot : slots) {
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
        InventorySlot slot = getClickedSlot(e, mouseInput);
        if (slot != null) {
            slot.onClick(e,this,  invSys);
        }
    }

    @Override
    protected InventorySlot getClickedSlot(MouseEvent e, MouseInput mouseInput) {
        for(InventorySlot slot : slots) {
            if(mouseInput.mouseOverLocalRect(slot.getBounds())) {
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
    public ArrayList<InventorySlot> getSlots() {
        return slots;
    }

    @Override
    public void addItem(Item item) {
        if(inventoryContainsItemAndCanStack(item)) {
            InventorySlot slot = getNextStackableSlot(item);
            if(slot != null) {
                Item rest = slot.addItem(item, this);
                if (rest != null) {
                    this.addItem(rest);
                }
            }
        } else {
            if(hasFreeSlot()) {
                InventorySlot slot = getNextFreeSlot();
                if(slot != null) {
                    Item rest = slot.addItem(item, this);
                    if(rest != null) this.addItem(rest);
                }
            } else {
                Item_Ground item_gnd = item.getItemGround();
                Point new_coords = new Point(Game.world.getPlayer().getX(), Game.world.getPlayer().getY());
                item_gnd.setX(new_coords.x);
                item_gnd.setY(new_coords.y);
                Game.world.getInventorySystem().dropItem(item_gnd);
            }
        }
    }

    @Override
    public boolean addItemAtPos(Item item, int pos) {
        InventorySlot slot = slots.get(pos);
        if(!slot.hasItem()) {
            slot.setItem(item);
            return true;
        }
        return false;
    }

    @Override
    public boolean canAcceptItem(Item item) {
        if(inventoryContainsItemAndCanStack(item)) {
            return true;
        } else {
            return hasFreeSlot();
        }
    }

    @Override
    public InventorySlot getNextFreeSlot() {
        for (InventorySlot slot : slots) {
            if (!slot.hasItem()) {
                return slot;
            }
        }
        return null;
    }

    @Override
    public boolean hasFreeSlot() {
        for(InventorySlot slot : slots) {
            if(!slot.hasItem()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean inventoryContainsItemAndCanStack(Item item) {
        for(InventorySlot slot : slots) {
            if(!slot.hasItem()) continue;
            Item slotItem = slot.getItem();
            if(slotItem.getItemType() == item.getItemType()) {
                if(slotItem.getAmount() < InventorySystem.stackSize) return true;
            }
        }
        return false;
    }

    @Override
    public InventorySlot getNextStackableSlot(Item item) {
        for(InventorySlot slot : slots) {
            if(!slot.hasItem()) continue;
            Item slotItem = slot.getItem();
            if(slotItem.getItemType() == item.getItemType()) {
                if(slotItem.getAmount() < InventorySystem.stackSize) return slot;
            }
        }
        return null;
    }

    @Override
    public void fillRandom(LinkedList<Item> items) {
        for(Item item : items) {
            boolean item_placed = false;
            while(!item_placed) {
                if(!this.hasFreeSlot()) break;
                item_placed = this.addItemAtPos(item, new Random().nextInt(size_x * size_y));
            }
        }
    }

    @Override
    public void fill(LinkedList<Item> items) {
        for(Item item : items) {
            this.addItem(item);
        }
    }
}
