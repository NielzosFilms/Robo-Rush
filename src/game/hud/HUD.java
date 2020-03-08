package game.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
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
	
	public void renderCam(Graphics g, Graphics2D g2d) {
		LinkedList<GameObject> objs = handler.getSelectableObjects(world);
		for(GameObject obj : objs) {
			if(obj.getSelectBounds() != null) {
				if(mouseInput.mouseOverWorldVar(obj.getSelectBounds().x, obj.getSelectBounds().y, 
						obj.getSelectBounds().width, obj.getSelectBounds().height)) {
					g.setColor(new Color(255, 255, 255, 127));
					g.drawRect(obj.getSelectBounds().x, obj.getSelectBounds().y, 
							obj.getSelectBounds().width, obj.getSelectBounds().height);
				}	
			}
		}
	}
	
	public void render(Graphics g, Graphics2D g2d) {
		Font font = new Font("SansSerif", Font.PLAIN, 3);
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics(font);
		
		String version = Game.VERSION;
		String name = "NielzosFilms";
		
		g.setColor(Color.black);
		g2d.drawString(version, (Game.WIDTH-fontMetrics.stringWidth(version)), fontMetrics.getAscent());
		g2d.drawString(name, (Game.WIDTH-fontMetrics.stringWidth(name)), fontMetrics.getHeight()+fontMetrics.getAscent());
		
		Font font2 = new Font("SansSerif", Font.PLAIN, 4);
		g2d.setFont(font2);
		g2d.drawString("X: "+player.getX(), 1, 35);
		g2d.drawString("Y: "+player.getY(), 1, 40);
		g2d.drawString("FPS: "+Game.current_fps, 1, 65);
		
		
		/*g.setColor(new Color(0, 0, 0, 100));
		g.fillArc(inventory.getHotbarX()-20, inventory.getHotbarY(), 20, 20, 90, -360);*/
		
		drawPlayerStats(g);
		
		inventory.render(g);
	}
	
	private void drawPlayerStats(Graphics g) {
		int hotbar_x = inventory.getHotbarX();
		int hotbar_y = inventory.getHotbarY();
		g.setColor(new Color(255, 46, 46));
		g.fillRect(hotbar_x, hotbar_y-4, player.getHealth()*(20*inventory.getSizeX())/100, 3);
		g.drawRect(hotbar_x, hotbar_y-4, player.getHealth()*(20*inventory.getSizeX())/100, 3);
		
		g.setColor(new Color(255, 175, 46));
		g.fillRect(hotbar_x-6, hotbar_y-player.getFood()*20/100+20, 2, player.getFood()*20/100);
		
		g.setColor(new Color(46, 203, 255));
		g.fillRect(hotbar_x-9, hotbar_y-player.getWater()*20/100+20, 2, player.getWater()*20/100);
	}
	
	private int getWorldCoordX(int screen_x) {
		return (int) (screen_x - Math.round(-cam.getX()));
	}
	private int getWorldCoordY(int screen_y) {
		return (int) (screen_y - Math.round(-cam.getY()));
	}
	
}
