package game.assets.entities.enemies;

import game.assets.entities.orbs.EnergyOrb;
import game.assets.entities.orbs.HealthOrb;
import game.assets.entities.orbs.Orb;
import game.enums.ID;
import game.system.systems.gameObject.GameObject;

import java.util.LinkedList;
import java.util.Random;

public class EnemyDrops {
    public static LinkedList<Orb> getSimpleDrops(int x, int y, int zIndex, ID id) {
        Random r = new Random();
        LinkedList<Orb> ret = new LinkedList<>();

        if(r.nextBoolean()) {
            if(r.nextFloat() < 0.25f) {
                ret.add(getSimpleDrop(x, y, zIndex, id));
                ret.add(getSimpleDrop(x, y, zIndex, id));
            } else {
                ret.add(getSimpleDrop(x, y, zIndex, id));
            }
        }
        return ret;
    }

    private static Orb getSimpleDrop(int x, int y, int zIndex, ID id) {
        Random r = new Random();
        if(r.nextBoolean()) {
            return new EnergyOrb(x, y, zIndex, id);
        } else {
            return new HealthOrb(x, y, zIndex, id);
        }
    }
}
