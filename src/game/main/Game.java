package game.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.Random;

import game.entities.Enemy;
import game.entities.Player;
import game.hud.HUD;
import game.textures.Textures;
import game.world.LevelLoader;
import game.world.World;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 852753996046178928L;
	
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static final int WIDTH = 480, HEIGHT = WIDTH / 16 * 9; //640 480 idk which is better
	public static final int NEW_WIDTH = (int) screenSize.getWidth(), NEW_HEIGHT = (int) screenSize.getHeight();
	public static final float SCALE_WIDTH = ((float) NEW_WIDTH) / WIDTH, SCALE_HEIGHT = ((float) NEW_HEIGHT) / HEIGHT;
	public static final String TITLE = "2D Platformer";
	public static final int FPS = 60;
	public static final String VERSION = "ALPHA V 2.3.0 INFDEV";

	private Thread thread;
	private boolean running = true;
	public static int current_fps = 0;
	
	public static boolean showHitboxes = false;
	public static GAMESTATES game_state = GAMESTATES.Game;
	public static boolean pauzed = false;
	
	private Random r;
	
	private Handler handler;
	private KeyInput keyInput;
	private HUD hud;
	public static Camera cam;
	static Canvas canvas;
	public Textures textures;
	private Collision collision;
	
	private LevelLoader ll;
	private World world;
	//private static ArrayList<ArrayList<Long>> blocks;
	
	public Game() {
		handler = new Handler();
		textures = new Textures();
		keyInput = new KeyInput(handler);
		//blocks = ll.getLevelData();
		ll.loadLevelData("assets/world/structures/top_down_map.json");
		collision = new Collision(handler, ll);
		cam = new Camera(0, 0);
		this.addKeyListener(keyInput);
		new Window(NEW_WIDTH, NEW_HEIGHT, TITLE, this);
		r = new Random();
		
		Player player = new Player(0, 0, ID.Player, keyInput);
		hud = new HUD(handler, player);
		handler.addObject(player);
		handler.addObject(new Enemy(8*16, 8*16, ID.Enemy));
		
		Long temp_seed = -2162936016020339965L;//r.nextLong();
		Long moist_seed = -6956972119187843971L;//r.nextLong();
		Long height_seed = 3695317381661324390L;
		world = new World(0, 0, 3, 3, height_seed, temp_seed, moist_seed);
		//ll.LoadLevelHeightMap(handler);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				current_fps = frames;
				frames = 0;
			}
		}
		stop();
	}
	
	private void tick() {
		if(game_state == GAMESTATES.Game && !pauzed && world.loaded) {
			handler.tick(world);
			collision.tick();
			
			world.tick();
			
			for(int i = 0; i < handler.object.size(); i++) {
				if(handler.object.get(i).getId() == ID.Player) {
					
					world.getChunkPointWithCoords(handler.object.get(i).x, handler.object.get(i).y);
					
					cam.tick(handler.object.get(i));
				}
			}
			hud.tick();
			
			keyInput.tick();
		} else if(game_state == GAMESTATES.Game && pauzed) {
			
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		/*AffineTransform scalingTransform = AffineTransform.getScaleInstance(SCALE_WIDTH,SCALE_HEIGHT);
		g2d.transform(scalingTransform);*/
		g2d.scale(SCALE_WIDTH, SCALE_HEIGHT);
		
		g.setColor(Color.decode("#d1e3ff"));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		if(game_state == GAMESTATES.Game && world.loaded) {
		
			g2d.translate(cam.getX(), cam.getY()); //start of cam
			
			/*for(int i = 0;i<7;i++) {
				g.drawImage(Textures.sky, i*Textures.sky.getWidth(), 0, null);
			}*/
			world.render(g);
				
			handler.render(g, (int)-cam.getX(), (int)-cam.getY(), WIDTH, HEIGHT);
			
			//if(showHitboxes) {
			/*g.setColor(Color.blue);
			for(int i = 0;i<ll.rectangle_bounds.size();i++) {
				g.drawRect(ll.rectangle_bounds.get(i).x+(x*16), ll.rectangle_bounds.get(i).y+(y*16), ll.rectangle_bounds.get(i).width, ll.rectangle_bounds.get(i).height);
			}
			
			for(int i = 0;i<ll.polygon_bounds.size();i++) {
				g.setColor(Color.green);
				g.drawPolygon(ll.polygon_bounds.get(i));
				g.setColor(Color.cyan);
				g.drawRect(ll.polygon_bounds.get(i).getBounds().x, ll.polygon_bounds.get(i).getBounds().y, ll.polygon_bounds.get(i).getBounds().width, ll.polygon_bounds.get(i).getBounds().height);
			}*/
			//}
			//g.drawRect(0, 192, 16*9, 16*4);
			
			g2d.translate(-cam.getX(), -cam.getY()); //end of cam
			hud.render(g, g2d);
		}
		
		if(pauzed) {
			g.setColor(new Color(0, 0, 0, 0.5f));
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		
		g.dispose();
		g2d.dispose();
		bs.show();
	}
	
	public static void main(String args[]) {
		canvas = new Game();
	}
	
	public static int clamp(int var, int min, int max) {
		if(var <= min) {
			var = min;
		}else if (var >= max) {
			var = max;
		}
		return var;
	}
	
	public static double clampDouble(double var, double min, double max) {
		if(var <= min) {
			var = min;
		}else if (var >= max) {
			var = max;
		}
		return var;
	}
	public static float clampFloat(float var, float min, float max) {
		if(var <= min) {
			var = min;
		}else if (var >= max) {
			var = max;
		}
		return var;
	}
	
}
