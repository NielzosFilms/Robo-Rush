package game.main;

import java.awt.Graphics;
import java.util.LinkedList;

import game.world.World;

public class Handler {

	public LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	//chunk needs to have an x and y asswell
	// maybe make a chunk class?
	// all of this is to optimize te game runtime
	public LinkedList<LinkedList<GameObject>> chunks = new LinkedList<LinkedList<GameObject>>(); // add everything to their corresponding chunks and loop throught the chunks
	
	public void tick(World world) {
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			if(tempObject.getId() == ID.Enemy) {
				int x = tempObject.getX();
				int y = tempObject.getY();
				if(world.getChunkPointWithCoords(x, y) != null) { //adds enemy to a chunk to be unloaded
					world.chunks.get(world.getChunkPointWithCoords(x, y)).entities.get(0).add(tempObject);
					object.remove(i);
				}
			}else {
				tempObject.tick();
			}
		}
	}
	
	public void render(Graphics g, int camX, int camY, int width, int height) {
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			if(tempObject.getX()+16 > camX && tempObject.getY()+16 > camY && tempObject.getX()-16 < camX+width && tempObject.getY()-16 < camY+height) {
				tempObject.render(g);
			}
		}
		
		//chunk looping
		for(LinkedList<GameObject> chunk : chunks) {
			for(GameObject object : chunk) {
				object.render(g);
			}
		}
		
	}
	
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
	
}
