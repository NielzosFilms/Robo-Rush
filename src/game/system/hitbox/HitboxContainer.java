package game.system.hitbox;

import game.system.main.Game;
import game.system.main.GameObject;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

public class HitboxContainer {
    private LinkedList<Hitbox> hitboxes = new LinkedList<>();
    private LinkedList<GameObject> objects_hit = new LinkedList<>();

    public HitboxContainer(Hitbox[] hitboxes, GameObject created_by) {
        for(Hitbox hitbox : hitboxes) {
            hitbox.setParent(this);
            this.hitboxes.add(hitbox);
        }
        objects_hit.add(created_by);
    }

    public void tick() {
        for(int i=0; i<hitboxes.size(); i++) {
            hitboxes.get(i).tick();
        }
        if(hitboxes.size() == 0) {
            Game.world.getHitboxSystem().removeHitboxContainer(this);
        }
    }

    public void render(Graphics g) {
        for(int i=0; i<hitboxes.size(); i++) {
            if(hitboxes.get(i).active()) hitboxes.get(i).render(g);
        }
    }

    public void addHitObject(GameObject obj) {
        this.objects_hit.add(obj);
    }

    public boolean canHitObject(GameObject obj) {
        return !objects_hit.contains(obj);
    }

    public void removeHitbox(Hitbox hitbox) {
        hitboxes.remove(hitbox);
    }

    public LinkedList<Hitbox> getHitboxes() {
        return hitboxes;
    }
}
