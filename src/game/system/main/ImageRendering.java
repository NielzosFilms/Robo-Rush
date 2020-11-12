package game.system.main;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import game.system.lighting.LightingSystem;

public class ImageRendering implements Runnable{
	
	private Thread thread;
	private boolean running = true;
	public static int current_fps = 0;
	
	private LightingSystem lightingSystem;
	private Canvas canvas;
	private Game game;
	
	//syncronized with game ???
	
	public ImageRendering(Canvas canvas, Game game) {
		this.canvas = canvas;
		this.game = game;
	}
	
	public void setLightingSystem(LightingSystem lightingSystem) {
		this.lightingSystem = lightingSystem;
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
	
	public synchronized void run() {
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
	
	public void tick() {
		
	}
	
	public void render() {
		BufferStrategy bs = game.getBufferStrategy();
		if(bs == null) {
			game.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		Long start = System.currentTimeMillis();
		lightingSystem.render(g);
		Long finish = System.currentTimeMillis();
		System.out.println("Light System Render Time: " + ( finish - start ) );
		
		g.dispose();
		g2d.dispose();
		bs.show();
	}

}
