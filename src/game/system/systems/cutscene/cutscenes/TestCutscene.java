package game.system.systems.cutscene.cutscenes;

import game.assets.entities.player.Character_Robot;
import game.assets.entities.player.Player;
import game.enums.GAMESTATES;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.systems.cutscene.Cutscene;
import game.system.systems.cutscene.CutsceneScript;
import game.system.systems.gameObject.GameObject;

import java.awt.*;

public class TestCutscene extends Cutscene {

    public TestCutscene() {
        this.scripts.add(new CutsceneScript() {
            GameObject robot = new Character_Robot(100, 100, 1);
            int timer = 120, walkDuration = 60 * 4, mirror1Duration = 60, mirror2Duration = 60;
            @Override
            public void tick() {
//                if(timer > 120) {
//                    this.running = false;
//                }
                if(walkDuration > 0) {
                    robot.setVelX(0.5f);
                    walkDuration--;
                } else {
                    robot.setVelX(0f);
                    if(mirror1Duration > 0) {
                        mirror1Duration--;
                    } else {
                        ((Player)robot).setMirror(true);
                        if(mirror2Duration > 0) {
                            mirror2Duration--;
                        } else {
                            ((Player)robot).setMirror(false);
                            if(timer > 0) {
                                timer--;
                            } else {
                                running = false;
                            }
                        }
                    }
                }
                robot.tick();
            }

            @Override
            public void render(Graphics g, Graphics2D g2d) {
                robot.render(g);
            }
        });
    }

    @Override
    public void onEnd() {
        Game.game_state = GAMESTATES.Game;
    }
}
