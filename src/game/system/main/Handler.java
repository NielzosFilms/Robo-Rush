package game.system.main;

import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.systems.gameObject.*;
import game.system.systems.particles.ParticleSystem;

import java.awt.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;

public class Handler {
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
        var all_game_objects = gameController.getAllGameObjects();

        for (LinkedList<GameObject> list : object_entities) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).tick();
            }
        }

        for (var list : all_game_objects) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).tick();
            }
        }
    }

    public void render(Graphics g, int width, int height) {
        var all_game_objects = gameController.getAllGameObjects();

        // RENDER

        for (int i = 0; i < all_game_objects.size(); i++) {
            LinkedList<GameObject> layer_game_objects = new LinkedList<>(all_game_objects.get(i));

            if (i < object_entities.size()) {
                layer_game_objects.addAll(object_entities.get(i));
            }

            LinkedList<GameObject> y_sorted = new LinkedList<>();
            while (layer_game_objects.size() > 0) {
                Optional<GameObject> lowest = layer_game_objects.stream().min(Comparator.comparing(GameObject::getY));
                y_sorted.add(lowest.get());
                layer_game_objects.remove(lowest.get());
            }
            for(GameObject ent : y_sorted) {
                if(Helpers.isEntityOnScreen(ent, this.cam)) ent.render(g);
            }
        }
    }

    public void addObject(GameObject object) {
        int z_index = object.getZIndex();
        for (int i = object_entities.size(); i <= z_index; i++) {
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
        var all_game_objects = gameController.getAllGameObjects();
        LinkedList<GameObject> ret = new LinkedList<>();

        for (LinkedList<GameObject> list : object_entities) {
            for (int i = 0; i < list.size(); i++) {
                GameObject tempObject = list.get(i);
                if (tempObject instanceof Bounds && !(tempObject instanceof Trigger)) {
                    if (((Bounds) tempObject).getBounds() != null) {
                        ret.add(tempObject);
                    }
                }
            }
        }

        for (var layer_game_objects : all_game_objects) {
            for (int i = 0; i < layer_game_objects.size(); i++) {
                GameObject tempObject = layer_game_objects.get(i);
                if (tempObject instanceof Bounds && !(tempObject instanceof Trigger)) {
                    if (((Bounds) tempObject).getBounds() != null) {
                        ret.add(tempObject);
                    }
                }
            }
        }

        return ret;
    }

    public LinkedList<GameObject> getTriggerObjects() {
        var all_game_objects = gameController.getAllGameObjects();
        LinkedList<GameObject> ret = new LinkedList<>();

        for (LinkedList<GameObject> list : object_entities) {
            for (int i = 0; i < list.size(); i++) {
                GameObject tempObject = list.get(i);
                if (tempObject instanceof Bounds && tempObject instanceof Trigger) {
                    if (((Bounds) tempObject).getBounds() != null) {
                        ret.add(tempObject);
                    }
                }
            }
        }

        for (var layer_game_objects : all_game_objects) {
            for (int i = 0; i < layer_game_objects.size(); i++) {
                GameObject tempObject = layer_game_objects.get(i);
                if (tempObject instanceof Bounds && tempObject instanceof Trigger) {
                    if (((Bounds) tempObject).getBounds() != null) {
                        ret.add(tempObject);
                    }
                }
            }
        }

        return ret;
    }

    public LinkedList<GameObject> getObjectsWithIds(ID... args) {
        LinkedList<ID> ids = new LinkedList<>();
        for (ID arg : args) {
            ids.push(arg);
        }
        var all_game_objects = gameController.getAllGameObjects();
        LinkedList<GameObject> ret = new LinkedList<>();

        for (LinkedList<GameObject> list : object_entities) {
            for (int i = 0; i < list.size(); i++) {
                GameObject tempObject = list.get(i);
                if (isInArray(ids, tempObject.getId())) {
                    ret.add(tempObject);
                }
            }
        }

        for (var layer_game_objects : all_game_objects) {
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
        var all_game_objects = gameController.getAllGameObjects();
        LinkedList<GameObject> ret = new LinkedList<GameObject>();

        for (LinkedList<GameObject> list : object_entities) {
            for (int i = 0; i < list.size(); i++) {
                GameObject tempObject = list.get(i);
                if (tempObject instanceof Interactable) {
                    if (((Interactable) tempObject).getSelectBounds() != null) {
                        ret.add(tempObject);
                    }
                }
            }
        }

        for (var list : all_game_objects) {
            for (int i = 0; i < list.size(); i++) {
                GameObject tempObject = list.get(i);
                if (tempObject instanceof Interactable) {
                    if (((Interactable) tempObject).getSelectBounds() != null) {
                        ret.add(tempObject);
                    }
                }
            }
        }

        return ret;
    }

    public LinkedList<GameObject> getShadowObjects() {
        var all_game_objects = gameController.getAllGameObjects();
        LinkedList<GameObject> ret = new LinkedList<GameObject>();
        LinkedList<ID> ids = new LinkedList<ID>();
        ids.push(ID.Tree);
        ids.push(ID.Player);

        for (LinkedList<GameObject> list : object_entities) {
            for (int i = 0; i < list.size(); i++) {
                GameObject tempObject = list.get(i);
                if (isInArray(ids, tempObject.getId())) {
                    ret.add(tempObject);
                }
            }
        }

        for (var list : all_game_objects) {
            for (int i = 0; i < list.size(); i++) {
                GameObject tempObject = list.get(i);
                if (isInArray(ids, tempObject.getId())) {
                    ret.add(tempObject);
                }
            }
        }

        return ret;
    }

    private Boolean isInArray(LinkedList<ID> arr, ID val) {
        for (ID tmp : arr) {
            if (tmp == val) {
                return true;
            }
        }
        return false;
    }

    public void findAndRemoveObject(GameObject object) {
        if (object.getId() == ID.Enemy) Logger.print("enemy removed");
        var all_game_objects = gameController.getAllGameObjects();

        if (object.getZIndex() < object_entities.size()) {
            object_entities.get(object.getZIndex()).remove(object);
        }

        if (object.getZIndex() < all_game_objects.size()) {
            all_game_objects.get(object.getZIndex()).remove(object);
        }

        gameController.getActiveLevel().getObjects().remove(object);

        if (object instanceof Destroyable) {
            ((Destroyable) object).destroyed();
        }
    }

    public boolean objectExistsAtCoords(Point coords) {
        var all_game_objects = gameController.getAllGameObjects();

        for (LinkedList<GameObject> list : object_entities) {
            for (GameObject obj : list) {
                if (obj instanceof Bounds) {
                    if (((Bounds) obj).getBounds().contains(coords) || obj.getX() == coords.x && obj.getY() == coords.y) {
                        return true;
                    }
                } else {
                    if (obj.getX() == coords.x && obj.getY() == coords.y) {
                        return true;
                    }
                }
            }
        }

        for (var list : all_game_objects) {
            for (var obj : list) {
                if (obj instanceof Bounds) {
                    if (((Bounds) obj).getBounds().contains(coords) || obj.getX() == coords.x && obj.getY() == coords.y) {
                        return true;
                    }
                } else {
                    if (obj.getX() == coords.x && obj.getY() == coords.y) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
