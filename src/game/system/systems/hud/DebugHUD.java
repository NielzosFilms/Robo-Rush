package game.system.systems.hud;

import game.assets.entities.player.PLAYER_STAT;
import game.assets.entities.player.Player;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.main.GameController;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.HashMap;

public class DebugHUD implements Serializable {
	private transient MouseInput mouse;
	private transient Player player;
	private transient GameController gameController;

	public DebugHUD() {
		//this.btn = new Button(50, 50, 30, 10, "New Seed");
	}

	public void setRequirements(MouseInput mouse, Player player, GameController gameController) {
		this.mouse = mouse;
		this.player = player;
		this.gameController = gameController;
	}

	public void tick() {
	}

	public void render(Graphics g, Graphics2D g2d) {
		Font font2 = new Font("SansSerif", Font.PLAIN, 2);
		g.setColor(Color.white);
		g2d.setFont(font2);
		g2d.drawString("FPS: " + Game.current_fps, 1, 10);
		if (Game.DEBUG_MODE) {
			g2d.drawString("X: " + player.getX(), 1, 35);
			g2d.drawString("Y: " + player.getY(), 1, 40);
			// g2d.drawString("BIOME: " + player.getCurrentBiome().toString(), 1, 45);
			//g2d.drawString("SEED: " + gameController.getSeed(), 1, 70);
			drawPlayerStats(g, 1, 50, 30, 3);
		}
	}

	private void drawPlayerStats(Graphics g, int x, int y, int x_valueOffset, int lineHeight) {
		HashMap<PLAYER_STAT, Float> player_stats = player.getPlayerStats();
		int index = 0;
		for(PLAYER_STAT stat_name : player_stats.keySet()) {
			g.drawString(stat_name.toString(), x, y + lineHeight*index);
			g.drawString(player_stats.get(stat_name).toString(), x + x_valueOffset, y + lineHeight*index);
			index++;
		}
	}

	public void renderCam(Graphics g, Graphics2D g2d) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
