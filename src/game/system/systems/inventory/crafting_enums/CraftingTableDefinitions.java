package game.system.systems.inventory.crafting_enums;

import game.assets.items.item.Item;
import game.assets.items.tools.iron.Tool_Iron_Axe;
import game.assets.items.tools.iron.Tool_Iron_Sword;
import game.assets.items.tools.wood.Tool_WoodenAxe;
import game.assets.items.tools.wood.Tool_WoodenSword;
import game.assets.objects.stick.Item_Stick;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public enum CraftingTableDefinitions {

    test_table(
            new Map[] {
                    new HashMap<Item, Item[]>() {{
                        put(new Tool_WoodenAxe(), new Item[]{new Item_Stick(2)});
                        put(new Tool_WoodenSword(), new Item[]{new Item_Stick(2)});
                    }},
                    new HashMap<Item, Item[]>() {{
                        put(new Tool_Iron_Axe(), new Item[]{new Item_Stick(4)});
                        put(new Tool_Iron_Sword(), new Item[]{new Item_Stick(4)});
                    }},
            }
    );

    private final Map<Item, Item[]>[] pages;

    CraftingTableDefinitions(Map<Item, Item[]>[] pages) {
        this.pages = pages;
    }

    public Map<Item, Item[]>[] getPages() {
        return pages;
    }
}
