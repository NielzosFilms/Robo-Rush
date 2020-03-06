package game.world;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import game.main.Game;
import game.main.GameObject;
import game.objects.Tile;

public class World {
	
	public Long seed, temp_seed, moist_seed;
	public HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
	public int x, y, w, h;
	public static boolean loaded = false;

	public World(int x, int y, int w, int h, Long seed, Long temp_seed, Long moist_seed) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.seed = seed;
		this.temp_seed = temp_seed;
		this.moist_seed = moist_seed;
		
		System.out.println("Height seed: "+seed);
		System.out.println("Temperature seed: "+temp_seed);
		System.out.println("Moisture seed: "+moist_seed);
		
		for(int yy = -2;yy<2;yy++) {
			for(int xx = -2;xx<2;xx++) {
				chunks.put(new Point(xx*16, yy*16), new Chunk(xx*16, yy*16, seed, temp_seed, moist_seed, this));
				//chunks.get(new Point(xx*16, yy*16)).entities.add(new Enemy((((xx)*16)+8)*16, (((yy)*16)+8)*16, ID.Enemy));
			}
		}
		
		loaded = true;
	}
	
	public void tick() {
		int camX = (Math.round(-Game.cam.getX()/16));
		int camY = (Math.round(-Game.cam.getY()/16));
		int camW = (Math.round(Game.WIDTH/16));
		int camH = (Math.round(Game.HEIGHT/16));
		
		for(int y = camY-32;y < camY+camH+16;y++) {
			for(int x = camX-32;x < camX+camW+16;x++) {
				if(chunks.containsKey(new Point(x, y))) {
					chunks.get(new Point(x, y)).tick();
					if(!chunks.containsKey(new Point(x-16, y))) {
						chunks.put(new Point(x-16, y), new Chunk(x-16, y, seed, temp_seed, moist_seed, this));
					}else if(!chunks.containsKey(new Point(x+16, y))) {
						chunks.put(new Point(x+16, y), new Chunk(x+16, y, seed, temp_seed, moist_seed, this));
					}
					if(!chunks.containsKey(new Point(x, y-16))) {
						chunks.put(new Point(x, y-16), new Chunk(x, y-16, seed, temp_seed, moist_seed, this));
					}else if(!chunks.containsKey(new Point(x, y+16))) {
						chunks.put(new Point(x, y+16), new Chunk(x, y+16, seed, temp_seed, moist_seed, this));
					}
				}
			}
		}
	}
	
	public void render(Graphics g) {
		
		int camX = (Math.round(-Game.cam.getX()/16));
		int camY = (Math.round(-Game.cam.getY()/16));
		int camW = (Math.round(Game.WIDTH/16));
		int camH = (Math.round(Game.HEIGHT/16));
		
		for(int y = camY-32;y < camY+camH+16;y++) {
			for(int x = camX-32;x < camX+camW+16;x++) {
				if(chunks.containsKey(new Point(x, y))) {
					Chunk chunk = chunks.get(new Point(x, y));
					chunk.renderTiles(g);
				}
			}
		}
		
		for(int y = camY-32;y < camY+camH+16;y++) {
			for(int x = camX-32;x < camX+camW+16;x++) {
				if(chunks.containsKey(new Point(x, y))) {
					Chunk chunk = chunks.get(new Point(x, y));
					chunk.renderEntities(g);
				}
			}
		}
		
		/*for(Chunk chunk : chunks) {
			if(OnScreen(chunk.x*16, chunk.y*16, 16, 16)) {
				chunk.render(g);
			}
		}*/
		
		
		
		/*for(int y = -32;y <= 32;y+=16) {
			for(int x = -32;x <= 32;x+=16) {
				if(chunks.containsKey(new Point(x, y))) {
					chunks.get(new Point(x, y)).render(g);
				}
			}
		}*/
		
		
		
		/*chunks.get(0).get(0).render(g);
		chunks.get(16).get(0).render(g);*/
	}
	
	//functions to get specific tiles/tiletypes
	
	private boolean OnScreen(int x, int y, int w, int h) {
		if((x+(16*16) > -Game.cam.getX() && x < -Game.cam.getX()+Game.WIDTH) && (y+(16*16) > -Game.cam.getY() && y < -Game.cam.getY()+Game.HEIGHT)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Chunk getChunkWithCoords(int x, int y) {
		Point chunk_point = new Point(x, y);
		return chunks.get(chunk_point);
	}
	
	public Point getChunkPointWithCoords(int x, int y) {
		x = x/16;
		y = y/16;
		
		x -= Math.floorMod(x, 16);
		y -= Math.floorMod(y, 16);
		
		return new Point(x, y);
	}
	
	
	
}
