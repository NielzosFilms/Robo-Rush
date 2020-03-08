package game.main;

import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import game.entities.particles.Particle;
import game.entities.particles.ParticleSystem;
import game.items.Item;
import game.world.Chunk;
import game.world.World;

public class Handler {

	public LinkedList<LinkedList<GameObject>> object = new LinkedList<LinkedList<GameObject>>();
	
	//chunk needs to have an x and y asswell
	// maybe make a chunk class?
	// all of this is to optimize te game runtime
	public LinkedList<LinkedList<GameObject>> chunks = new LinkedList<LinkedList<GameObject>>(); // add everything to their corresponding chunks and loop throught the chunks
	
	public Handler() {
		for(int i = 0;i<4;i++) {
			this.object.add(new LinkedList<GameObject>());
		}
	}
	
	public void tick(World world) {
		for(LinkedList<GameObject> list : object) {
			for(int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if(tempObject.getId() != ID.Player) {
					int x = tempObject.getX();
					int y = tempObject.getY();
					if(world.getChunkPointWithCoords(x, y) != null) { //adds enemy to a chunk to be unloaded
						world.chunks.get(world.getChunkPointWithCoords(x, y)).entities.get(0).add(tempObject);
						list.remove(i);
					}
				}else {
					tempObject.tick();
				}
			}
		}
	}
	
	public void render(Graphics g, int camX, int camY, int width, int height, World world, ParticleSystem ps) {
		LinkedList<LinkedList<GameObject>> tmp_list = new LinkedList<LinkedList<GameObject>>();
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
		
		for(int i = 0;i<object.size();i++) {
			tmp_list.add(new LinkedList<GameObject>());
		}
		
		for(LinkedList<GameObject> list : object) {
			for(int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if(tempObject.getX()+16 > camX && tempObject.getY()+16 > camY && tempObject.getX()-16 < camX+width && tempObject.getY()-16 < camY+height) {
					tmp_list.get(tempObject.getZIndex()).add(tempObject);
				}
			}
		}
		
		for(LinkedList<GameObject> chunk : chunks) {
			for(GameObject object : chunk) {
				tmp_list.get(object.getZIndex()).add(object);
			}
		}
		
		//chunks
		for(Chunk chunk : chunks_on_screen) {
			LinkedList<GameObject> chunk_content = chunk.getTilesEntities();
			for(GameObject obj : chunk_content) {
				tmp_list.get(obj.getZIndex()).add(obj);
			}
		}
		
		//particles
		LinkedList<Particle> particles = ps.getParticles();
		for(Particle particle : particles) {
			tmp_list.get(particle.getZIndex()).add(particle);
		}
		
		for(LinkedList<GameObject> list : tmp_list) {
			for(GameObject obj : list) {
				obj.render(g);
			}
		}
		
		for(Chunk chunk : chunks_on_screen) {
			chunk.renderBorder(g);
		}
		
		
		
		/*for(LinkedList<GameObject> list : object) {
			for(int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if(tempObject.getX()+16 > camX && tempObject.getY()+16 > camY && tempObject.getX()-16 < camX+width && tempObject.getY()-16 < camY+height) {
					tempObject.render(g);
				}
			}
		}*/
		
		//chunk looping
		/*for(LinkedList<GameObject> chunk : chunks) {
			for(GameObject object : chunk) {
				object.render(g);
			}
		}*/
		
	}
	
	public void addObject(int z_index, GameObject object) {
		while(z_index > this.object.size()) {//add new layers if it doesnt exist
			this.object.add(new LinkedList<GameObject>());
		}
		this.object.get(z_index).add(object);
	}
	public void removeObject(int z_index, GameObject object) {
		this.object.get(z_index).remove(object);
	}
	
	public LinkedList<GameObject> getSelectableObjects(World world) {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
		LinkedList<GameObject> objs = new LinkedList<GameObject>();
		ID[] ids = {ID.Tree, ID.Mushroom};
		
		for(LinkedList<GameObject> list : object) {
			for(int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if(isInArray(ids, tempObject.getId())) {
					objs.add(tempObject);
				}
			}
		}
		
		for(LinkedList<GameObject> chunk : chunks) {
			for(GameObject object : chunk) {
				if(isInArray(ids, object.getId())) {
					objs.add(object);
				}
			}
		}
		
		//chunks
		for(Chunk chunk : chunks_on_screen) {
			LinkedList<GameObject> chunk_content = chunk.getTilesEntities();
			for(GameObject obj : chunk_content) {
				if(isInArray(ids, obj.getId())) {
					objs.add(obj);
				}
			}
		}
		
		return objs;
	}
	
	private Boolean isInArray(ID[] arr, ID val) {
		for(ID tmp : arr) {
			if(tmp == val) {
				return true;
			}
		}
		return false;
	}

	public void findAndRemoveObject(GameObject item, World world) {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
		
		for(LinkedList<GameObject> list : object) {
			list.remove(item);
		}
		
		for(LinkedList<GameObject> chunk : chunks) {
			chunk.remove(item);
		}
		
		//chunks
		for(Chunk chunk : chunks_on_screen) {
			//chunk.removeFromTilesEntities(item);
			world.chunks.get(new Point(chunk.x, chunk.y)).removeFromTilesEntities(item);
		}
	}
	
}
