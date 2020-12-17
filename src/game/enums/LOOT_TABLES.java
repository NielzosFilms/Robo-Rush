package game.enums;

import game.assets.items.item.Item;
import game.assets.items.tools.wood.Tool_WoodenAxe;
import game.assets.objects.rock.Item_Rock;
import game.assets.objects.stick.Item_Stick;

import java.util.LinkedList;
import java.util.Random;

public enum LOOT_TABLES {
    sticks_rocks {
        public LinkedList<Item> getGeneratedItems() {
            Random rand = new Random();
            LinkedList<Item> items = new LinkedList<>();
            for(int i=0; i<rand.nextInt(3) + 1; i++) {
                if(rand.nextInt(100) < 1) {
                    items.add(new Tool_WoodenAxe());
                } else {
                    if(rand.nextInt(2) == 1) {
                        items.add(new Item_Stick(rand.nextInt(5) + 1));
                    } else {
                        items.add(new Item_Rock(rand.nextInt(5) + 1));
                    }
                }
            }
            return items;
        }
    };

    public abstract LinkedList<Item> getGeneratedItems();
}
