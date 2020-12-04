package game.system.helpers;

public class Settings {
    public static float sound_vol = 1f;
    public static float music_vol = 1f;

    public static void setSoundVolFloat(float vol) {
        sound_vol = vol;
    }

    public static void setSoundVolPercent(int percent) {
        sound_vol = (float) percent / 100;
    }

    public static void setMusicVolFloat(float vol) {
        music_vol = vol;
    }

    public static void setMusicVolPercent(int percent) {
        music_vol = (float) percent / 100;
    }
}
