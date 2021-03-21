package game.assets.entities.enemies;

import game.assets.HealthBar;
import game.assets.entities.bullets.Bullet;
import game.assets.entities.bullets.EnemyBullet;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.Attack;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Enemy_AI {
    float max_vel = 1f; //0.8
    float wander_vel = 0.2f;
    float combat_vel = 0.6f;
    float acceleration = 0.05f;
    float deceleration = 0.1f;

    int avoid_radius = 40;

    // Own variables
    private HashMap<Integer, Float> angles = new HashMap<>();
    private boolean move = true;
    private AI_ACTION action = AI_ACTION.circle_target;
    private Timer decide = new Timer(120),
            decide_action = new Timer(120);
    private Random r = new Random();
    private GameObject target, parent;
    private int wonderAreaSize = 75, circle_offset = 60, folow_time = 0;
    private String circle_direction = "right";

    // return variables
    private float velX = 0f, velY = 0f;

    public Enemy_AI(GameObject target, GameObject parent) {
        this.target = target;
        this.parent = parent;
        decide.resetTimer();

        resetAngles();
    }

    public void tick() {
        float new_velX = getNewVelX(combat_vel);
        float new_velY = getNewVelY(combat_vel);

        if(move) {
            velX += (new_velX - velX) * acceleration;
            velY += (new_velY - velY) * acceleration;
        } else {
            velX -= (velX) * deceleration;
            velY -= (velY) * deceleration;
        }

        clampVelocity();

        runDecision();

        setGotoAngles();

        setAnglesToAvoid();
    }

    private float getNewVelX(float velocity) {
        float new_velX = 0f;
        for(int angle : angles.keySet()) {
            if(angles.get(angle) > 0f) {
                new_velX += (float) (velocity * Math.cos(Math.toRadians(getClosestAngle(angle))));
            } else if(angles.get(angle) < 0f){
                new_velX -= (float) (velocity * Math.cos(Math.toRadians(getClosestAngle(angle))));
            }
        }
        return new_velX;
    }
    private float getNewVelY(float velocity) {
        float new_velY = 0f;
        for(int angle : angles.keySet()) {
            if(angles.get(angle) > 0f) {
                new_velY += (float) (velocity * Math.sin(Math.toRadians(getClosestAngle(angle))));
            } else if(angles.get(angle) < 0f){
                new_velY -= (float) (velocity * Math.sin(Math.toRadians(getClosestAngle(angle))));
            }
        }
        return new_velY;
    }

    private void clampVelocity() {
        if(Math.abs(velX) > max_vel) {
            velX -= (velX) * deceleration;
        }
        if(Math.abs(velY) > max_vel) {
            velY -= (velY) * deceleration;
        }
    }

    private void resetAngles() {
        for(int i=0; i<360; i+=30) {
            angles.put(i, 0f);
        }
    }

    private void runDecision() {
        if(decide_action.timerOver()) {
            decide_action.resetTimer();
            decide_action.setDelay(r.nextInt(240)*2);

//            if(r.nextInt(2) == 0) {
//                action = Action.wander;
//            } else {
//                action = Action.circle_target;
//            }
        }
        decide_action.tick();
    }

    private void setGotoAngles() {
        int target_angle = getClosestAngle((int) Helpers.getAngle(new Point(parent.getX(), parent.getY()), new Point(target.getX(), target.getY())));
        int target_dist = (int) Helpers.getDistance(new Point(parent.getX(), parent.getY()), new Point(target.getX(), target.getY()));
        switch (action){
            case wander:
                wander();
                break;
            case circle_target:
                circleTarget(target_dist, target_angle, 100);
                break;
            case goto_target:
                gotoTarget(target_angle, target_dist);
                break;
            case avoid_target:
                avoidTarget(target_angle);
                break;
        }
    }

    private void wander() {
        if(decide.timerOver()) {
            decide.setDelay(r.nextInt(120)*2);
            decide.resetTimer();
            resetAngles();
            if (r.nextInt(2) == 0) {
                move = true;
                int random_angle = getClosestAngle(r.nextInt(360));
                if (angles.containsKey(random_angle)) angles.put(random_angle, 1f);
            } else {
                move = false;
            }
        }
        decide.tick();
    }

    private void circleTarget(int target_dist, int target_angle, int circle_radius) {
        resetAngles();
        move = true;
        if(decide.timerOver()) {
            decide.setDelay(r.nextInt(120)*2);
            decide.resetTimer();
            if(r.nextInt(2) == 0) {
                circle_direction = "right";
            } else {
                circle_direction = "left";
            }
        }
        decide.tick();
        if(target_dist < circle_radius) {
            angles.put(target_angle, -0.3f);

        } else {
            angles.put(target_angle, 1f);
        }

        if(circle_direction.equals("right")) {
            angles.put(getOffsetAngle(target_angle, circle_offset), 1f);
        } else {
            angles.put(getOffsetAngle(target_angle, -circle_offset), 1f);
        }
    }

    private void gotoTarget(int target_angle, int target_dist) {
        resetAngles();
        move = true;
        if(target_dist > this.avoid_radius) {
            angles.put(target_angle, 1f);
        }
    }

    private void avoidTarget(int target_angle) {
        resetAngles();
        move = true;
        angles.put(getOffsetAngle(target_angle, 180), 1f);
    }

    private void setAnglesToAvoid() {
        LinkedList<Rectangle> bounds = getAllBounds();

        for(int angle : angles.keySet()) {
            if(angles.get(angle) < 0) {
                angles.put(angle, 0f);
            }
        }

        for(Rectangle bound : bounds) {
            checkBoundsInRangeAndSetAvoid(bound, avoid_radius);
        }
    }

    private LinkedList<Rectangle> getAllBounds() {
        LinkedList<GameObject> objects = Game.gameController.getHandler().getObjectsWithIds(ID.Enemy);
        objects.addAll(Game.gameController.getHandler().getBoundsObjects());

        LinkedList<Rectangle> all_bounds = new LinkedList<>();

        for(GameObject object : objects) {
//            if(action == Action.goto_target || action == Action.wander) {
//                if(object == target) continue;
//            }
            if(object == parent) continue;
            if(object instanceof Bounds) {
                if(((Bounds) object).getBounds() != null) {
                    all_bounds.add(((Bounds) object).getBounds());
                }
            }
        }
        return all_bounds;
    }

    private void checkBoundsInRangeAndSetAvoid(Rectangle bounds, int radius) {
        int boundsX = (int) bounds.getCenterX();
        int boundsY = (int) bounds.getCenterY();
        int distance = (int) Helpers.getDistance(new Point(parent.getX(), parent.getY()), new Point(boundsX, boundsY));
        Point parent_coords = new Point((int) ((Bounds)parent).getBounds().getCenterX(), (int) ((Bounds)parent).getBounds().getCenterY());

        // This fixes center too far from edge??????????
        if(Helpers.getDistance(parent_coords, new Point(bounds.x, boundsY)) < distance) {
            boundsX = bounds.x;
        }
        if(Helpers.getDistance(parent_coords, new Point(bounds.x + bounds.width, boundsY)) < distance) {
            boundsX = bounds.x + bounds.width;
        }

        if(Helpers.getDistance(parent_coords, new Point(boundsX, bounds.y)) < distance) {
            boundsY = bounds.y;
        }
        if(Helpers.getDistance(parent_coords, new Point(boundsX, bounds.y + bounds.height)) < distance) {
            boundsY = bounds.y + bounds.height;
        }

        if(distance < radius) {
            int angle = getClosestAngle((int) Helpers.getAngle(parent_coords, new Point(boundsX, boundsY)));
            angles.put(angle, -0.3f);
        }
    }

    private int getHighestAngle() {
        int highest = 0;
        float lastval = 0f;
        for(int angle : angles.keySet()) {
            if(angles.get(angle) > lastval) {
                lastval = angles.get(angle);
                highest = angle;
            }
        }
        return highest;
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

    private int getOffsetAngle(int angle, int offset) {
        int ret = angle + offset;
        if(ret < 0) ret += 360;
        if(ret >= 360) ret -= 360;
        //if(ret == 360) ret = 0;
        return ret;
    }

    public float getVelX() {
        return this.velX;
    }

    public float getVelY() {
        return this.velY;
    }

    public void drawAi(Graphics g, int x, int y) {
        if(!Game.DEBUG_MODE) return;
        for(int angle : angles.keySet()) {
            if(angle == getHighestAngle()) {
                g.setColor(new Color(99, 199, 77));
            } else {
                g.setColor(new Color(255, 0, 68));
            }
            int length = (int) ((int) 10 + (angles.get(angle)*20));
            int startX = (int) Math.round(x + 10 * Math.cos(Math.toRadians(angle)));
            int startY = (int) Math.round(y + 10 * Math.sin(Math.toRadians(angle)));

            int endX = (int) Math.round(x + length * Math.cos(Math.toRadians(angle)));
            int endY = (int) Math.round(y + length * Math.sin(Math.toRadians(angle)));
            g.drawLine(startX, startY, endX, endY);
            //g.drawString(String.valueOf(angle), endX, endY);
        }

        g.setColor(new Color(139, 155, 180));
        g.drawArc(x-10, y-10, 20, 20, 0, 360);
        g.setColor(new Color(99, 199, 77));
        g.drawString(action.name(), x, y);
    }

    public int getTargetAngle() {
        return (int) Helpers.getAngle(new Point(parent.getX(), parent.getY()), new Point(target.getX(), target.getY()));
    }

    public AI_ACTION getAction() {
        return this.action;
    }
}
