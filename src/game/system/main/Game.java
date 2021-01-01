package game.system.main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.Arrays;
import game.enums.GAMESTATES;
import game.enums.ID;
import game.system.audioEngine.AudioFiles;
import game.assets.entities.Player;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.systems.menu.elements.LoadingAnimation;
import game.system.inputs.KeyInput;
import game.system.inputs.MouseInput;
import game.system.systems.menu.MenuSystem;
import game.textures.*;
import game.system.world.World;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 852753996046178928L;
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int NEW_WIDTH = (int) screenSize.getWidth(), NEW_HEIGHT = (int) screenSize.getHeight();
	public static final float RATIO = (float) NEW_WIDTH / NEW_HEIGHT;
	public static int WIDTH = 360, HEIGHT = (int) Math.round(WIDTH / RATIO); // 640 480 360 idk which is better
	public static final float SCALE_WIDTH = ((float) NEW_WIDTH) / WIDTH, SCALE_HEIGHT = ((float) NEW_HEIGHT) / HEIGHT;
	public static final String TITLE = "Top Down Java Game";
	public static final String VERSION = "ALPHA V 3.74.0 COMBAT";

	public static GAMESTATES game_state = GAMESTATES.Menu;
	public static boolean DEBUG_MODE = false;
	public static boolean NO_SAVE = false, NO_LOAD = false, WINDOWED = false;
	public static boolean DEV_MODE = false;

	public static int current_loaded_save_slot;

	private Thread thread;
	private boolean running = true;
	public static int current_fps = 0;
	static Canvas canvas;

	public static Textures textures;
	public static AudioFiles audioFiles;
	public static Fonts fonts;
	public static KeyInput keyInput;
	public static MouseInput mouseInput;

	public static MenuSystem menuSystem;

	public static World world;

	public static LoadingAnimation loadingAnimation = new LoadingAnimation(32, 32, 32, 32);
	public static Texture cursor = new Texture(TEXTURE_LIST.cursors, 2);

	public Game() {

		keyInput = new KeyInput();
		mouseInput = new MouseInput();

		textures = new Textures();
		audioFiles = new AudioFiles();
		fonts = new Fonts();

		menuSystem = new MenuSystem();

		world = new World();
		setRequirements();

		if(game_state == GAMESTATES.Game) generateRequirements();

		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
		this.addMouseWheelListener(mouseInput);
		new Window(NEW_WIDTH, NEW_HEIGHT, TITLE, this);
	}

	public void setRequirements() {
		world.setRequirements(new Player(0, 0, 2, ID.Player, null), textures, keyInput, mouseInput);

		keyInput.setRequirements(world);
		mouseInput.setRequirements(this, world);

		menuSystem.setRequirements(mouseInput);
	}

	private void generateRequirements() {
		world.generate();
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
			world.tick();

			/*
			 * if(AudioPlayer.audioEnded(audioFiles.futureopolis)) {
			 * AudioPlayer.stopSound(audioFiles.futureopolis); }
			 */

		} else if ((game_state == GAMESTATES.Pauzed) || game_state == GAMESTATES.Menu) {
			menuSystem.tick();
		}
		loadingAnimation.tick();
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
		//g.setColor(new Color(217, 247, 255));
		g.setColor(new Color(24, 20, 37));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		if (game_state == GAMESTATES.Menu) {
			menuSystem.render(g, g2d);
		} else if ((game_state == GAMESTATES.Game || game_state == GAMESTATES.Pauzed) && World.loaded) {
			world.render(g, g2d);
			if (game_state == GAMESTATES.Pauzed) {
				menuSystem.render(g, g2d);
			}
		}
		loadingAnimation.render(g);

		g.drawImage(cursor.getTexure(), mouseInput.mouse_x, mouseInput.mouse_y, 8, 8, null);

		g.dispose();
		g2d.dispose();
		bs.show();
	}

	public static void saveChunks() {
		loadingAnimation.setLoading(true);
		if(!NO_SAVE) {
			String directory = "saves";
			Helpers.createDirIfNotExisting(directory);
			try {
				FileOutputStream fos = new FileOutputStream(directory + "/save_slot_" + current_loaded_save_slot + ".data");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(world);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadingAnimation.setLoading(false);
	}

	public static void main(String[] args) {
		for(String arg : args) {
			if(arg.equals("debug")) DEBUG_MODE = true;
			if(arg.equals("no-save")) NO_SAVE = true;
			if(arg.equals("no-load")) NO_LOAD = true;
			if(arg.equals("windowed")) {
				WINDOWED = true;
				HEIGHT -= 20;
			}
			if(arg.equals("dev")) DEV_MODE = true;
		}
		Logger.clearLogs();
		Logger.print("Arguments: " + Arrays.toString(args));
		Logger.print("Game starting...");
		canvas = new Game();
	}

}
