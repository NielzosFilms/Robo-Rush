package game.assets.entities.enemies;

import game.assets.HealthBar;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

enum Action {
    wander,
    goto_target,
    avoid_target,
    circle_target,
    attack_target,
}

public class Enemy_AI {
    float max_vel = 1f; //0.8
    float wander_vel = 0.5f;
    float acceleration = 0.05f;
    float deceleration = 0.1f;

    // Own variables
    private HashMap<Integer, Float> angles = new HashMap<>();
    private boolean move = true;
    private Action action = Action.wander;
    private Timer decide = new Timer(120);
    private Timer decideAction = new Timer(120);
    private Random r = new Random();
    private GameObject target;
    private int wonderAreaSize = 75, circle_offset = 60, folow_time = 0;

    // return variables
    private float velX = 0f, velY = 0f;

    public Enemy_AI(GameObject target) {
        this.target = target;
        decide.resetTimer();

        for(int i=0; i<360; i+=30) {
            angles.put(i, 0.5f);
        }
        int random_angle = getClosestAngle(r.nextInt(12)*360);
        if(angles.containsKey(random_angle)) angles.put(random_angle, 1f);
    }

    public void tick() {
        float targ_velX = 0f;
        float targ_velY = 0f;
        for(int angle : getPositiveAngles()) {
            if(action == Action.wander) {
                targ_velX += (float) (wander_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
                targ_velY += (float) (wander_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
            } else {
                targ_velX += (float) (max_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
                targ_velY += (float) (max_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
            }
        }

        for(int angle : getNegativeAngles()) {
            if(action == Action.wander) {
                targ_velX -= (float) (wander_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
                targ_velY -= (float) (wander_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
            } else {
                targ_velX -= (float) (max_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
                targ_velY -= (float) (max_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
            }
        }

        System.out.println("targX: " + targ_velX);
        System.out.println("targY: " + targ_velY);

        if(move) {
            velX += (targ_velX - 1f) * acceleration;
            velY += (targ_velY - 1f) * acceleration;
        } else {
            velX -= (velX) * deceleration;
            velY -= (velY) * deceleration;
        }

        if(decide.timerOver()) {
            decide.resetTimer();
            for(int i=0; i<360; i+=30) {
                angles.put(i, 0.5f);
            }
            int random_angle = getClosestAngle(r.nextInt(360));
            if(angles.containsKey(random_angle)) angles.put(random_angle, 1f);
        }
        decide.tick();
    }

    private LinkedList<Integer> getPositiveAngles() {
        LinkedList<Integer> ret = new LinkedList<>();
        for(int angle : angles.keySet()) {
            if(angles.get(angle) > 0f) {
                ret.add(angle);
            }
        }
        return ret;
    }
    private LinkedList<Integer> getNegativeAngles() {
        LinkedList<Integer> ret = new LinkedList<>();
        for(int angle : angles.keySet()) {
            if(angles.get(angle) < 0f) {
                ret.add(angle);
            }
        }
        return ret;
    }

    private int getClosestAngle(int input_angle) {
        int closest = 0;
        int distance = input_angle;
        for(int angle : angles.keySet()) {
            if(angle == 0) continue;
            int tmp_distance = Math.abs(angle - input_angle);
            if(tmp_distance < distance) {
                distance = tmp_distance;
                closest = angle;
            }
        }
        return closest;
    }

    public float getVelX() {
        return this.velX;
    }

    public float getVelY() {
        return this.velY;
    }
}
