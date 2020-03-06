package game.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game.entities.Player;
import game.inventory.Inventory;
import game.main.Game;
import game.main.Handler;

public class HUD {
	
	private double velX, velY;
	private Handler handler;
	private Player player;
	private Inventory inventory;
	
	public HUD(Handler handler, Player player, Inventory inventory) {
		this.handler = handler;
		this.player = player;
		this.inventory = inventory;
	}

	public void tick() {
		/*for(int i = 0; i < handler.object.size(); i++) {
			if(handler.object.get(i).getId() == ID.Player) {
				this.player = (Player)handler.object.get(i);
			}
		}*/
	}
	
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(Color.gray);
		g.fillRect(1, 1, 100, 10);
		
		Font font = new Font("Serif", Font.PLAIN, 3);
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics(font);
		
		String version = Game.VERSION;
		String name = "NielzosFilms";
		
		g2d.drawString(version, (Game.WIDTH-fontMetrics.stringWidth(version)), fontMetrics.getAscent());
		g2d.drawString(name, (Game.WIDTH-fontMetrics.stringWidth(name)), fontMetrics.getHeight()+fontMetrics.getAscent());
		
		Font font2 = new Font("Serif", Font.PLAIN, 4);
		g2d.setFont(font2);
		g.setColor(Color.black);
		g2d.drawString("accX: "+player.accX, 1, 15);
		g2d.drawString("accY: "+player.accY, 1, 20);
		g2d.drawString("velX: "+player.getVelX(), 1, 25);
		g2d.drawString("velY: "+player.getVelY(), 1, 30);
		g2d.drawString("X: "+player.getX(), 1, 35);
		g2d.drawString("Y: "+player.getY(), 1, 40);
		g2d.drawString("onGround: "+player.onGround, 1, 45);
		g2d.drawString("direction: "+player.direction, 1, 50);
		g2d.drawString("jumping: "+player.jumping, 1, 55);
		g2d.drawString("falling: "+player.falling, 1, 60);
		g2d.drawString("FPS: "+Game.current_fps, 1, 65);
	}
	
}
