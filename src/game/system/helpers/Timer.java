package game.system.helpers;

import java.io.Serializable;

public class Timer implements Serializable {
    private final int delay;
    private int timer;

    public Timer(int delay) {
        this.delay = delay;
    }

    public void tick() {
        if(timer > 0) timer--;
    }

    public void resetTimer() {
        timer = delay;
    }

    public boolean timerOver() {
        return timer <= 0;
    }
}
