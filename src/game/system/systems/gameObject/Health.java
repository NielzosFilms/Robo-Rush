package game.system.systems.gameObject;

import game.assets.HealthBar;

public interface Health {
    int getHealth();
    HealthBar getHealthBar();
    boolean dead();
}
