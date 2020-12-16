package game.assets.objects.puzzle_objects;

import game.system.systems.GameObject;

public interface PuzzleObject {
    void setConnectedObject(GameObject object);
    GameObject getConnectedObject();

    int getConnectedObject_id();
    int getConnection_id();

    boolean hasConnection();
}
