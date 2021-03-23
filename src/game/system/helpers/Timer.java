package game.system.helpers;

import java.io.Serializable;

public class Timer implements Serializable {
    private int delay;
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

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
