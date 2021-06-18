package game.system.systems.cutscene;

import game.system.inputs.KeyInput;
import game.system.main.Game;
import game.system.systems.cutscene.cutscenes.TestCutscene;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CutsceneEngine {
    private boolean isPlaying = false;
    private Cutscene currentCutscene;

    public CutsceneEngine() {}

    public void tick() {
        if(isPlaying && currentCutscene != null) {
            currentCutscene.tick();
            if(KeyInput.keys_down.get(KeyEvent.VK_SPACE)) {
                skipCutscene();
            }
            if(!currentCutscene.running) {
                isPlaying = false;
                currentCutscene.onEnd();
            }
        }
    }

    public void render(Graphics g, Graphics2D g2d) {
        if(isPlaying && currentCutscene != null) {
            currentCutscene.render(g, g2d);
        }
    }

    public void playCutscene(Cutscene cutscene) {
        this.currentCutscene = cutscene;
        this.isPlaying = true;
    }

    public void skipCutscene() {
        this.isPlaying = false;
        if(this.currentCutscene != null) {
            this.currentCutscene.onEnd();
        }
    }
}
