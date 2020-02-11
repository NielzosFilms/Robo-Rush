package game.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game.main.Game;
import game.main.GameObject;

public class HUD {
	
	private double velX, velY;

	public void tick(GameObject player) {
		this.velX = player.getVelX();
		this.velY = player.getVelY();
	}
	
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(Color.gray);
		g.fillRect(1, 1, 100, 10);
		
		Font font = new Font("Serif", Font.PLAIN, 2);
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics(font);
		
		String version = Game.VERSION;
		String name = "NielzosFilms";
		
		g2d.drawString(version, (Game.WIDTH-fontMetrics.stringWidth(version)), fontMetrics.getAscent());
		g2d.drawString(name, (Game.WIDTH-fontMetrics.stringWidth(name)), fontMetrics.getHeight()+fontMetrics.getAscent());
		
		Font font2 = new Font("Serif", Font.PLAIN, 4);
		g2d.setFont(font2);
		g2d.drawString("velX: "+velX, 1, 15);
		g2d.drawString("velY: "+velY, 1, 20);
	}
	
}
