package game.assets;

import game.system.main.Game;
import game.system.helpers.Helpers;
import game.textures.Textures;

import java.awt.*;
import java.io.Serializable;

public class HealthBar implements Serializable {
    private static final Color background = new Color(0, 0, 0, 127);
    private static final int HIDE_DELAY = 60*5;
    private int hide_timer = HIDE_DELAY;
    private Color healthBar_color = new Color(205,0,0);
    private int min = 0, max = 100;
    private int w = 24, h = 4;
    private int health;
    private int x, y;

    public HealthBar(int x, int y, int min, int max) {
        this.x = x;
        this.y = y;
        this.min = min;
        this.max = max;
        this.health = max;
        Game.world.getHud().addHealthBar(this);
    }

    public void tick() {
        if(hide_timer > 0) hide_timer--;
    }

    public void render(Graphics g) {
        if(health != max && hide_timer != 0) {
            g.drawImage(Textures.healthbar, x, y, 24, 4, null);
            int health_perc = getHealthPercent();
            int until = (int)((float)(Textures.healthbar_list.size()) / 100 * health_perc);
            for(int i=0; i<until; i++) {
                g.drawImage(Textures.healthbar_list.get(new Point(i, 0)), x + i + 1, y + 1, 1, 2, null);
            }
        }
    }

    public boolean dead() {
        return health <= min;
    }

    public void subtractHealth(int amount) {
        hide_timer = HIDE_DELAY;
        health = health - amount;
    }

    public void addHealth(int amount) {
        hide_timer = HIDE_DELAY;
        health = Helpers.clampInt(health + amount, min, max);
    }

    public int getHealthPercent() {
        return (int)(100 / max * health);
    }

    private int getDrawWidth() {
        return (int)(w / max * health);
    }

    public boolean showing() {
        return hide_timer > 0;
    }
}
