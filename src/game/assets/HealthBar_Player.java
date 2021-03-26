package game.assets;

import game.system.systems.gameObject.HUD_Component;
import game.textures.COLOR_PALETTE;

import java.awt.*;

public class HealthBar_Player extends HealthBar implements HUD_Component {
    public HealthBar_Player(int x, int y, int min, int max, int hud_z_index) {
        super(x, y, min, max, hud_z_index);
    }

    @Override
    public void render(Graphics g) {
        int health_perc = getHealthPercent();
        g.setColor(COLOR_PALETTE.gray_0.color);
        g.fillRect(x, y, 80, 16);
        g.setColor(COLOR_PALETTE.red.color);
        g.fillRect(x+1, y+1,  Math.round((float)78 / 100 * health_perc), 14);
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
