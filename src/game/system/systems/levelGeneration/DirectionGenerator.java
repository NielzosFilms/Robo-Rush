package game.system.systems.levelGeneration;

import java.util.LinkedList;
import java.util.Random;

public class DirectionGenerator {

    private int maxDuplicateDirections;
    private Random r = new Random();

    private LinkedList<Boolean> previousDuplicateDirections;

    public DirectionGenerator(int maxDuplicateDirections) {
        this.maxDuplicateDirections = maxDuplicateDirections;
        this.previousDuplicateDirections = new LinkedList<>();
    }

    public void setSeed(long seed) {
        r.setSeed(seed);
    }

    /**
     *
     * @return Boolean ? Horizontal : Vertical
     */
    public boolean getNextDirection() {
        boolean bool = r.nextBoolean();

        if(this.previousDuplicateDirections.size() > 0) {
            if(this.previousDuplicateDirections.getLast() == bool) {
                this.previousDuplicateDirections.add(bool);
                if(this.previousDuplicateDirections.size() >= this.maxDuplicateDirections) {
                    bool = !bool;
                    this.previousDuplicateDirections = new LinkedList<>();
                    this.previousDuplicateDirections.add(bool);
                }
            } else {
                this.previousDuplicateDirections = new LinkedList<>();
                this.previousDuplicateDirections.add(bool);
            }
        } else {
            this.previousDuplicateDirections.add(bool);
        }
        return bool;
    }
}
