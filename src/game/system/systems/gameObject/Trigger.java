package game.system.systems.gameObject;

import game.assets.entities.player.Player;

public interface Trigger {
    boolean canTrigger();
    void setTriggerActive(boolean triggerActive);
    boolean triggerCollision();
    void triggered(Player player);
}
