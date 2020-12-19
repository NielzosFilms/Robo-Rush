package game.system.systems.menu.buttons;

import game.enums.BUTTONS;
import game.enums.MENUSTATES;
import game.system.audioEngine.AudioFiles;
import game.system.audioEngine.AudioPlayer;
import game.system.main.Game;
import game.textures.Fonts;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ButtonPlay extends Button {
    public ButtonPlay(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.btn_type = BUTTONS.Play;
    }

    public void render(Graphics g, Graphics2D g2d) {
        this.setColor(g);
        g.drawImage(Textures.default_btn, x, y, null);
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        g.setFont(Fonts.default_fonts.get(10));
        g.drawString("Play", x, y + height / 2);
    }

    public void handleClick(MouseEvent e) {
        AudioPlayer.playSound(AudioFiles.menu_forward, 0.7f, false, 0);
        Game.menuSystem.update();
        Game.menuSystem.setState(MENUSTATES.SaveSlotSelect);
    }
}
