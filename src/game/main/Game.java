package game.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.Random;

import game.audioEngine.AudioFiles;
import game.entities.Player;
import game.entities.particles.Particle;
import game.entities.particles.ParticleSystem;
import game.hud.HUD;
import game.inventory.InventorySystem;
import game.lighting.LightingSystem;
import game.objects.House;
import game.textures.Textures;
import game.world.LevelLoader;
import game.world.World;
import game.menu.Text;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 852753996046178928L;

	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public static final int NEW_WIDTH = (int) screenSize.getWidth(), NEW_HEIGHT = (int) screenSize.getHeight();
	public static final float RATIO = (float) NEW_WIDTH / NEW_HEIGHT;
	public static final int WIDTH = 480, HEIGHT = (int) Math.round(WIDTH / RATIO); // 640 480 idk which is better
	public static final float SCALE_WIDTH = ((float) NEW_WIDTH) / WIDTH, SCALE_HEIGHT = ((float) NEW_HEIGHT) / HEIGHT;
	public static final String TITLE = "Top Down Java Game";
	public static final int FPS = 60;
	public static final String VERSION = "ALPHA V 2.2.0 INFDEV";

	private Thread thread;
	private ImageRendering imageRenderer;
	private boolean running = true;
	public static int current_fps = 0;
	public static boolean DEDUG_MODE = true;

	public static boolean showHitboxes = false;
	public static GAMESTATES game_state = GAMESTATES.Game;
	public static boolean pauzed = false;

	private Random r;

	public static Handler handler;
	public static ParticleSystem ps;
	public static KeyInput keyInput;
	public static MouseInput mouseInput;
	public static HUD hud;
	public static Camera cam;
	static Canvas canvas;
	public static Textures textures;
	public static Collision collision;

	public static Player player;

	public static LevelLoader ll;
	public static World world;
	public static InventorySystem inventorySystem;
	public static LightingSystem lightingSystem;
	public static AudioFiles audioFiles;

	public Game() {
		handler = new Handler();
		keyInput = new KeyInput(handler);
		mouseInput = new MouseInput();
		r = new Random();
		textures = new Textures();

		audioFiles = new AudioFiles();

		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
		this.addMouseWheelListener(mouseInput);
		new Window(NEW_WIDTH, NEW_HEIGHT, TITLE, this);

		if (game_state == GAMESTATES.Game) {
			loadGameRequirements();
		}

	}

	public void loadGameRequirements() {
		ps = new ParticleSystem();

		// blocks = ll.getLevelData();
		// ll.loadLevelData("assets/world/structures/top_down_map.json");

		cam = new Camera(0, 0);
		mouseInput.setCam(cam);

		player = new Player(0, 0, 2, ID.Player, keyInput, textures);
		inventorySystem = new InventorySystem();
		mouseInput.setInventory(inventorySystem);
		keyInput.setInventory(inventorySystem);
		hud = new HUD(handler, player);
		hud.setMouseInput(mouseInput);
		hud.setCam(cam);
		handler.addObject(player.getZIndex(), player);
		// handler.addObject(1, new Enemy(8*16, 8*16, 2, ID.Enemy));

		ps.addParticle(new Particle(0, 0, 3, ID.Particle, 0, -1, 60, ps));
		// handler.addObject(3, new Particle(0, 0, 3, ID.Particle, 0, -1, 60, handler));

		Long seed = 9034865798355343302L; // r.nextLong();
		world = new World(player, textures);
		world.generate(seed);
		//inventoryOLD.setWorld(world);
		hud.setWorld(world);
		collision = new Collision(handler, world, player);
		keyInput.setWorld(world);
		inventorySystem.setWorld(world);

		lightingSystem = new LightingSystem();
		lightingSystem.setHandler(handler);
		lightingSystem.setWorld(world);
		lightingSystem.setCam(cam);

		handler.addObject(1, new House(0, 0, 1, ID.House));

		/*
		 * handler.addLight(new Light(new Point(0, 300), textures.light));
		 * handler.addObject(1, new Tree(0, 250, 1, ID.Tree, "forest", player,
		 * textures)); handler.addObject(1, new Tree(35, 320, 1, ID.Tree, "forest",
		 * player, textures));
		 */

		// AudioPlayer.playSound(audioFiles.futureopolis, 0.1, true, 0);

		/*
		 * imageRenderer = new ImageRendering(canvas, this); imageRenderer.start();
		 * imageRenderer.setLightingSystem(lightingSystem);
		 */

		// ll.LoadLevelHeightMap(handler);
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
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
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if (running)
				render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				current_fps = frames;
				frames = 0;
			}
		}
		stop();
	}

	private void tick() {
		if (game_state == GAMESTATES.Game && !pauzed && World.loaded) {

			handler.tick(world);
			ps.tick();
			world.tick();
			collision.tick();

			inventorySystem.tick();

			for (LinkedList<GameObject> list : handler.object_entities) {
				for (GameObject gameObject : list) {
					if (gameObject.getId() == ID.Player) {

						// world.getChunkPointWithCoords(list.get(i).x, list.get(i).y);

						cam.tick(gameObject);
					}
				}
			}
			hud.tick();

			keyInput.tick();

			/*
			 * if(AudioPlayer.audioEnded(audioFiles.futureopolis)) {
			 * AudioPlayer.stopSound(audioFiles.futureopolis); }
			 */

		} else if (game_state == GAMESTATES.Game && pauzed) {

		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;

		/*
		 * AffineTransform scalingTransform =
		 * AffineTransform.getScaleInstance(SCALE_WIDTH,SCALE_HEIGHT);
		 * g2d.transform(scalingTransform);
		 */
		g2d.scale(SCALE_WIDTH, SCALE_HEIGHT);

		g.setColor(Color.decode("#d1e3ff"));
		// g.setColor(Color.);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		if (game_state == GAMESTATES.Menu) {
			g.setColor(Color.decode("#363636"));
			g.fillRect(0, 0, WIDTH, HEIGHT);

			g.setColor(Color.WHITE);
			new Text("TEST string", WIDTH / 2, HEIGHT / 2, 20, mouseInput).render(g, g2d);
		}

		if (game_state == GAMESTATES.Game && World.loaded) {

			g2d.translate(cam.getX(), cam.getY()); // start of cam

			handler.render(g, (int) -cam.getX(), (int) -cam.getY(), WIDTH, HEIGHT, world, ps);

			// ongeveer 30-35 ms
			Long start = System.currentTimeMillis();
			// lightingSystem.render(g);
			Long finish = System.currentTimeMillis();
			// System.out.println("Light System Render Time: " + (finish - start));

			hud.renderCam(g, g2d);
			g2d.translate(-cam.getX(), -cam.getY()); // end of cam
			hud.render(g, g2d);

			inventorySystem.render(g);
		}

		if (pauzed) {
			g.setColor(new Color(0, 0, 0, 0.5f));
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}

		g.dispose();
		g2d.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		// System.setProperty("sun.java2d.opengl", "true");

		canvas = new Game();

	}

	public static int clamp(int var, int min, int max) {
		if (var <= min) {
			var = min;
		} else if (var >= max) {
			var = max;
		}
		return var;
	}

	public static double clampDouble(double var, double min, double max) {
		if (var <= min) {
			var = min;
		} else if (var >= max) {
			var = max;
		}
		return var;
	}

	public static float clampFloat(float var, float min, float max) {
		if (var <= min) {
			var = min;
		} else if (var >= max) {
			var = max;
		}
		return var;
	}

}
