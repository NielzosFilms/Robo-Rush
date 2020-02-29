package game.world;

import java.awt.Graphics;
import java.util.LinkedList;

import game.main.Game;

public class World {
	
	public Long seed;
	public LinkedList<Chunk> chunks = new LinkedList<Chunk>();
	public int x, y, w, h;
	public static boolean loaded = false;

	public World(int x, int y, int w, int h, Long seed) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.seed = seed;
		
		for(int yy = -10;yy<=10;yy++) {
			for(int xx = -10;xx<=10;xx++) {
				chunks.add(new Chunk(xx*16, yy*16, seed));
			}
		}
		loaded = true;
		/*chunks.add(new Chunk(-16, 0, seed));
		chunks.add(new Chunk(0, 0, seed));
		chunks.add(new Chunk(16, 0, seed));
		chunks.add(new Chunk(0, 16, seed));
		chunks.add(new Chunk(16, 16, seed));*/
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		for(Chunk chunk : chunks) {
			if(OnScreen(chunk.x*16, chunk.y*16, 16, 16)) {
				chunk.render(g);
			}
		}
	}
	
	//functions to get specific tiles/tiletypes
	/*public static void GenerateChunk() {
		
	}*/
	
	private boolean OnScreen(int x, int y, int w, int h) {
		if((x+(16*16) > -Game.cam.getX() && x < -Game.cam.getX()+Game.WIDTH) && (y+(16*16) > -Game.cam.getY() && y < -Game.cam.getY()+Game.HEIGHT)) {
			return true;
		} else {
			return false;
		}
	}
	
}
