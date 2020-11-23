package game.system.main;

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
        int xx = (int) (x + -cam.getX());
        int yy = (int) (y + -cam.getY());
        return new Point(xx, yy);
    }

    public static Point getScreenCoords(int x, int y, Camera cam) {
        int xx = (int) (x + cam.getX());
        int yy = (int) (y + cam.getY());
        return new Point(xx, yy);
    }
}
