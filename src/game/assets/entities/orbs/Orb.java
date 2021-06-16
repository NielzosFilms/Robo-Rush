package game.assets.entities.orbs;

import game.assets.entities.player.Character_Robot;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Trigger;

import java.awt.*;
import java.util.Random;

public abstract class Orb extends GameObject implements Trigger, Bounds {
    protected Character_Robot robot;
    protected float newVelX = 0f, newVelY = 0f;
    public Orb(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        if(Game.gameController.getPlayer() instanceof Character_Robot){
            this.robot = (Character_Robot)Game.gameController.getPlayer();
        }
        int angle = new Random().nextInt(360);
        velX = (float) (1f * Math.cos(Math.toRadians(angle)));
        velY = (float) (1f * Math.sin(Math.toRadians(angle)));
    }

    public void tick() {
        int cenX = (int) robot.getBounds().getCenterX();
        int cenY = (int) robot.getBounds().getCenterY();
        double distance = Helpers.getDistance(new Point(x, y), new Point(cenX, cenY));
        if(distance < 60) {
            newVelX = (float) (2f * Math.cos(Math.toRadians(Helpers.getAngle(new Point(x, y), new Point(cenX, cenY)))));
            newVelY = (float) (2f * Math.sin(Math.toRadians(Helpers.getAngle(new Point(x, y), new Point(cenX, cenY)))));
            velX += (newVelX - velX) * 0.1f;
            velY += (newVelY - velY) * 0.1f;
        } else {
            velX -= (velX) * 0.1f;
            velY -= (velY) * 0.1f;
        }
        buffer_x += velX;
        x = Math.round(buffer_x);
        buffer_y += velY;
        y = Math.round(buffer_y);

    }
    @Override
    public boolean canTrigger() {
        return true;
    }

    @Override
    public void setTriggerActive(boolean triggerActive) {

    }

    @Override
    public boolean triggerCollision() {
        return false;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x-2, y-2, 4, 4);
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
