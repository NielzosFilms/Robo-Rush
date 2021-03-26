package game.assets;

import game.system.main.Game;
import game.system.systems.gameObject.HUD_Component;
import game.textures.COLOR_PALETTE;
import game.textures.TEXTURE_LIST;
import game.textures.Textures;

import java.awt.*;

public class HealthBar_Boss extends HealthBar implements HUD_Component {
    public HealthBar_Boss(int x, int y, int min, int max, int hud_z_index) {
        super(x, y, min, max, hud_z_index);
    }

    @Override
    public void render(Graphics g) {
        int health_perc = getHealthPercent();
        g.setColor(COLOR_PALETTE.gray_0.color);
        g.fillRect(0, 16, 200, 16);
        g.setColor(COLOR_PALETTE.red.color);
        g.fillRect(1, 17,  Math.round((float)198 / 100 * health_perc), 14);
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
