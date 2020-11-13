package game.system.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import game.assets.entities.Player;
import game.system.main.Camera;
import game.system.main.Game;
import game.system.main.GameObject;
import game.system.main.Handler;
import game.system.inputs.MouseInput;
import game.system.world.World;

public class HUD {

	private double velX, velY;
	private Handler handler;
	private Player player;
	private MouseInput mouseInput;
	private World world;
	private Camera cam;

	public HUD() {}

	public void setRequirements(Handler handler, Player player, MouseInput mouseInput, World world, Camera cam) {
		this.handler = handler;
		this.player = player;
		this.mouseInput = mouseInput;
		this.world = world;
		this.cam = cam;
	}

	public void tick() {
		// get object when hovered over

	}

	public void renderCam(Graphics g, Graphics2D g2d) {
		LinkedList<GameObject> objs = handler.getSelectableObjects(world);
		for (GameObject obj : objs) {
			if (obj.getSelectBounds() != null) {
				if (mouseInput.mouseOverWorldVar(obj.getSelectBounds().x, obj.getSelectBounds().y,
						obj.getSelectBounds().width, obj.getSelectBounds().height)) {
					// TODO nicer selectboxes
					g.setColor(new Color(255, 255, 255, 127));
					g.drawRect(obj.getSelectBounds().x, obj.getSelectBounds().y, obj.getSelectBounds().width,
							obj.getSelectBounds().height);
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
		g2d.drawString(version, (Game.WIDTH - fontMetrics.stringWidth(version)), fontMetrics.getAscent());
		g2d.drawString(name, (Game.WIDTH - fontMetrics.stringWidth(name)),
				fontMetrics.getHeight() + fontMetrics.getAscent());

		Font font2 = new Font("SansSerif", Font.PLAIN, 4);
		g2d.setFont(font2);
		g2d.drawString("FPS: " + Game.current_fps, 1, 10);
		if (Game.DEDUG_MODE) {
			g2d.drawString("X: " + player.getX(), 1, 35);
			g2d.drawString("Y: " + player.getY(), 1, 40);
			// g2d.drawString("BIOME: " + player.getCurrentBiome().toString(), 1, 45);
			g2d.drawString("SEED: " + World.seed, 1, 70);
		}
	}

	private int getWorldCoordX(int screen_x) {
		return (int) (screen_x - Math.round(-cam.getX()));
	}

	private int getWorldCoordY(int screen_y) {
		return (int) (screen_y - Math.round(-cam.getY()));
	}

}
