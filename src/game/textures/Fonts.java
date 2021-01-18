package game.textures;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Fonts {

    public static ArrayList<Font> default_fonts = new ArrayList<>();

    public Fonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemResourceAsStream("assets/main/hud/gameovercre1.ttf")));

            //System.out.println(Arrays.toString(ge.getAllFonts()));

            for(int i = 0;i < 50;i++) {
                default_fonts.add(new Font("gameovercre", Font.PLAIN, i));
            }

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
