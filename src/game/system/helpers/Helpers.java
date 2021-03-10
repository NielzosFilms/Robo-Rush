package game.system.helpers;

import game.assets.entities.player.PLAYER_STAT;
import game.enums.DIRECTIONS;
import game.system.main.Camera;
import game.system.systems.gameObject.Bounds;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class Helpers {

    public static int clampInt(int var, int min, int max) {
        if (var <= min) {
            var = min;
        } else if (var >= max) {
            var = max;
        }
        return var;
    }

    public static double clampDouble(double var, double min, double max) {
        if (var <= min) {
            var = min;
        } else if (var >= max) {
            var = max;
        }
        return var;
    }

    public static float clampFloat(float var, float min, float max) {
        if (var <= min) {
            var = min;
        } else if (var >= max) {
            var = max;
        }
        return var;
    }

    public static Point getWorldCoords(int x, int y, Camera cam) {
        int xx = Math.round(x - cam.getX());
        int yy = Math.round(y - cam.getY());
        return new Point(xx, yy);
    }

    public static Point getScreenCoords(int x, int y, Camera cam) {
        int xx = Math.round(x + cam.getX());
        int yy = Math.round(y + cam.getY());
        return new Point(xx, yy);
    }

    public static Point getTileCoords(Point world_coords, int item_w, int item_h) {
        int tile_x = Math.round(world_coords.x/item_w) * item_w;
        int tile_y = Math.round(world_coords.y/item_h) * item_h;
//        if(world_coords.x < 0) tile_x -= InventorySystem.item_w;
//        if(world_coords.y < 0) tile_y -= InventorySystem.item_h;
        return new Point(tile_x, tile_y);
    }

    public static double getDistance(Point crd_1, Point crd_2) {
        return Math.sqrt((crd_2.x - crd_1.x)
                * (crd_2.x - crd_1.x)
                + (crd_2.y - crd_1.y) * (crd_2.y - crd_1.y));
    }

    public static double getDistanceBetweenBounds(Rectangle bounds_1, Rectangle bounds_2) {
        Point cent_1 = new Point((int)bounds_1.getCenterX(), (int)bounds_1.getCenterY());
        Point cent_2 = new Point((int)bounds_2.getCenterX(), (int)bounds_2.getCenterY());
        return getDistance(cent_1, cent_2);
    }

    public static DIRECTIONS getDirection(Point origin, Point target) {
        int x_diff = Math.abs(target.x) - Math.abs(origin.x);
        int y_diff = Math.abs(target.y) - Math.abs(origin.y);

        if(Math.abs(x_diff) > Math.abs(y_diff)) {
            if(Math.abs(x_diff) > Math.abs(y_diff) - 5 && Math.abs(x_diff) < Math.abs(y_diff) + 5) {
                return DIRECTIONS.up_left;
            } else if(target.x < origin.x) {
                return DIRECTIONS.left;
            } else {
                return DIRECTIONS.right;
            }
        } else {
            if(target.y < origin.y) {
                return DIRECTIONS.up;
            } else {
                return DIRECTIONS.down;
            }
        }
    }

    public static void createDirIfNotExisting(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static float getAngle(Point origin, Point target) {
        float atan = (float) Math.atan2(target.y - origin.y, target.x - origin.x);
        float angle = (float) Math.toDegrees(atan);

        // to set 0 top the top
        //angle += 90;

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }

    public static int getNearest90Degrees(float angle) {
        float div = angle / 360;
        int rounded = Math.round(div * 4);
        return rounded * 90;
    }

    public static float getDotProduct(Point origin, Point target) {
        return (float) (Math.atan2(origin.x - target.x, target.y - origin.y) / (float) Math.PI);
    }

    public static void drawBounds(Graphics g, Bounds entity) {
        if(entity.getBounds() != null) g.drawRect(entity.getBounds().x, entity.getBounds().y, entity.getBounds().width, entity.getBounds().height);
        if(entity.getBottomBounds() != null) g.drawRect(entity.getBottomBounds().x, entity.getBottomBounds().y, entity.getBottomBounds().width, entity.getBottomBounds().height);
        if(entity.getTopBounds() != null) g.drawRect(entity.getTopBounds().x, entity.getTopBounds().y, entity.getTopBounds().width, entity.getTopBounds().height);
        if(entity.getLeftBounds() != null) g.drawRect(entity.getLeftBounds().x, entity.getLeftBounds().y, entity.getLeftBounds().width, entity.getLeftBounds().height);
        if(entity.getRightBounds() != null) g.drawRect(entity.getRightBounds().x, entity.getRightBounds().y, entity.getRightBounds().width, entity.getRightBounds().height);
    }

    public static boolean sameRectangle(Rectangle rect1, Rectangle rect2) {
        return rect1.x == rect2.x && rect1.y == rect2.y && rect1.width == rect2.width && rect1.height == rect2.height;

    }
}
