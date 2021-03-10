package game.system.systems.gameObject;

public interface Trigger {
    boolean canTrigger();
    void setTriggerActive(boolean triggerActive);
    boolean triggerCollision();
    void triggered();
}
