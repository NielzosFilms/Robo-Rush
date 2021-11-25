package game.system.systems.levelGeneration;

public class Line {
    public int x1, x2, y1, y2, depth;

    public Line(int x1, int y1, int x2, int y2, int depth) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.depth = depth;
    }
}
