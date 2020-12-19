package game.system.helpers;

import java.awt.*;

public class Offsets {
    public static Point[] edge_offsets = new Point[]{
            new Point(0, -1),
            new Point(1, 0),
            new Point(0, 1),
            new Point(-1, 0),
    };

    public static Point[] corner_offsets = new Point[]{
            new Point(1, -1),
            new Point(1, 1),
            new Point(-1, 1),
            new Point(-1, -1),
    };

    public static Point[] all_offsets = new Point[]{
            new Point(0, -1),
            new Point(1, 0),
            new Point(0, 1),
            new Point(-1, 0),

            new Point(1, -1),
            new Point(1, 1),
            new Point(-1, 1),
            new Point(-1, -1),
    };
}
