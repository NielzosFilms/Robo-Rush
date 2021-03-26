package game.assets;

import game.system.helpers.Logger;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.HUD_Component;
import game.textures.COLOR_PALETTE;
import game.textures.TEXTURE_LIST;
import game.system.main.Game;
import game.system.helpers.Helpers;
import game.textures.Textures;

import java.awt.*;
import java.io.Serializable;

public class HealthBar extends GameObject implements HUD_Component {
    protected static final Color background = COLOR_PALETTE.black.color;
    protected static final int HIDE_DELAY = 60*5;
    protected int hide_timer = HIDE_DELAY;
    protected Color healthBar_color = COLOR_PALETTE.red.color;
    protected int min = 0, max = 100;
    protected int w = 24, h = 4;
    protected int health;
    protected int x, y;

    public HealthBar(int x, int y, int min, int max, int hud_z_index) {
        super(x, y, hud_z_index, null);
        this.x = x;
        this.y = y;
        this.min = min;
        this.max = max;
        this.health = max;
    }

    public void tick() {
        if(hide_timer > 0) hide_timer--;
    }

    public void render(Graphics g) {
        if(health != max && hide_timer != 0) {
            g.drawImage(Textures.healthbar, x, y, 24, 4, null);
            int health_perc = getHealthPercent();
            float div = (float)Textures.texture_lists.get(TEXTURE_LIST.healthbar_list).size() / 100;
            int until = Math.round(div * health_perc);
            for(int i=0; i<until; i++) {
                g.drawImage(Textures.texture_lists.get(TEXTURE_LIST.healthbar_list).get(new Point(i, 0)), x + i + 1, y + 1, 1, 2, null);
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
        float div = 100 / (float)max;
        return Math.round(div * health);
    }

    public int getHealth() {
        return health;
    }

    private int getDrawWidth() {
        return (int)(w / max * health);
    }

    public boolean showing() {
        return hide_timer > 0;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
