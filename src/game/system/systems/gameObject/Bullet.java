package game.system.systems.gameObject;

import java.awt.*;
import java.util.LinkedList;

public interface Bullet {
    Rectangle getBounds();
    int getDamage();

    void addHitObject(GameObject object);
    LinkedList<GameObject> getHitObjects();

    GameObject getCreatedBy();
    void destroy();
}
