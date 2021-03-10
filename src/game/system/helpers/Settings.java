package game.system.helpers;

import game.enums.SETTING;
import game.system.main.Game;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Settings {
    private static final String FILENAME = "game_settings.txt";
    private Texture cursor;

    private HashMap<SETTING, Float> settings = new HashMap<SETTING, Float>() {{
        put(SETTING.sound_vol, 1f);
        put(SETTING.music_vol, 1f);
        put(SETTING.cursor, 2f);
    }};

    public Settings() {
        loadSettings();
        this.cursor = new Texture(TEXTURE_LIST.cursors, Math.round(settings.get(SETTING.cursor)));
    }

    public void saveSettings() {
        Game.loadingAnimation.setLoading(true);
        if(!Game.NO_SAVE || true) {
            try {
                File file = new File(FILENAME);
                file.createNewFile();
                FileWriter writer = new FileWriter(FILENAME);

                for(SETTING key : settings.keySet()) {
                    writer.write(key.toString() + "=" + settings.get(key) + "\n");
                }

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Game.loadingAnimation.setLoading(false);
    }

    public void loadSettings() {
        Game.loadingAnimation.setLoading(true);
        if(!Game.NO_LOAD || true) {
            try {
                File file = new File(FILENAME);
                if(file.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while((line = br.readLine()) != null) {
                        String key = line.substring(0, line.indexOf("="));
                        float val = Float.parseFloat(line.substring(line.indexOf("=") + 1));
                        settings.put(SETTING.valueOf(key), val);
                    }
                } else {
                    Logger.printWarning("No setting file found...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Game.loadingAnimation.setLoading(false);
    }

    public float getSetting(SETTING setting) {
        if(!this.settings.containsKey(setting)) return 0f;
        return this.settings.get(setting);
    }

    public void setSetting(SETTING setting, float val) {
        this.settings.put(setting, val);
    }

    public void setCursor(Texture cursor) {
        this.cursor = cursor;
    }

    public Texture getCursor() {
        return cursor;
    }
}
