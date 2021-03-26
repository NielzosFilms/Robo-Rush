package game.system.systems.gameObject;

import java.util.LinkedList;

public interface HUD_Rendering {
    /**
     * @return LinkedList of GameObjects that implement HUD_Component
     */
    LinkedList<GameObject> getHudObjects();
}
