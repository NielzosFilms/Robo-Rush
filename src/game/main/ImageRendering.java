package game.main;

public class ImageRendering implements Runnable{
	
	private Thread thread;
	private boolean running = true;
	public static int current_fps = 0;
	
	public ImageRendering() {
		
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
		System.out.println("ImageRendering Test");
	}
	
	public void render() {
		
	}

}
