package game.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import game.entities.Player;
import game.inventory.Inventory;
import game.main.Camera;
import game.main.Game;
import game.main.GameObject;
import game.main.Handler;
import game.main.MouseInput;
import game.world.World;

public class HUD {
	
	private double velX, velY;
	private Handler handler;
	private Player player;
	private Inventory inventory;
	private MouseInput mouseInput;
	private World world;
	private Camera cam;
	
	public HUD(Handler handler, Player player, Inventory inventory) {
		this.handler = handler;
		this.player = player;
		this.inventory = inventory;
	}
	
	public void setCam(Camera cam) {
		this.cam = cam;
	}
	
	public void setMouseInput(MouseInput mouseInput) {
		this.mouseInput = mouseInput;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}

	public void tick() {
		//get object when hovered over
		
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
		g2d.drawString("FPS: "+Game.current_fps, 1, 65);
		
		LinkedList<GameObject> objs = handler.getSelectableObjects(world);
		for(GameObject obj : objs) {
			if(obj.getSelectBounds() != null) {
				if(mouseInput.mouseOverWorldVar(obj.getSelectBounds().x, obj.getSelectBounds().y, 
						obj.getSelectBounds().width, obj.getSelectBounds().height)) {
					g.setColor(new Color(255, 255, 255, 127));
					g.drawRect(getWorldCoordX(obj.getSelectBounds().x), getWorldCoordY(obj.getSelectBounds().y), 
							obj.getSelectBounds().width, obj.getSelectBounds().height);
				}	
			}
		}
		
		inventory.render(g);
	}
	
	private int getWorldCoordX(int screen_x) {
		return (int) (screen_x - -cam.getX());
	}
	private int getWorldCoordY(int screen_y) {
		return (int) (screen_y - -cam.getY());
	}
	
}
