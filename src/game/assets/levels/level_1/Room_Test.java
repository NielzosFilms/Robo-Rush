package game.assets.levels.level_1;

import game.assets.levels.def.Room;
import game.assets.objects.tree.Tree;
import game.enums.BIOME;
import game.enums.ID;
import game.system.helpers.Logger;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.LinkedList;

public class Room_Test extends Room {

    public Room_Test() {
        addObject(new Tree(0, 0, 5, ID.Tree, BIOME.Forest));
        addObject(new Tree(0, -16, 5, ID.Tree, BIOME.Forest));
    }

    @Override
    public void tick() {
        for(LinkedList<GameObject> list : objects) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).tick();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        for(LinkedList<GameObject> list : objects) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).render(g);
            }
        }
    }
}
