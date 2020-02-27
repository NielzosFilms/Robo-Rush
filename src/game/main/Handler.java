package game.main;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {

	public LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	public LinkedList<GameObject> object_noTick = new LinkedList<GameObject>();
	
	public void tick() {
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			tempObject.tick();
		}
	}
	
	public void render(Graphics g, int camX, int camY, int width, int height) {
		for(int i = 0; i < object_noTick.size(); i++) {
			GameObject tempObject = object_noTick.get(i);
			if(tempObject.getX()+16 > camX && tempObject.getY()+16 > camY && tempObject.getX()-16 < camX+width && tempObject.getY()-16 < camY+height) {
				tempObject.render(g);
			}
		}
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			if(tempObject.getX()+16 > camX && tempObject.getY()+16 > camY && tempObject.getX()-16 < camX+width && tempObject.getY()-16 < camY+height) {
				tempObject.render(g);
			}
		}
		
	}
	
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
	
	public void addObjectNoTick(GameObject object) {
		this.object_noTick.add(object);
	}
	public void removeObjectNoTick(GameObject object) {
		this.object_noTick.remove(object);
	}
	
}
