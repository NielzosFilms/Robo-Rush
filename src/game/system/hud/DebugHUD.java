package game.system.hud;

import game.assets.entities.Player;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.world.World;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DebugHUD {
	private MouseInput mouse;
	private Player player;
	private World world;

	public DebugHUD() {
		//this.btn = new Button(50, 50, 30, 10, "New Seed");
	}

	public void setRequirements(MouseInput mouse, Player player, World world) {
		this.mouse = mouse;
		this.player = player;
		this.world = world;
	}

	public void tick() {
	}

	public void render(Graphics g, Graphics2D g2d) {
		Font font2 = new Font("SansSerif", Font.PLAIN, 4);
		g2d.setFont(font2);
		g2d.drawString("FPS: " + Game.current_fps, 1, 10);
		if (Game.DEDUG_MODE) {
			g2d.drawString("X: " + player.getX(), 1, 35);
			g2d.drawString("Y: " + player.getY(), 1, 40);
			// g2d.drawString("BIOME: " + player.getCurrentBiome().toString(), 1, 45);
			g2d.drawString("SEED: " + world.getSeed(), 1, 70);
		}
	}

	public void renderCam(Graphics g, Graphics2D g2d) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
