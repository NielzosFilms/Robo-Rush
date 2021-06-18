package game.system.systems.cutscene;

import game.assets.entities.player.Character_Robot;
import game.system.helpers.Helpers;
import game.system.systems.cutscene.actions.CutsceneMoveTo;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.LinkedList;

public abstract class Cutscene {
    protected LinkedList<CutsceneScript> scripts = new LinkedList<>();
    protected int activeScriptIndex = 0;
    public boolean running = true;

    public void tick() {
        scripts.get(activeScriptIndex).tick();
        if(!scripts.get(activeScriptIndex).running) {
            if(activeScriptIndex + 1 < scripts.size()) {
                activeScriptIndex++;
            } else {
                this.running = false;
            }
        }
    }
    public void render(Graphics g, Graphics2D g2d) {
        scripts.get(activeScriptIndex).render(g, g2d);
    }

    public abstract void onEnd();

}
