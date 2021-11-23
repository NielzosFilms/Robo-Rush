package game.system.systems.levelGeneration;

import java.awt.*;
import java.util.UUID;

public class SplitRectangle {
    public Rectangle rect;
    public int depth;
    public UUID uuid, parentUuid;

    public SplitRectangle(int depth, UUID parentUuid, Rectangle rect) {
        this.depth = depth;
        this.rect = rect;
        this.uuid = UUID.randomUUID();
        this.parentUuid = parentUuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "depth:" + depth + " & uuid:" + uuid.toString().substring(0, 8) + " & parentUuid:" + parentUuid.toString().substring(0, 8);
    }
}
