package game.system.systems.inventory;

import game.assets.items.item.Item;
import game.system.main.Game;
import game.system.systems.inventory.inventoryDef.InventoryDef;
import game.system.systems.inventory.inventoryDef.InventorySlotDef;

import java.awt.*;

public class InventorySlot extends InventorySlotDef {
    public InventorySlot(InventoryDef inv, int x, int y) {
        super(inv, x, y);
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
        if(this.item != null) item.render(g, inv_x + x + margin, inv_y + y + margin);

        if(hover) {
            g.setColor(InventorySystem.slot_hover);
            g.fillRect(inv_x + x, inv_y + y, w, h);
        }
    }

    @Override
    public void leftClick(InventoryDef inv, InventorySystem invSys) {
        if(invSys.isHolding()) {
            if(this.hasItem()) {
                if(invSys.getHolding().getItemType() == this.item.getItemType() && this.item.isStackable()) {
                    Item rest = this.addItem(invSys.getHolding());
                    invSys.setHolding(rest);
                } else {
                    Item tmp = invSys.getHolding();
                    invSys.setHolding(this.getItem());
                    this.setItem(tmp);
                }
            } else {
                this.setItem(invSys.getHolding());
                invSys.clearHolding();
            }
        } else {
            invSys.setHolding(this.getItem());
            this.clearItem();
        }
    }

    @Override
    public void middleClick(InventoryDef inv, InventorySystem invSys) {

    }

    @Override
    public void rightClick(InventoryDef inv, InventorySystem invSys) {
        if(invSys.isHolding()) {
            if(this.hasItem()) {
                if(this.getItem().getItemType() == invSys.getHolding().getItemType()) {
                    if (this.getItem().getAmount() < InventorySystem.stackSize) {
                        this.getItem().setAmount(this.getItem().getAmount() + 1);
                        invSys.getHolding().setAmount(invSys.getHolding().getAmount() - 1);
                        if (invSys.getHolding().getAmount() <= 0) invSys.clearHolding();
                    }
                }
            } else {
                try {
                    Item tmp = (Item) invSys.getHolding().clone();
                    tmp.setAmount(1);
                    this.setItem(tmp);
                    invSys.getHolding().setAmount(invSys.getHolding().getAmount() - 1);
                    if(invSys.getHolding().getAmount() <= 0) invSys.clearHolding();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if(this.hasItem()) {
                try {
                    Item tmp = (Item) this.getItem().clone();
                    tmp.setAmount((int) Math.ceil(tmp.getAmount() / 2));
                    this.getItem().setAmount(this.getItem().getAmount() - tmp.getAmount());
                    if (this.getItem().getAmount() <= 0) this.clearItem();
                    if (tmp.getAmount() > 0) invSys.setHolding(tmp);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Item addItem(Item item) {
        if(this.hasItem()) {
            int sum = this.item.getAmount() + item.getAmount();
            int stack_diff = sum - InventorySystem.stackSize;
            if(stack_diff > 0) {
                this.item.setAmount(InventorySystem.stackSize);
                item.setAmount(stack_diff);
                return item;
            } else {
                this.item.setAmount(sum);
            }
        } else {
            if(item.getAmount() > InventorySystem.stackSize) {
                try {
                    Item tmp = (Item) item.clone();
                    tmp.setAmount(InventorySystem.stackSize);
                    item.setAmount(item.getAmount() - InventorySystem.stackSize);
                    this.setItem(tmp);
                    return item;
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            } else {
                this.setItem(item);
            }
        }
        return null;
    }
}
