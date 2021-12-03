package game.assets.entities.orbs;

import game.assets.entities.player.Character_Robot;
import game.assets.entities.player.Player;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Trigger;
import game.textures.COLOR_PALETTE;

import java.awt.*;

public class HealthOrb extends Orb {
    private int healthAmount = 5;
    public HealthOrb(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(COLOR_PALETTE.red.color);
        g.fillRect(x-2, y-2, 4, 4);
    }

    @Override
    public void triggered(Player player) {
        player.getHealth_player().addHealth(healthAmount);
        // TODO remove orb
//        Game.gameController.getActiveLevel().getActiveRoom().removeObject(this);
    }
}
