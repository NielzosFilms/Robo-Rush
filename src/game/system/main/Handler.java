package game.system.main;

import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.systems.gameObject.*;
import game.system.systems.particles.ParticleSystem;

import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;

public class Handler implements Serializable {
	private transient GameController gameController;
	private transient Camera cam;
	private transient ParticleSystem ps;

	public LinkedList<LinkedList<GameObject>> object_entities = new LinkedList<>();

	public Handler() {
		for (int i = 0; i < 4; i++) {
			this.object_entities.add(new LinkedList<GameObject>());
		}
	}

	public void setRequirements(GameController gameController, Camera cam, ParticleSystem ps) {
		this.gameController = gameController;
		this.cam = cam;
		this.ps = ps;
	}

	public void tick() {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				tempObject.tick();
			}
		}

		for (LinkedList<GameObject> list : all_game_objects) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				tempObject.tick();
			}
		}
	}

	public void render(Graphics g, int width, int height) {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();

		// RENDER

		for (int i=0; i < all_game_objects.size(); i++) {
			LinkedList<GameObject> layer_game_objects = new LinkedList<>(all_game_objects.get(i));

			if(i < object_entities.size()) {
				layer_game_objects.addAll(object_entities.get(i));
			}

			LinkedList<GameObject> y_sorted = new LinkedList<>();
			while (layer_game_objects.size() > 0) {
				GameObject lowest = null;
				int lowestY = 0;
				for (GameObject new_ent : layer_game_objects) {
					// TODO fix the y sorting; it works just looks wierd with the player
					int newY = new_ent.getY() + 16;
					if (newY < lowestY || lowest == null) {
						lowest = new_ent;
						lowestY = newY;
					}
				}
				y_sorted.add(lowest);
				layer_game_objects.remove(lowest);
			}
			for (GameObject entity : y_sorted) {
				if(isEntityOnScreen(entity)) {
					entity.render(g);
				}
			}
		}
	}

	private boolean isEntityOnScreen(GameObject entity) {
		Point screenCoords = Helpers.getScreenCoords(entity.getX(), entity.getY(), this.cam);
		return screenCoords.x > -32 && screenCoords.y > -32  && screenCoords.x < Game.GAME_WIDTH && screenCoords.y < Game.GAME_WIDTH;
	}

	public void addObject(GameObject object) {
		int z_index = object.getZIndex();
		for(int i=object_entities.size(); i<=z_index; i++) {
			this.object_entities.add(new LinkedList<GameObject>());
		}

		this.object_entities.get(z_index).add(object);
	}

	public void removeObject(GameObject object) {
		this.object_entities.get(object.getZIndex()).remove(object);
	}

	/*public void removeTile(Tile tile) {
		// remove tile
	}*/

	public LinkedList<GameObject> getBoundsObjects() {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();
		LinkedList<GameObject> ret = new LinkedList<>();

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject instanceof Bounds && !(tempObject instanceof Trigger)) {
					if(((Bounds) tempObject).getBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		for (LinkedList<GameObject> layer_game_objects : all_game_objects) {
			for (int i = 0; i < layer_game_objects.size(); i++) {
				GameObject tempObject = layer_game_objects.get(i);
				if (tempObject instanceof Bounds && !(tempObject instanceof Trigger)) {
					if(((Bounds) tempObject).getBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		return ret;
	}

	public LinkedList<GameObject> getTriggerObjects() {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();
		LinkedList<GameObject> ret = new LinkedList<>();

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject instanceof Bounds && tempObject instanceof Trigger) {
					if(((Bounds) tempObject).getBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		for (LinkedList<GameObject> layer_game_objects : all_game_objects) {
			for (int i = 0; i < layer_game_objects.size(); i++) {
				GameObject tempObject = layer_game_objects.get(i);
				if (tempObject instanceof Bounds && tempObject instanceof Trigger) {
					if(((Bounds) tempObject).getBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		return ret;
	}

	public LinkedList<GameObject> getObjectsWithIds(ID... args) {
		ID[] ids = new ID[args.length];
		for(int i = 0; i < args.length; i++) {
			ids[i] = args[i];
		}
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();
		LinkedList<GameObject> ret = new LinkedList<>();

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (isInArray(ids, tempObject.getId())) {
					ret.add(tempObject);
				}
			}
		}

		for (LinkedList<GameObject> layer_game_objects : all_game_objects) {
			for (int i = 0; i < layer_game_objects.size(); i++) {
				GameObject tempObject = layer_game_objects.get(i);
				if (isInArray(ids, tempObject.getId())) {
					ret.add(tempObject);
				}
			}
		}
 		return ret;
	}

	public LinkedList<GameObject> getSelectableObjects() {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();
		LinkedList<GameObject> ret = new LinkedList<GameObject>();

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject instanceof Interactable) {
					if(((Interactable) tempObject).getSelectBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		for (LinkedList<GameObject> list : all_game_objects) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject instanceof Interactable) {
					if(((Interactable) tempObject).getSelectBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		return ret;
	}

	public LinkedList<GameObject> getShadowObjects() {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();
		LinkedList<GameObject> ret = new LinkedList<GameObject>();
		ID[] ids = { ID.Tree, ID.Player };

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (isInArray(ids, tempObject.getId())) {
					ret.add(tempObject);
				}
			}
		}

		for (LinkedList<GameObject> list : all_game_objects) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (isInArray(ids, tempObject.getId())) {
					ret.add(tempObject);
				}
			}
		}

		return ret;
	}

	private Boolean isInArray(ID[] arr, ID val) {
		for (ID tmp : arr) {
			if (tmp == val) {
				return true;
			}
		}
		return false;
	}

	public void findAndRemoveObject(GameObject object) {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();

		if(object.getZIndex() < object_entities.size()) {
			object_entities.get(object.getZIndex()).remove(object);
		}

		if(object.getZIndex() < all_game_objects.size()) {
			all_game_objects.get(object.getZIndex()).remove(object);
		}

		if(object instanceof Destroyable) {
			((Destroyable) object).destroyed();
		}
	}

	public boolean objectExistsAtCoords(Point coords) {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();

		for(LinkedList<GameObject> list : object_entities) {
			for(GameObject obj : list) {
				if(obj instanceof Bounds) {
					if(((Bounds) obj).getBounds().contains(coords) || obj.getX() == coords.x && obj.getY() == coords.y) {
						return true;
					}
				} else {
					if(obj.getX() == coords.x && obj.getY() == coords.y) {
						return true;
					}
				}
			}
		}

		for(LinkedList<GameObject> list : all_game_objects) {
			for(GameObject obj : list) {
				if(obj instanceof Bounds) {
					if(((Bounds) obj).getBounds().contains(coords) || obj.getX() == coords.x && obj.getY() == coords.y) {
						return true;
					}
				} else {
					if(obj.getX() == coords.x && obj.getY() == coords.y) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
