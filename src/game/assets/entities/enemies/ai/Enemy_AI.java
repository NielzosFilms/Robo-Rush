package game.assets.entities.enemies.ai;

import game.assets.entities.enemies.ai.AI_ACTION;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    private AI_ACTION action = AI_ACTION.goto_target;
    private Timer decide = new Timer(120),
            decide_action = new Timer(120),
            decide_stuck_timer = new Timer(240);
    private Random r = new Random();
    private GameObject target, parent;
    private int wonderAreaSize = 75, circle_offset = 60, folow_time = 0, circle_radius = 70, chosen_circle_radius = 70, avoid_final_radius = 80;
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
        switch (action) {
            case goto_target:
                if(getTargetDistance() < circle_radius) {
                    chosen_circle_radius = getTargetDistance();
                    action = AI_ACTION.circle_target;
                    decide_stuck_timer.resetTimer();
                }
                if(decide_stuck_timer.timerOver()) {
                    decide_stuck_timer.resetTimer();
                    decide_stuck_timer.setDelay(r.nextInt(240)*2);
                    chosen_circle_radius = getTargetDistance();
                    action = AI_ACTION.circle_target;
                }
                decide_stuck_timer.tick();
                break;
            case avoid_target:
                if(getTargetDistance() > avoid_final_radius) {
                    decide_action.resetTimer();
                    action = AI_ACTION.stand_still;
                    decide_stuck_timer.resetTimer();
                }
                if(decide_stuck_timer.timerOver()) {
                    decide_stuck_timer.resetTimer();
                    decide_stuck_timer.setDelay(r.nextInt(240)*2);
                    chosen_circle_radius = getTargetDistance();
                    action = AI_ACTION.circle_target;
                    decide_action.resetTimer();
                }
                decide_stuck_timer.tick();
                break;
            case stand_still:
                if(getTargetDistance() < avoid_radius) {
                    chosen_circle_radius = circle_radius;
                    action = AI_ACTION.circle_target;
                    decide_stuck_timer.resetTimer();
                }
                if(decide_action.timerOver()) {
                    decide_action.setDelay(r.nextInt(240)*2);
                    decide_action.resetTimer();
                    if(r.nextInt(2) == 0) {
                        chosen_circle_radius = getTargetDistance();
                        action = AI_ACTION.circle_target;
                    } else {
                        action = AI_ACTION.goto_target;
                    }
                    decide_stuck_timer.resetTimer();
                }
                decide_action.tick();
                break;
            case circle_target:
                if(decide_action.timerOver()) {
                    decide_action.setDelay(r.nextInt(240)*2);
                    decide_action.resetTimer();
                    if(r.nextInt(2) == 0) {
                        action = AI_ACTION.avoid_target;
                    }
                    decide_stuck_timer.resetTimer();
                }
                decide_action.tick();
                break;

        }
    }

    private void setGotoAngles() {
        int target_angle = getClosestAngle(getTargetAngle());
        int target_dist = getTargetDistance();
        switch (action){
            case wander:
                wander();
                break;
            case circle_target:
                circleTarget(target_dist, target_angle, chosen_circle_radius);
                break;
            case goto_target:
                gotoTarget(target_angle, target_dist);
                break;
            case avoid_target:
                avoidTarget(target_angle);
                break;
            case stand_still:
                move = false;
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
        var bounds = getAllBounds();

        for(int angle : angles.keySet()) {
            if(angles.get(angle) < 0) {
                angles.put(angle, 0f);
            }
        }

        for(Rectangle bound : bounds) {
            checkBoundsInRangeAndSetAvoid(bound, avoid_radius);
        }
    }

    private List<Rectangle> getAllBounds() {
        var objects = Game.gameController.getHandler().getObjectsWithIds(ID.Enemy);
        objects.addAll(Game.gameController.getHandler().getBoundsObjects());

        var all_bounds = new ArrayList<Rectangle>();

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

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return this.velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
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

    public int getTargetDistance() {
        return (int) Helpers.getDistance(new Point(parent.getX(), parent.getY()), new Point(target.getX(), target.getY()));
    }

    public AI_ACTION getAction() {
        return this.action;
    }

    public void setAction(AI_ACTION action) {
        this.action = action;
    }

    public boolean inCombat() {
        return action != AI_ACTION.wander;
    }

    public GameObject getTarget() {
        return this.target;
    }

    /**
     * Predict bullet path with target.
     * @param bullet_speed
     * @return integer angle for the bullet to travel to hit the target
     */
    public int predictBulletDirection(float bullet_speed) {
        Point target_center = new Point(new Point((int)((Bounds)target).getBounds().getCenterX(), (int)((Bounds)target).getBounds().getCenterY()));
        Point origin = new Point(new Point((int)((Bounds)parent).getBounds().getCenterX(), (int)((Bounds)parent).getBounds().getCenterY()));

        double distance_to_target = Helpers.getDistance(origin, target_center);
        double travel_time = Math.round(distance_to_target / bullet_speed);

        Point target_location = new Point(
                (int)Math.round(target.getX() + target.getVelX() * travel_time),
                (int)Math.round(target.getY() + target.getVelY() * travel_time));
        return (int) Helpers.getAngle(origin, target_location);
    }

    public void setMax_vel(float max_vel) {
        this.max_vel = max_vel;
    }

    public void setWander_vel(float wander_vel) {
        this.wander_vel = wander_vel;
    }

    public void setCombat_vel(float combat_vel) {
        this.combat_vel = combat_vel;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void setDeceleration(float deceleration) {
        this.deceleration = deceleration;
    }

    public void setAvoid_radius(int avoid_radius) {
        this.avoid_radius = avoid_radius;
    }
}
