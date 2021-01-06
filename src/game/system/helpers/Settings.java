package game.system.helpers;

import game.system.main.Game;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Settings implements Serializable {
    private Texture cursor = new Texture(TEXTURE_LIST.cursors, 2);
    private float sound_vol = 1f;
    private float music_vol = 1f;

    public Settings() {}

    public void save() {
        Game.loadingAnimation.setLoading(true);
        if(!Game.NO_SAVE) {
            try {
                FileOutputStream fos = new FileOutputStream("gameSettings.data");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(this);

                fos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Game.loadingAnimation.setLoading(false);
    }

    public void setSoundVolFloat(float vol) {
        sound_vol = vol;
    }

    public void setSoundVolPercent(int percent) {
        sound_vol = (float) percent / 100;
    }

    public void setMusicVolFloat(float vol) {
        music_vol = vol;
    }

    public void setMusicVolPercent(int percent) {
        music_vol = (float) percent / 100;
    }

    public float getMusic_vol() {
        return music_vol;
    }

    public float getSound_vol() {
        return sound_vol;
    }

    public void setCursor(Texture cursor) {
        this.cursor = cursor;
    }

    public Texture getCursor() {
        return cursor;
    }
}
