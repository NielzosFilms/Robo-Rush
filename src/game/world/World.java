package game.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import game.main.Game;
import game.main.GameObject;
import game.main.ID;
import game.objects.Light;
import game.textures.Textures;

public class World {
	
	public Long seed, temp_seed, moist_seed;
	public HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
	public  ArrayList<Light> lights_on_screen;
	public int x, y, w, h, time;
	public static boolean loaded = false;
	public static BufferedImage lighting_stencil, final_lights;

	public World(int x, int y, int w, int h, Long seed, Long temp_seed, Long moist_seed) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.seed = seed;
		this.temp_seed = temp_seed;
		this.moist_seed = moist_seed;
		this.time = 0;
		
		System.out.println("Height seed: "+seed);
		System.out.println("Temperature seed: "+temp_seed);
		System.out.println("Moisture seed: "+moist_seed);
		
		for(int yy = -2;yy<2;yy++) {
			for(int xx = -2;xx<2;xx++) {
				chunks.put(new Point(xx*16, yy*16), new Chunk(xx*16, yy*16, seed, temp_seed, moist_seed));
				//chunks.get(new Point(xx*16, yy*16)).entities.add(new Light((((xx)*16)+8)*16, (((yy)*16)+8)*16, ID.Light, Textures.test_light));
			}
		}
		
		loaded = true;
	}
	
	public void tick() {
		//lights_on_screen = new ArrayList<Light>();
		int camX = (Math.round(-Game.cam.getX()/16));
		int camY = (Math.round(-Game.cam.getY()/16));
		int camW = (Math.round(Game.WIDTH/16));
		int camH = (Math.round(Game.HEIGHT/16));
		
		for(int y = camY-32;y < camY+camH+16;y++) {
			for(int x = camX-32;x < camX+camW+16;x++) {
				if(chunks.containsKey(new Point(x, y))) {
					chunks.get(new Point(x, y)).tick(this);
					if(!chunks.containsKey(new Point(x-16, y))) {
						chunks.put(new Point(x-16, y), new Chunk(x-16, y, seed, temp_seed, moist_seed));
					}else if(!chunks.containsKey(new Point(x+16, y))) {
						chunks.put(new Point(x+16, y), new Chunk(x+16, y, seed, temp_seed, moist_seed));
					}
					if(!chunks.containsKey(new Point(x, y-16))) {
						chunks.put(new Point(x, y-16), new Chunk(x, y-16, seed, temp_seed, moist_seed));
					}else if(!chunks.containsKey(new Point(x, y+16))) {
						chunks.put(new Point(x, y+16), new Chunk(x, y+16, seed, temp_seed, moist_seed));
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
		
		int z_indexes = 2;
		for(int z = 0;z<z_indexes;z++) {
			for(int y = camY-32;y < camY+camH+16;y++) {
				for(int x = camX-32;x < camX+camW+16;x++) {
					if(chunks.containsKey(new Point(x, y))) {
						Chunk chunk = chunks.get(new Point(x, y));
						for(GameObject tile : chunk.tiles.get(z)) {
							tile.render(g);
						}
						if(z == z_indexes-1) chunk.render(g);
					}
				}
			}
		}
		
		/*final_lights = lighting_stencil; LIGHTING
		BufferedImage mask = GenerateLights();
		if(mask != null) applyGrayscaleMaskToAlpha(final_lights, mask);
		lighting_stencil = getLightOverlay();*/
		
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
	public static BufferedImage getLightOverlay() {
		BufferedImage dark_img = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		/*int alpha = 255;
		int red   = 0;
		int green = 0;
		int blue  = 0;

		int argb = alpha << 24 + red << 16 + green << 8 + blue;*/
		for(int y = 0;y<dark_img.getHeight();y++) {
			for(int x = 0;x<dark_img.getWidth();x++) {
				dark_img.setRGB(x, y, new Color(0, 0, 0, 225).getRGB());
			}
		}
		return dark_img;
	}
	
	public BufferedImage GenerateLights() {
		if(lights_on_screen != null && lights_on_screen.size() != 0) {
			int w = Game.WIDTH, h = Game.HEIGHT;
		    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	
		    // paint all images, preserving the alpha channels
		    Graphics g = combined.getGraphics();
	    
		    for (Light light : lights_on_screen) {
		        g.drawImage(light.light_img, (int)Game.cam.getX()+light.getX(), (int)Game.cam.getY()+light.getY(), null);
		    }
		    return combined;
	    }else {
	    	return null;
	    }
	}
	
	public void applyGrayscaleMaskToAlpha(BufferedImage image, BufferedImage mask) {
	    int width = image.getWidth();
	    int height = image.getHeight();

	    int[] imagePixels = image.getRGB(0, 0, width, height, null, 0, width);
	    int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

	    for (int i = 0; i < imagePixels.length; i++)
	    {
	        int color = imagePixels[i] & 0x00ffffff; // Mask preexisting alpha
	        
	        // get alpha from color int
	        // be careful, an alpha mask works the other way round, so we have to subtract this from 255
	        if(maskPixels[i] != 0) {
		        int alpha = (maskPixels[i] >> 24)& 0xff;
		        //imagePixels[i] = imagePixels[i] | alpha;
		        imagePixels[i] = new Color(0, 0, 0, alpha).getRGB();
	        }
	    }

	    image.setRGB(0, 0, width, height, imagePixels, 0, width);
	}
	
	private boolean OnScreen(int x, int y, int w, int h) {
		if((x+(16*16) > -Game.cam.getX() && x < -Game.cam.getX()+Game.WIDTH) && (y+(16*16) > -Game.cam.getY() && y < -Game.cam.getY()+Game.HEIGHT)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Point getChunkWithCoords(int x, int y) {
		x = x/16;
		y = y/16;
		
		x -= Math.floorMod(x, 16);
		y -= Math.floorMod(y, 16);
		
		return new Point(x, y);
	}
	
}
