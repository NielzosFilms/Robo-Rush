package game.system.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.Random;

import game.enums.GAMESTATES;
import game.enums.ID;
import game.enums.MENUSTATES;
import game.system.audioEngine.AudioFiles;
import game.assets.entities.Player;
import game.assets.entities.particles.Particle;
import game.assets.entities.particles.ParticleSystem;
import game.system.hud.DebugHUD;
import game.system.hud.HUD;
import game.system.inputs.KeyInput;
import game.system.inputs.MouseInput;
import game.system.inventory.InventorySystem;
import game.system.lighting.LightingSystem;
import game.assets.objects.House;
import game.system.menu.Menu;
import game.textures.Textures;
import game.system.world.LevelLoader;
import game.system.world.World;
import game.system.menu.Text;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 852753996046178928L;
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int NEW_WIDTH = (int) screenSize.getWidth(), NEW_HEIGHT = (int) screenSize.getHeight();
	public static final float RATIO = (float) NEW_WIDTH / NEW_HEIGHT;
	public static final int WIDTH = 480, HEIGHT = (int) Math.round(WIDTH / RATIO); // 640 480 idk which is better
	public static final float SCALE_WIDTH = ((float) NEW_WIDTH) / WIDTH, SCALE_HEIGHT = ((float) NEW_HEIGHT) / HEIGHT;
	public static final String TITLE = "Top Down Java Game";
	public static final String VERSION = "ALPHA V 2.21.0 INFDEV";

	public static GAMESTATES game_state = GAMESTATES.Game;
	public static boolean DEDUG_MODE = true;
	public static boolean showHitboxes = false;

	private Thread thread;
	private boolean running = true;
	public static int current_fps = 0;
	static Canvas canvas;

	private Random r;
	public static Handler handler;
	public static KeyInput keyInput;
	public static MouseInput mouseInput;
	public static Textures textures;
	public static AudioFiles audioFiles;
	public static Collision collision;
	public static Camera cam;
	public static LevelLoader ll;

	public static Menu menu;
	public static HUD hud;
	public static DebugHUD debug_hud;

	public static InventorySystem inventorySystem;
	public static LightingSystem lightingSystem;
	public static ParticleSystem ps;
	public static ImageRendering imageRenderer;

	public static World world;
	public static Player player;

	public Game() {
		r = new Random();

		handler = new Handler();
		keyInput = new KeyInput();
		mouseInput = new MouseInput();
		cam = new Camera(0, 0);
		collision = new Collision();

		textures = new Textures();
		audioFiles = new AudioFiles();

		inventorySystem = new InventorySystem();
		ps = new ParticleSystem();
		lightingSystem = new LightingSystem();

		menu = new Menu();
		hud = new HUD();
		debug_hud = new DebugHUD(mouseInput);

		world = new World();

		player = new Player(0, 0, 2, ID.Player, keyInput);
		handler.addObject(player);

		setRequirements();

		addTestObjects();

		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
		this.addMouseWheelListener(mouseInput);
		new Window(NEW_WIDTH, NEW_HEIGHT, TITLE, this);
	}

	private void setRequirements() {
		keyInput.setRequirements(handler, inventorySystem, world);
		mouseInput.setRequirements(inventorySystem, menu, cam);
		collision.setRequirements(handler, world, player);

		inventorySystem.setRequirements(handler, mouseInput, world, player);
		lightingSystem.setRequirements(handler, world, cam);

		hud.setRequirements(handler, player, mouseInput, world, cam);

		world.setRequirements(player, textures);
		Long seed = 9034865798355343302L; // r.nextLong();
		world.generate(seed);
	}

	private void addTestObjects() {
		//handler.addObject(1, new House(0, 0, 1, ID.House));
		ps.addParticle(new Particle(0, 0, 3, ID.Particle, 0, -1, 60, ps));
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
		if (game_state == GAMESTATES.Game && World.loaded) {

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
			if(DEDUG_MODE) debug_hud.tick();

			keyInput.tick();

			/*
			 * if(AudioPlayer.audioEnded(audioFiles.futureopolis)) {
			 * AudioPlayer.stopSound(audioFiles.futureopolis); }
			 */

		} else if ((game_state == GAMESTATES.Pauzed) || game_state == GAMESTATES.Menu) {
			menu.tick();
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
		g.fillRect(0, 0, WIDTH, HEIGHT);

		if (game_state == GAMESTATES.Menu) {
			menu.render(g, g2d);
		} else if (game_state == GAMESTATES.Game && World.loaded) {
			g2d.translate(cam.getX(), cam.getY()); // start of cam

			handler.render(g, (int) -cam.getX(), (int) -cam.getY(), WIDTH, HEIGHT, world, ps);

			// ongeveer 30-35 ms
			Long start = System.currentTimeMillis();
			// lightingSystem.render(g);
			Long finish = System.currentTimeMillis();
			// System.out.println("Light System Render Time: " + (finish - start));

			hud.renderCam(g, g2d);
			if(DEDUG_MODE) debug_hud.renderCam(g, g2d);
			g2d.translate(-cam.getX(), -cam.getY()); // end of cam
			hud.render(g, g2d);
			if(DEDUG_MODE) debug_hud.render(g, g2d);

			inventorySystem.render(g);

			if (game_state == GAMESTATES.Pauzed) {
				menu.setState(MENUSTATES.Pauzed);
				menu.render(g, g2d);
			}
		}

		g.dispose();
		g2d.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		// System.setProperty("sun.java2d.opengl", "true");
		canvas = new Game();
	}

}
