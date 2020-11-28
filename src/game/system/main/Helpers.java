package game.system.main;

import game.system.inventory.InventorySystem;

import java.awt.*;
import java.awt.geom.Rectangle2D;

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
        if(world_coords.x < 0) tile_x -= InventorySystem.item_w;
        if(world_coords.y < 0) tile_y -= InventorySystem.item_h;
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
}
