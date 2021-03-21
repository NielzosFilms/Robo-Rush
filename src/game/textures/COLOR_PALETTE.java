package game.textures;

import java.awt.*;

/**
 * Pre-defined color palette from Lospec.com.
 * Color palette name: Endesga 32
 * https://lospec.com/palette-list/endesga-32
 */
public enum COLOR_PALETTE {
    alloy_orange(Color.decode("#be4a2f")),
    dark_almond(Color.decode("#d77643")),
    light_almond(Color.decode("#ead4aa")),
    almond(Color.decode("#e4a672")),
    oak(Color.decode("#b86f50")),
    spruce(Color.decode("#733e39")),
    dark_oak(Color.decode("#3e2731")),
    cherry_red(Color.decode("#a22633")),
    red(Color.decode("#e43b44")),
    orange(Color.decode("#f77622")),
    gold(Color.decode("#feae34")),
    yellow(Color.decode("#fee761")),
    light_green(Color.decode("#63c74d")),
    green(Color.decode("#3e8948")),
    dark_green(Color.decode("#265c42")),
    blue_green(Color.decode("#193c3e")),
    dark_blue(Color.decode("#124e89")),
    blue(Color.decode("#0099db")),
    light_blue(Color.decode("#2ce8f5")),
    white(Color.decode("#ffffff")),
    gray_4(Color.decode("#c0cbdc")),
    gray_3(Color.decode("#8b9bb4")),
    gray_2(Color.decode("#5a6988")),
    gray_1(Color.decode("#3a4466")),
    gray_0(Color.decode("#262b44")),
    black(Color.decode("#181425")),
    light_red(Color.decode("#ff0044")),
    purple(Color.decode("#68386c")),
    magenta(Color.decode("#b55088")),
    salmon(Color.decode("#f6757a")),
    desert_sand(Color.decode("#e8b796")),
    desert(Color.decode("#c28569"));

    /**
     * Yeet
     */
    public Color color;

    COLOR_PALETTE(Color color) {
        this.color = color;
    }
}
