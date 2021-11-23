package game.system.main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import game.assets.entities.player.Character_Robot;
import game.audio.SoundEffect;
import game.enums.GAMESTATES;
import game.enums.ID;
import game.assets.entities.player.Player;
import game.enums.SETTING;
import game.system.helpers.Logger;
import game.system.helpers.Settings;
import game.system.main.postProcessing.PostProcessing;
import game.system.systems.cutscene.CutsceneEngine;
import game.system.systems.menu.elements.LoadingAnimation;
import game.system.inputs.KeyInput;
import game.system.inputs.MouseInput;
import game.system.systems.menu.MenuSystem;
import game.textures.*;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 852753996046178928L;
	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	public static final float RATIO = (float) SCREEN_SIZE.width / SCREEN_SIZE.height;
	public static final int GAME_WIDTH = 16*24;
	public static final float SCALE_FINAL = 6.7f;
	public static float scale = SCALE_FINAL;
	public static final String TITLE = "Robo Rush";
	public static final String VERSION = "ALPHA V 4.0";

	public static Window window;
	public static Point windowSize = new Point(SCREEN_SIZE.width, SCREEN_SIZE.height);

	public static BufferedImage game_image = new BufferedImage(SCREEN_SIZE.width, SCREEN_SIZE.height, BufferedImage.TYPE_INT_ARGB);

	public static GAMESTATES game_state = GAMESTATES.Menu;
	public static boolean DEBUG_MODE = false;
	public static boolean NO_SAVE = false, NO_LOAD = false, WINDOWED = false;
	public static boolean DEV_MODE = false;
	public static final String SAVES_DIRECTORY = "saves";

	public static int current_loaded_save_slot = 1;

	private Thread thread;
	private boolean running = true;
	public static int current_fps = 0;
	static Canvas canvas;

	public static Textures textures;
	public static Fonts fonts;
	public static KeyInput keyInput;
	public static MouseInput mouseInput;
	public static Settings settings;
	public static PostProcessing postProcessing;

	public static MenuSystem menuSystem;
	public static CutsceneEngine cutsceneEngine;

	//public static World world;
	public static GameController gameController;

	public static LoadingAnimation loadingAnimation = new LoadingAnimation(16, 16, 16, 16);

	public Game() {
		settings = new Settings();
		keyInput = new KeyInput();
		mouseInput = new MouseInput();
		SoundEffect.init();
		postProcessing = new PostProcessing();

		textures = new Textures();
		fonts = new Fonts();

		menuSystem = new MenuSystem();
		cutsceneEngine = new CutsceneEngine();

		gameController = new GameController();
		setRequirements();

		if(game_state == GAMESTATES.Game) generateRequirements();

		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
		this.addMouseWheelListener(mouseInput);
		window = new Window(SCREEN_SIZE.width, SCREEN_SIZE.height, 1000, RATIO, TITLE, this);
	}

	public void setRequirements() {
		gameController.setRequirements(textures, keyInput, mouseInput);

		keyInput.setRequirements(gameController);
		mouseInput.setRequirements(this, gameController);

		menuSystem.setRequirements(mouseInput);
	}

	private void generateRequirements() {
		gameController.generate();
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
		double tick_ns = 1000000000 / amountOfTicks;
		double frames_ns = 1000000000 / (double)settings.getSetting(SETTING.framerate_cap);
		double tick_delta = 0;
		double frames_delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			tick_delta += (now - lastTime) / tick_ns;
			frames_delta += (now - lastTime) / frames_ns;
			lastTime = now;
			while (tick_delta >= 1) {
				tick();
				tick_delta--;
			}
			if (running && frames_delta >= 1) {
				render();
				frames++;
				frames_delta--;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				current_fps = frames;
				frames = 0;
			}
		}
		stop();
	}

	private void tick() {
		Rectangle window_bounds = window.f.getBounds();
		windowSize = new Point(window_bounds.width, window_bounds.height);
		scale = (float)windowSize.x / (float)GAME_WIDTH;

		if (game_state == GAMESTATES.Game && GameController.loaded) {
			gameController.tick();

			/*
			 * if(AudioPlayer.audioEnded(audioFiles.futureopolis)) {
			 * AudioPlayer.stopSound(audioFiles.futureopolis); }
			 */

		} else if(game_state == GAMESTATES.CutScene) {
			cutsceneEngine.tick();
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
		//Graphics g_bs = bs.getDrawGraphics();
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;

		/*Graphics g = game_image.createGraphics();
		Graphics2D g2d = (Graphics2D) g;*/

		/*
		 * AffineTransform scalingTransform =
		 * AffineTransform.getScaleInstance(SCALE_WIDTH,SCALE_HEIGHT);
		 * g2d.transform(scalingTransform);
		 */
		g2d.scale(scale, scale);
		//g.setColor(new Color(217, 247, 255));
		g.setColor(new Color(24, 20, 37));
		g.fillRect(0, 0, windowSize.x, windowSize.y);

		if (game_state == GAMESTATES.Menu) {
			menuSystem.render(g, g2d);
		} else if(game_state == GAMESTATES.CutScene) {
			cutsceneEngine.render(g, g2d);
		} else if ((game_state == GAMESTATES.Game || game_state == GAMESTATES.Pauzed) && GameController.loaded) {
			gameController.render(g, g2d);
			if (game_state == GAMESTATES.Pauzed) {
				menuSystem.render(g, g2d);
			}
		}
		loadingAnimation.render(g);

		if(game_state != GAMESTATES.CutScene) g.drawImage(settings.getCursor().getTexure(), mouseInput.mouse_x, mouseInput.mouse_y, 8, 8, null);

		drawVersion(g, g2d);

		/*postProcessing.render(g_bs, game_image);
		g_bs.drawImage(game_image, 0, 0, null);*/

		//g_bs.dispose();
		g.dispose();
		g2d.dispose();
		bs.show();
	}

	private void drawVersion(Graphics g, Graphics2D g2d) {
		Font font = Fonts.default_fonts.get(3);
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics(font);
		String version = Game.VERSION;
		String name = "NielzosFilms";

		g.setColor(Color.white);
		g2d.drawString(version, (Game.getGameSize().x - fontMetrics.stringWidth(version)), fontMetrics.getAscent());
		g2d.drawString(name, (Game.getGameSize().x - fontMetrics.stringWidth(name)),
				fontMetrics.getHeight() + fontMetrics.getAscent());
	}

	public static void main(String[] args) {
		for(String arg : args) {
			if(arg.equals("debug")) DEBUG_MODE = true;
			if(arg.equals("no-save")) NO_SAVE = true;
			if(arg.equals("no-load")) NO_LOAD = true;
			if(arg.equals("windowed")) {
				WINDOWED = true;
				//HEIGHT -= 20;
			}
			if(arg.equals("dev")) DEV_MODE = true;
		}
		Logger.clearLogs();
		Logger.print("Arguments: " + Arrays.toString(args));
		Logger.print("Game starting...");
		System.setProperty("sun.java2d.opengl", "True");
		canvas = new Game();
	}

	public static Point getGameSize() {
		return new Point(Math.round(windowSize.x / scale), Math.round(windowSize.y / scale));
	}

}
