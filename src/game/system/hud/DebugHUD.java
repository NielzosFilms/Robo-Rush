package game.system.hud;

import game.assets.entities.Player;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.menu.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class DebugHUD {
	private MouseInput mouse;
	private Player player;

	private Button btn;

	public DebugHUD(MouseInput mouse) {
		this.mouse = mouse;
		this.player = Game.player;
		this.btn = new Button(50, 50, "New Seed");
	}

	public void tick() {
		if(mouse.mouseOverLocalRect(btn.getBounds())) {
			btn.setState("hovered");
		} else {
			btn.setState("static");
		}
	}

	public void render(Graphics g, Graphics2D g2d) {
		btn.render(g, g2d);
	}

	public void renderCam(Graphics g, Graphics2D g2d) {
	}

	public void mousePressed(MouseEvent e) {
		if(mouse.mouseOverLocalRect(btn.getBounds())) {
			btn.setState("pressed");
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(mouse.mouseOverLocalRect(btn.getBounds())) {
			Game.world.generate(new Random().nextLong());
			btn.setState("clicked");
		}
	}
}
