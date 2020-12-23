package game.system.systems.inventory.inventoryDef;

import game.assets.items.item.Item;
import game.system.systems.inventory.InventorySlot;

import java.util.LinkedList;

public interface AcceptsItems {
    void addItem(Item item);
    boolean addItemAtPos(Item item, int pos);

    boolean canAcceptItem(Item item);
    InventorySlotDef getNextFreeSlot();
    boolean hasFreeSlot();
    boolean inventoryContainsItemAndCanStack(Item item);
    InventorySlotDef getNextStackableSlot(Item item);

    void fillRandom(LinkedList<Item> items);
    void fill(LinkedList<Item> items);
}
