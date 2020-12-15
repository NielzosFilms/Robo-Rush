package game.system.systems;

import java.awt.*;
import java.util.LinkedList;

import game.assets.entities.Player;
import game.system.main.Handler;
import game.system.world.Chunk;
import game.system.world.World;

public class Collision {

	private Handler handler;
	private World world;
	private Player player;

	public Collision() {}

	public void setRequirements(Handler handler, World world, Player player) {
		this.handler = handler;
		this.world = world;
		this.player = player;
	}

	public void tick() {
		LinkedList<GameObject> objects_w_bounds = handler.getBoundsObjects();
		LinkedList<Rectangle> all_bounds = new LinkedList<>();
		for(Chunk chunk : world.getChunksOnScreen()) {
			all_bounds.addAll(chunk.getAllTileBounds());
		}
		for(GameObject entity : objects_w_bounds) {
			all_bounds.add(entity.getBounds());
		}

		for (Rectangle bounds : all_bounds) {
			checkBoundWithPlayer(bounds);
		}
		for(GameObject entity : objects_w_bounds) {
			for(Rectangle bounds : all_bounds) {
				// only do this for objects that can/will move
				//checkCollisionForGameObject(bounds, entity);
			}
		}
	}

	private void checkBoundWithPlayer(Rectangle bounds) {
		if(bounds == player.getBounds()) return;
		if (bounds.intersects(player.getBounds())) {
			int player_x = player.getBounds().x;
			int player_y = player.getBounds().y;
			int player_x_diff = player.getBounds().x - player.getX();
			int player_y_diff = player.getBounds().y - player.getY();
			int player_cenX = player_x + (player.getBounds().width / 2);
			int player_cenY = player_y + (player.getBounds().height / 2);
			int player_bottomY = player_y + player.getBounds().height;
			int player_rightX = player_x + player.getBounds().width;
			int obj_x = bounds.x;
			int obj_y = bounds.y;
			int obj_cenX = obj_x + (bounds.width / 2);
			int obj_cenY = obj_y + (bounds.height / 2);
			int obj_bottomY = obj_y + bounds.height;
			int obj_rightX = obj_x + bounds.width;

			if (player_cenY < obj_cenY && player_bottomY < obj_y + 4) {
				player.setY(obj_y - player.getBounds().height - player_y_diff);
			} else if (player_cenY > obj_cenY && player_y > obj_bottomY - 4) {
				player.setY(obj_y + bounds.height - player_y_diff);
			} else if (player_cenX < obj_cenX && player_rightX < obj_x + 4) {
				player.setX(obj_x - player.getBounds().width - player_x_diff);
			} else if (player_cenX > obj_cenX && player_x > obj_rightX - 4) {
				player.setX(obj_x + bounds.width - player_x_diff);
			}
		}
	}

	private void checkCollisionForGameObject(Rectangle bounds, GameObject entity) {
		//wierd stuff happens when moving entities touch
		if(bounds == entity.getBounds()) return;
		if (bounds.intersects(entity.getBounds())) {
			int player_x = entity.getBounds().x;
			int player_y = entity.getBounds().y;
			int player_x_diff = entity.getBounds().x - entity.getX();
			int player_y_diff = entity.getBounds().y - entity.getY();
			int player_cenX = player_x + (entity.getBounds().width / 2);
			int player_cenY = player_y + (entity.getBounds().height / 2);
			int player_bottomY = player_y + entity.getBounds().height;
			int player_rightX = player_x + entity.getBounds().width;
			int obj_x = bounds.x;
			int obj_y = bounds.y;
			int obj_cenX = obj_x + (bounds.width / 2);
			int obj_cenY = obj_y + (bounds.height / 2);
			int obj_bottomY = obj_y + bounds.height;
			int obj_rightX = obj_x + bounds.width;

			if (player_cenY < obj_cenY && player_bottomY < obj_y + 4) {
				entity.setY(obj_y - entity.getBounds().height - player_y_diff);
			} else if (player_cenY > obj_cenY && player_y > obj_bottomY - 4) {
				entity.setY(obj_y + bounds.height - player_y_diff);
			} else if (player_cenX < obj_cenX && player_rightX < obj_x + 4) {
				entity.setX(obj_x - entity.getBounds().width - player_x_diff);
			} else if (player_cenX > obj_cenX && player_x > obj_rightX - 4) {
				entity.setX(obj_x + bounds.width - player_x_diff);
			}
		}
	}

}
