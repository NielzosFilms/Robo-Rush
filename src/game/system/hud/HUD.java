package game.system.hud;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

import game.assets.HealthBar;
import game.assets.entities.Player;
import game.system.main.*;
import game.system.inputs.MouseInput;
import game.system.world.World;

public class HUD implements Serializable {
	private DebugHUD debugHUD;
	private transient double velX, velY;
	private transient Handler handler;
	private transient Player player;
	private transient MouseInput mouseInput;
	private transient World world;
	private transient Camera cam;

	private ArrayList<HealthBar> healthBars = new ArrayList<>();

	public HUD() {
		this.debugHUD = new DebugHUD();
	}

	public void setRequirements(Handler handler, Player player, MouseInput mouseInput, World world, Camera cam) {
		this.handler = handler;
		this.player = player;
		this.mouseInput = mouseInput;
		this.world = world;
		this.cam = cam;
		this.debugHUD.setRequirements(mouseInput, player, world);
	}

	public void tick() {
		if(Game.DEDUG_MODE) debugHUD.tick();
	}

	public void renderCam(Graphics g, Graphics2D g2d) {
		for(int i=0; i<healthBars.size(); i++) {
			healthBars.get(i).render(g);
		}

		LinkedList<GameObject> objs = handler.getSelectableObjects();
		for (GameObject obj : objs) {
			if (obj.getSelectBounds() != null) {
				if (mouseInput.mouseOverWorldVar(obj.getSelectBounds().x, obj.getSelectBounds().y,
						obj.getSelectBounds().width, obj.getSelectBounds().height)) {

					if (Helpers.getDistanceBetweenBounds(Game.world.getPlayer().getBounds(), obj.getSelectBounds()) < Game.world.getPlayer().REACH) {
						// TODO nicer selectboxes
						g.setColor(new Color(255, 255, 255, 127));
						g.drawRect(obj.getSelectBounds().x, obj.getSelectBounds().y, obj.getSelectBounds().width,
								obj.getSelectBounds().height);
					}

				}
			}
		}
		if(Game.DEDUG_MODE) debugHUD.renderCam(g, g2d);
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

		if(Game.DEDUG_MODE) debugHUD.render(g, g2d);
	}

	private int getWorldCoordX(int screen_x) {
		return (int) (screen_x - Math.round(-cam.getX()));
	}

	private int getWorldCoordY(int screen_y) {
		return (int) (screen_y - Math.round(-cam.getY()));
	}

	public void mousePressed(MouseEvent e) {
		debugHUD.mousePressed(e);
	}
	public void mouseReleased(MouseEvent e) {
		debugHUD.mouseReleased(e);
	}

	public void addHealthBar(HealthBar healthBar) {
		healthBars.add(healthBar);
	}

	public void removeHealthBar(HealthBar healthBar) {
		healthBars.remove(healthBar);
	}

}
