package game.assets.objects;

import game.assets.entities.player.Character_Robot;
import game.assets.entities.player.Player;
import game.audio.SoundEffect;
import game.enums.ID;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Trigger;
import game.textures.COLOR_PALETTE;

import java.awt.*;

public class EnergyRefill extends GameObject implements Bounds, Trigger {
    private int refillCharges = 3, refillAmount = 30;
    private boolean canTrigger = true;

    public EnergyRefill(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(COLOR_PALETTE.yellow.color);
        g.drawRect(x, y, width, height);
    }

    @Override
    public boolean canTrigger() {
        return refillCharges > 0 && canTrigger;
    }

    @Override
    public void setTriggerActive(boolean triggerActive) {
        canTrigger = triggerActive;
    }

    @Override
    public boolean triggerCollision() {
        return false;
    }

    @Override
    public void triggered(Player player) {
        canTrigger = false;
        if(player instanceof Character_Robot) {
            Character_Robot robot = (Character_Robot) player;
            if(!robot.isEnergyMax()) {
                robot.addEnergy(refillAmount);
                refillCharges--;
                SoundEffect.inv_select_1.play();
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle getTopBounds() {
        return null;
    }

    @Override
    public Rectangle getBottomBounds() {
        return null;
    }

    @Override
    public Rectangle getLeftBounds() {
        return null;
    }

    @Override
    public Rectangle getRightBounds() {
        return null;
    }
}
