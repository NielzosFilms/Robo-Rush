package game.system.systems;

import java.awt.*;
import java.util.LinkedList;

import game.assets.entities.player.Player;
import game.assets.entities.bullets.Bullet;
import game.assets.items.Item_Ground;
import game.assets.levels.RoomDoorTrigger;
import game.enums.ID;
import game.system.main.GameController;
import game.system.main.Handler;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Hitable;
import game.system.systems.gameObject.Trigger;

public class Collision {

	private Handler handler;
	private GameController gameController;
	private Player player;

	public Collision() {}

	public void setRequirements(Handler handler, GameController gameController, Player player) {
		this.handler = handler;
		this.gameController = gameController;
		this.player = player;
	}

	public void tick() {
		LinkedList<GameObject> objects_w_bounds = handler.getBoundsObjects();

		LinkedList<GameObject> triggers = handler.getTriggerObjects();

		for(GameObject entity : objects_w_bounds) {
			for(GameObject trigger_object : triggers) {
				Trigger trigger = (Trigger) trigger_object;
				Bounds trigger_bounds = (Bounds) trigger_object;
				if(trigger.triggerCollision()) {
					checkCollisionFor2Entities(entity, trigger_object);
				}
				if(trigger_bounds.getBounds().intersects(player.getBounds())) {
					if(trigger.canTrigger() && !trigger.triggerCollision()) {
						trigger.triggered();
						trigger.setTriggerActive(false);
						break;
					}
				} else if(!trigger.canTrigger()) {
					trigger.setTriggerActive(true);
				}
			}
			for (GameObject entity_2 : objects_w_bounds) {
				checkCollisionFor2Entities(entity, entity_2);
			}
		}

		checkBulletCollision(objects_w_bounds, triggers);

	}

	private void checkBulletCollision(LinkedList<GameObject> objects, LinkedList<GameObject> triggers) {
		for(GameObject bullet : handler.getObjectsWithIds(ID.Bullet)) {
			Bullet bullet_cast = (Bullet) bullet;
			for(GameObject entity : objects) {
				if (!bullet_cast.getHitObjects().contains(entity)) {
					if (bullet_cast.getBounds().intersects(((Bounds) entity).getBounds())) {
						if(entity instanceof Player) {
							if(((Player) entity).canBeHit()) {
								((Player) entity).hit(bullet_cast.getDamage(), 0, 0f, bullet_cast.getCreatedBy());
								bullet_cast.destroy();
							}
						} else {
							if (entity instanceof Hitable) {
								((Hitable) entity).hit(bullet_cast.getDamage(), 0, 0f, bullet_cast.getCreatedBy());
							}
							bullet_cast.destroy();
						}
					}
				}
			}

			for(GameObject trigger : triggers) {
				if(((Trigger)trigger).triggerCollision()) {
					if (!bullet_cast.getHitObjects().contains(trigger)) {
						if (bullet_cast.getBounds().intersects(((Bounds) trigger).getBounds())) {
							if (trigger instanceof Hitable) {
								((Hitable) trigger).hit(bullet_cast.getDamage(), 0, 0f, bullet_cast.getCreatedBy());
							}
							bullet_cast.destroy();
						}
					}
				}
			}
		}
	}

	private void checkCollisionFor2Entities(GameObject entity_1, GameObject entity_2) {
		if(entity_1 == entity_2) return;
		Rectangle ent_1_bounds = ((Bounds)entity_1).getBounds();
		Rectangle ent_2_bounds = ((Bounds)entity_2).getBounds();
		if(ent_1_bounds.intersects(ent_2_bounds)) {
			Bounds ent_1 = (Bounds) entity_1;
			if(ent_1.getTopBounds() != null && ent_1.getBottomBounds() != null && ent_1.getLeftBounds() != null && ent_1.getRightBounds() != null) {
				if(!ent_1.getTopBounds().intersects(ent_2_bounds) ||
						!ent_1.getBottomBounds().intersects(ent_2_bounds) ||
						!ent_1.getLeftBounds().intersects(ent_2_bounds) ||
						!ent_1.getRightBounds().intersects(ent_2_bounds)) {
					if (ent_1.getTopBounds().intersects(ent_2_bounds)) {
						entity_1.setY(entity_1.getY() + 1);
						entity_1.setVelY(0);
					}
					if (ent_1.getBottomBounds().intersects(ent_2_bounds)) {
						entity_1.setY(entity_1.getY() - 1);
						entity_1.setVelY(0);
					}
					if (ent_1.getLeftBounds().intersects(ent_2_bounds)) {
						entity_1.setX(entity_1.getX() + 1);
						entity_1.setVelX(0);
					}
					if (ent_1.getRightBounds().intersects(ent_2_bounds)) {
						entity_1.setX(entity_1.getX() - 1);
						entity_1.setVelX(0);
					}
				}
			}
		}
	}
}
