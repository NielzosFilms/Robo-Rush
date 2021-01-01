package game.system.systems.hitbox;

import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.Hitable;
import game.system.systems.particles.Particle_DamageNumber;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.system.main.Handler;

import java.awt.*;
import java.util.LinkedList;

public class HitboxSystem {
    private Handler handler;

    private LinkedList<HitboxContainer> hitboxContainers = new LinkedList<>();

    public HitboxSystem() {}

    public void setRequirements(Handler handler) {
        this.handler = handler;
    }

    public void tick() {
        for(int i=0; i<hitboxContainers.size(); i++) {
            hitboxContainers.get(i).tick();
        }
        LinkedList<GameObject> objects_w_bounds = handler.getBoundsObjects();
        for(GameObject object : objects_w_bounds) {
            for(int i=0; i<hitboxContainers.size(); i++) {
                if(hitboxContainers.get(i).canHitObject(object)) {
                    for(int j=0; j<hitboxContainers.get(i).getHitboxes().size(); j++) {
                        Hitbox hitbox = hitboxContainers.get(i).getHitboxes().get(j);
                        if(hitbox.active()) {
                            if(object instanceof Hitable && object instanceof Bounds) {
                                if(((Bounds) object).getBounds().intersects(hitbox.getBounds())) {
                                    ((Hitable) object).hit(hitboxContainers.get(i), j);
                                    Game.world.getPs().addParticle(new Particle_DamageNumber(object.getX(), object.getY(), 0f, -0.3f, 40, hitbox.getDamage()));
                                    hitboxContainers.get(i).addHitObject(object);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        for(int i=0; i<hitboxContainers.size(); i++) {
            hitboxContainers.get(i).render(g);
        }
    }

    public void addHitboxContainer(HitboxContainer hitboxContainer) {
        this.hitboxContainers.add(hitboxContainer);
    }

    public void removeHitboxContainer(HitboxContainer hitboxContainer) {
        this.hitboxContainers.remove(hitboxContainer);
    }
}
