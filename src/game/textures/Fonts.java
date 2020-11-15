package game.textures;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Fonts {

    public static ArrayList<Font> gameria_fonts = new ArrayList<>();

    public Fonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/main/hud/gameria.ttf")));

            for(int i = 0;i < 50;i++) {
                gameria_fonts.add(new Font("GAMERIA", Font.PLAIN, i));
            }

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
