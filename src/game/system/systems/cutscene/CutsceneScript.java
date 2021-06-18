package game.system.systems.cutscene;

import java.awt.*;

public abstract class CutsceneScript {
    public boolean running = true;

    public abstract void tick();
    public abstract void render(Graphics g, Graphics2D g2d);
}
