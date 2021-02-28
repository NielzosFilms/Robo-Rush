package game.system.systems.hitbox;

import game.system.main.Game;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.LinkedList;

public class HitboxContainer {
    private LinkedList<Hitbox> hitboxes = new LinkedList<>();
    private LinkedList<GameObject> objects_hit = new LinkedList<>();
    private GameObject created_by;
    private int hit_count = 0;

    public HitboxContainer(Hitbox[] hitboxes, GameObject created_by) {
        objects_hit.add(created_by);
        this.created_by = created_by;
        for(Hitbox hitbox : hitboxes) {
            hitbox.setParent(this);
            this.hitboxes.add(hitbox);
        }
    }

    public void tick() {
        for(int i=0; i<hitboxes.size(); i++) {
            hitboxes.get(i).tick();
        }
        if(hitboxes.size() == 0) {
            Game.gameController.getHitboxSystem().removeHitboxContainer(this);
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

    public GameObject getCreated_by() {
        return this.created_by;
    }

    public void addHit_count(int amount) {
        hit_count += amount;
    }

    public int getHit_count() { return hit_count; }
}
