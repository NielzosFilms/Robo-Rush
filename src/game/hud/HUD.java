package game.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game.main.Game;

public class HUD {

	public void tick() {
		
	}
	
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(Color.gray);
		g.fillRect(15, 15, 200, 32);
		
		Font font = new Font("Serif", Font.PLAIN, 12);
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics(font);
		String version = Game.VERSION;
		String name = "NielzosFilms";
		g2d.drawString(version, (Game.WIDTH-fontMetrics.stringWidth(version))-9, 0+fontMetrics.getAscent());
		g2d.drawString(name, (Game.WIDTH-fontMetrics.stringWidth(name))-9, fontMetrics.getHeight()+fontMetrics.getAscent());
	}
	
}
