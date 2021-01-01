package game.system.systems;

import java.awt.*;
import java.util.LinkedList;

import game.assets.entities.Player;
import game.enums.DIRECTIONS;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.main.Handler;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Pushable;
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
		LinkedList<Rectangle> other_bounds = new LinkedList<>();
		for(Chunk chunk : world.getChunksOnScreen()) {
			other_bounds.addAll(chunk.getAllTileBounds());
		}
		/*for(GameObject entity : objects_w_bounds) {
			//if(entity instanceof Pushable) continue;
			//all_bounds.add(((game.system.systems.gameObject.Collision)entity).getBounds());
		}*/

		for(GameObject entity : objects_w_bounds) {
			if(entity instanceof Pushable) {
				if(player.getBounds().intersects(((Bounds)entity).getBounds())) {
					//((Pushable) entity).push(DIRECTIONS.down);
				}
			}
		}

		for(GameObject entity : objects_w_bounds) {
			for (GameObject entity_2 : objects_w_bounds) {
				checkCollisionFor2Entities(entity, entity_2);
				//checkCollisionForMovingEntities(entity, entity_2);
				// wierd stuff happens
				//checkCollisionForGameObject(bounds, entity);
			}
			for (Rectangle bounds : other_bounds) {
				checkCollisionForRectangle(entity, bounds);
			}
		}
/*
		for (Rectangle bounds : all_bounds) {
			checkBoundWithPlayer(bounds);
		}*/

	}

	private void checkCollisionFor2Entities(GameObject entity_1, GameObject entity_2) {
		if(entity_1 == entity_2) return;
		Rectangle ent_1_bounds = ((Bounds)entity_1).getBounds();
		Rectangle ent_2_bounds = ((Bounds)entity_2).getBounds();
		if(ent_1_bounds.intersects(ent_2_bounds)) {
			Bounds ent_1 = (Bounds) entity_1;
			if(ent_1.getTopBounds() != null && ent_1.getBottomBounds() != null && ent_1.getLeftBounds() != null && ent_1.getRightBounds() != null) {
				if(ent_1.getTopBounds().intersects(ent_2_bounds)) {
					entity_1.setY(entity_1.getY() + 1);
					entity_1.setVelY(0);
				}
				if(ent_1.getBottomBounds().intersects(ent_2_bounds)) {
					entity_1.setY(entity_1.getY() - 1);
					entity_1.setVelY(0);
				}
				if(ent_1.getLeftBounds().intersects(ent_2_bounds)) {
					entity_1.setX(entity_1.getX() + 1);
					entity_1.setVelX(0);
				}
				if(ent_1.getRightBounds().intersects(ent_2_bounds)) {
					entity_1.setX(entity_1.getX() - 1);
					entity_1.setVelX(0);
				}
			}
		}
	}

	private void checkCollisionForRectangle(GameObject entity_1, Rectangle bounds) {
		Rectangle ent_1_bounds = ((Bounds)entity_1).getBounds();
		if(ent_1_bounds == bounds) return;
		if(bounds == null || ent_1_bounds == null) return;
		if(bounds.x == ent_1_bounds.x && bounds.y == ent_1_bounds.y && bounds.width == ent_1_bounds.width && bounds.height == ent_1_bounds.height) return;
		if(ent_1_bounds.intersects(bounds)) {
			Bounds ent_1 = (Bounds) entity_1;
			if(ent_1.getTopBounds() != null && ent_1.getBottomBounds() != null && ent_1.getLeftBounds() != null && ent_1.getRightBounds() != null) {
				if(ent_1.getTopBounds().intersects(bounds)) {
					entity_1.setY(entity_1.getY() + 1);
					entity_1.setVelY(0);
				}
				if(ent_1.getBottomBounds().intersects(bounds)) {
					entity_1.setY(entity_1.getY() - 1);
					entity_1.setVelY(0);
				}
				if(ent_1.getLeftBounds().intersects(bounds)) {
					entity_1.setX(entity_1.getX() + 1);
					entity_1.setVelX(0);
				}
				if(ent_1.getRightBounds().intersects(bounds)) {
					entity_1.setX(entity_1.getX() - 1);
					entity_1.setVelX(0);
				}
			}
		}
	}

}
