package game.system.menu.buttons;

import game.enums.BUTTONS;
import game.enums.MENUSTATES;
import game.system.main.Game;
import game.textures.Fonts;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ButtonBack extends Button {
	private MENUSTATES goto_state;

	public ButtonBack(int x, int y, int width, int height, MENUSTATES goto_state) {
		super(x, y, width, height);
		this.btn_type = BUTTONS.Back;
		this.goto_state = goto_state;
	}

	public void render(Graphics g, Graphics2D g2d) {
		this.setColor(g);
		g.drawImage(Textures.default_btn, x, y, null);
		g.fillRect(x, y, width, height);

		g.setColor(Color.BLACK);
		g.setFont(Fonts.default_fonts.get(10));
		g.drawString("Back", x, y + height / 2);
	}

	public void handleClick(MouseEvent e) {
		Game.menuSystem.setState(goto_state);
	}
}
