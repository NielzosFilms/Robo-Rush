package game.system.helpers;

public class Timer {
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
