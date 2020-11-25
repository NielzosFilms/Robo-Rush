package game.assets;

import game.system.main.Game;
import game.system.main.Helpers;

import java.awt.*;

public class HealthBar {
    private static final Color background = new Color(0, 0, 0, 127);
    private Color healthBar_color = new Color(205,0,0);
    private int min = 0, max = 100;
    private int w = 32, h = 4;
    private int health;
    private int x, y;

    public HealthBar(int x, int y, int min, int max) {
        this.x = x;
        this.y = y;
        this.min = min;
        this.max = max;
        this.health = max;
        Game.hud.addHealthBar(this);
    }

    public void tick() {}

    public void render(Graphics g) {
        if(health != max) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(0.5f));
            g.setColor(background);
            g.fillRect(x, y, w, h);
            g.setColor(healthBar_color);
            g.fillRect(x, y, getDrawWidth(), h);
            g.setColor(Color.black);
            g.drawRect(x, y, w, h);
            g2d.setStroke(new BasicStroke(1));
        }
    }

    public boolean dead() {
        return health <= 0;
    }

    public void subtractHealth(int amount) {
        health = health - amount;
    }

    public void addHealth(int amount) {
        health = health + amount;
    }

    public int getHealthPercent() {
        return (int)(100 / max * health);
    }

    private int getDrawWidth() {
        return (int)(w / max * health);
    }
}
