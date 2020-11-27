package game.system.main;

import game.system.inventory.InventorySystem;

import java.awt.*;

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
}
