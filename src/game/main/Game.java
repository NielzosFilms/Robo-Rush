package game.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Random;

import game.entities.Player;
import game.hud.HUD;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 852753996046178928L;
	
	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
	public static final String TITLE = "2D Platformer";
	public static final int FPS = 60;
	public static final String VERSION = "ALPHA V 1.0";

	private Thread thread;
	private boolean running = true;
	
	private Random r;
	
	private Handler handler;
	private KeyInput keyInput;
	private HUD hud;
	private Camera cam;
	
	public Game() {
		handler = new Handler();
		keyInput = new KeyInput(handler);
		cam = new Camera(0, 0);
		this.addKeyListener(keyInput);
		new Window(WIDTH, HEIGHT, TITLE, this);
		
		hud = new HUD();
		
		r = new Random();
		handler.addObject(new Player(WIDTH/2-16, HEIGHT/2-16, ID.Player, keyInput));
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
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	private void tick() {
		handler.tick();
		
		for(int i = 0; i < handler.object.size(); i++) {
			if(handler.object.get(i).getId() == ID.Player) {
				cam.tick(handler.object.get(i));
			}
		}
		
		keyInput.tick();
		hud.tick();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g2d.translate(cam.getX(), cam.getY()); //start of cam
		g.setColor(Color.PINK);
		g.fillRect(50, 50, WIDTH-100, HEIGHT-100);
		handler.render(g);
		g2d.translate(-cam.getX(), -cam.getY()); //end of cam
		hud.render(g, g2d);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String args[]) {
		new Game();
	}
	
	public static int clamp(int var, int min, int max) {
		if(var <= min) {
			var = min;
		}else if (var >= max) {
			var = max;
		}
		return var;
	}
	
	public static double clampFloat(double velY, double d, double e) {
		if(velY <= d) {
			velY = d;
		}else if (velY >= e) {
			velY = e;
		}
		return velY;
	}
	
}
