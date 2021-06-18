package game.system.systems.cutscene;

import game.system.inputs.KeyInput;
import game.system.main.Camera;
import game.system.main.Game;
import game.system.systems.cutscene.cutscenes.TestCutscene;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CutsceneEngine {
    private boolean isPlaying = false;
    private Cutscene currentCutscene;
    public Camera cam = new Camera(0, 0);

    public CutsceneEngine() {}

    public void tick() {
        if(isPlaying && currentCutscene != null) {
            currentCutscene.tick();
            cam.tick(null);
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
            g2d.translate(cam.getX(), cam.getY());
            currentCutscene.render(g, g2d);
            g2d.translate(-cam.getX(), -cam.getY());
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
