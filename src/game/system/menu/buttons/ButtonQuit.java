package game.system.menu.buttons;

import game.enums.BUTTONS;
import game.system.main.Logger;
import game.textures.Fonts;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ButtonQuit extends Button {
    public ButtonQuit(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.btn_type = BUTTONS.Quit;
    }
    public void render(Graphics g, Graphics2D g2d) {
        this.setColor(g);
        g.drawImage(Textures.default_btn, x, y, null);
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        g.setFont(Fonts.default_fonts.get(10));
        g.drawString("Quit", x, y + height / 2);
    }
    public void handleClick(MouseEvent e) {
        Logger.print("(╯°□°）╯︵ ┻━┻");
        System.exit(1);
    }
}
