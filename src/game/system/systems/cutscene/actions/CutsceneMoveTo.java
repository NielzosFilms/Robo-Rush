package game.system.systems.cutscene.actions;

import game.system.helpers.Helpers;
import game.system.systems.cutscene.CutsceneAction;
import game.system.systems.gameObject.GameObject;

import java.awt.*;

public class CutsceneMoveTo extends CutsceneAction {
    private GameObject object;
    private Point start, end;
    private int duration, currentTime = 0;
    float angle, distance, speed;

    private boolean running = false;
    public CutsceneMoveTo(GameObject object, Point start, Point end, int duration) {
        this.object = object;
        this.start = start;
        this.end = end;
        this.duration = duration;

        angle = Helpers.getAngle(start, end);
        distance = (float)Helpers.getDistance(start, end);
        speed = distance/duration;

        running = true;
    }

    @Override
    public void tick() {
        if(running) {
            if(currentTime >= duration) {
                running = false;
                onEnd();
            } else {
                object.setVelX((float) (speed*Math.cos(Math.toRadians(angle))));
                object.setVelY((float) (speed*Math.sin(Math.toRadians(angle))));
            }
            currentTime++;
        }
    }

    @Override
    public void onEnd() {
        object.setVelX(0f);
        object.setVelY(0f);
    }
}
