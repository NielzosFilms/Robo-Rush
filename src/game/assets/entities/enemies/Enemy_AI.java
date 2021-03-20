package game.assets.entities.enemies;

import game.assets.HealthBar;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
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
    float wander_vel = 0.2f;
    float acceleration = 0.05f;
    float deceleration = 0.1f;

    int avoid_radius = 40;

    // Own variables
    private HashMap<Integer, Float> angles = new HashMap<>();
    private boolean move = true;
    private Action action = Action.wander;
    private Timer decide = new Timer(120);
    private Random r = new Random();
    private GameObject target, parent;
    private int wonderAreaSize = 75, circle_offset = 60, folow_time = 0;

    // return variables
    private float velX = 0f, velY = 0f;

    public Enemy_AI(GameObject target, GameObject parent) {
        this.target = target;
        this.parent = parent;
        decide.resetTimer();

        resetAngles();

        int random_angle = getClosestAngle(r.nextInt(360));
        System.out.println(random_angle);
        if(angles.containsKey(random_angle)) angles.put(random_angle, 1f);
    }

    public void tick() {
        float new_velX = getNewVelX(wander_vel);
        float new_velY = getNewVelY(wander_vel);

        if(move) {
            velX += (new_velX - velX) * acceleration;
            velY += (new_velY - velY) * acceleration;
        } else {
            velX -= (velX) * deceleration;
            velY -= (velY) * deceleration;
        }

        clampVelocity();

        runDecision();

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
        if(decide.timerOver()) {
            decide.resetTimer();
            decide.setDelay(r.nextInt(120)*2);
            resetAngles();

            wander();
        }
        decide.tick();
    }

    private void wander() {
        if(r.nextInt(2) == 0) {
            move = true;
            int random_angle = getClosestAngle(r.nextInt(360));
            if(angles.containsKey(random_angle)) angles.put(random_angle, 1f);
        } else {
            move = false;
        }
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
//            if(object == target) continue;
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
}
