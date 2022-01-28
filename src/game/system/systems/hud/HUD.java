package game.system.systems.hud;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

import game.assets.entities.player.Character_Robot;
import game.assets.entities.player.Player;
import game.assets.levels.def.Level;
import game.system.helpers.Helpers;
import game.system.main.*;
import game.system.inputs.MouseInput;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.HUD_Component;
import game.system.systems.gameObject.HUD_Rendering;
import game.system.systems.gameObject.Interactable;
import game.system.systems.levelGeneration.Room;
import game.textures.COLOR_PALETTE;

public class HUD implements Serializable {
	private DebugHUD debugHUD;
	private transient double velX, velY;
	private transient Handler handler;
	private transient Player player;
	private transient MouseInput mouseInput;
	private transient GameController gameController;
	private transient Camera cam;

	private LinkedList<LinkedList<GameObject>> objects_on_hud = new LinkedList<>();
	private LinkedList<LinkedList<GameObject>> objects_on_hud_static = new LinkedList<>();

	private Selection selection = new Selection();

	public HUD() {
		this.debugHUD = new DebugHUD();
	}

	public void setRequirements(Handler handler, Player player, MouseInput mouseInput, GameController gameController, Camera cam) {
		this.handler = handler;
		this.player = player;
		this.mouseInput = mouseInput;
		this.gameController = gameController;
		this.cam = cam;
		this.debugHUD.setRequirements(mouseInput, player, gameController);
	}

	public void tick() {
		objects_on_hud = new LinkedList<>();
		objects_on_hud_static = new LinkedList<>();
		for(var layer : gameController.getAllGameObjects()) {
			for(GameObject object : layer) {
				if(object instanceof HUD_Rendering) {
					for(GameObject hud_object : ((HUD_Rendering) object).getHudObjects()) {
						if(((HUD_Component)hud_object).isStatic()) {
							addHudObjectStatic(hud_object);
						} else {
							addHudObject(hud_object);
						}
					}
				}
			}
		}
		if(Game.DEBUG_MODE) debugHUD.tick();
		selection.tick();
	}

	public void renderCam(Graphics g, Graphics2D g2d) {
		for(LinkedList<GameObject> z_list : objects_on_hud) {
			for(GameObject object : z_list) {
				object.render(g);
			}
		}

		LinkedList<GameObject> objs = handler.getSelectableObjects();
		for (GameObject obj : objs) {
			if (((Interactable)obj).getSelectBounds() != null) {
				if (mouseInput.mouseOverWorldVar(((Interactable)obj).getSelectBounds().x, ((Interactable)obj).getSelectBounds().y,
						((Interactable)obj).getSelectBounds().width, ((Interactable)obj).getSelectBounds().height)) {
//					if (Helpers.getDistanceBetweenBounds(Game.gameController.getPlayer().getBounds(), ((Interactable)obj).getSelectBounds()) < Game.gameController.getPlayer().REACH) {
//						selection.renderSelection(g, ((Interactable) obj).getSelectBounds(), 2);
//						break;
//					}
				}
			}
		}
		if(Game.DEBUG_MODE) debugHUD.renderCam(g, g2d);
	}

	public void render(Graphics g, Graphics2D g2d) {
		for(LinkedList<GameObject> layer : objects_on_hud_static) {
			for(GameObject object : layer) {
				object.render(g);
			}
		}

		drawPlayerHud(g, this.player, new Point(0, Game.getGameSize().y - 20));
		drawMiniMap(g, new Point(Game.getGameSize().x - 8*4 - 20, 5));

		if(Game.DEBUG_MODE) debugHUD.render(g, g2d);
	}

	private void drawPlayerHud(Graphics g, Player player, Point drawPos) {
		if(player instanceof Character_Robot) {
			Character_Robot robot = (Character_Robot) player;
			g.setColor(COLOR_PALETTE.oak.color);
			g.fillRect(drawPos.x, drawPos.y, 50, 4);
			g.setColor(COLOR_PALETTE.yellow.color);
			g.fillRect(drawPos.x, drawPos.y, (int) (50 * robot.getEnergyPercent()), 4);
			g.setColor(COLOR_PALETTE.dark_oak.color);
			g.drawRect(drawPos.x, drawPos.y, 50, 4);
		}
	}

	private void drawMiniMap(Graphics g, Point draw_pos) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
		g.setColor(Color.black);
		g.fillRect(draw_pos.x-2, draw_pos.y-2, 8*5 + 8, 8*5 + 8);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		if(Game.DEBUG_MODE) {
			HashMap<Point, Integer> cells = Game.gameController.getActiveLevel().getGenerator().getDungeonInCellsMinimal();
			for(Point cell : cells.keySet()) {
				if(cells.get(cell) == 0) {
					g.setColor(COLOR_PALETTE.yellow.color);
					g.drawLine(draw_pos.x + cell.x, draw_pos.y + cell.y, draw_pos.x + cell.x, draw_pos.y + cell.y);
				}
			}
		}

		LinkedList<Room> rooms = Game.gameController.getActiveLevel().getGenerator().getRooms();
		for(Room room : rooms) {
			g.setColor(COLOR_PALETTE.dark_green.color);
			Rectangle rect = room.rect;
			Rectangle worldRect = new Rectangle(rect.x * 16, rect.y * 16, rect.width * 16, rect.height * 16);
			if(worldRect.contains(new Point(Game.gameController.getPlayer().getX(), Game.gameController.getPlayer().getY()))) {
				g.setColor(COLOR_PALETTE.light_green.color);
			}
			g.fillRect(draw_pos.x + rect.x, draw_pos.y + rect.y, rect.width - 1, rect.height - 1);
		}
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

	private void addHudObject(GameObject object) {
		int z_index = object.getZIndex();
		for(int i=objects_on_hud.size(); i<=z_index; i++) {
			this.objects_on_hud.add(new LinkedList<GameObject>());
		}

		this.objects_on_hud.get(z_index).add(object);
	}

	private void addHudObjectStatic(GameObject object) {
		int z_index = object.getZIndex();
		for(int i=objects_on_hud_static.size(); i<=z_index; i++) {
			this.objects_on_hud_static.add(new LinkedList<GameObject>());
		}

		this.objects_on_hud_static.get(z_index).add(object);
	}

}
