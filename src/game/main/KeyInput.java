package game.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyInput extends KeyAdapter {

	private Handler handler;
	
	public Map<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();
	
	public KeyInput(Handler handler) {
		this.handler = handler;
		
		keysDown.put(KeyEvent.VK_W, false);
		keysDown.put(KeyEvent.VK_S, false);
		keysDown.put(KeyEvent.VK_A, false);
		keysDown.put(KeyEvent.VK_D, false);
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Player) {
				if(key == KeyEvent.VK_W) keysDown.put(KeyEvent.VK_W, true);
				if(key == KeyEvent.VK_S) keysDown.put(KeyEvent.VK_S, true);
				if(key == KeyEvent.VK_A) keysDown.put(KeyEvent.VK_A, true);
				if(key == KeyEvent.VK_D) keysDown.put(KeyEvent.VK_D, true);
			}
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Player) {
				if(key == KeyEvent.VK_W) keysDown.put(KeyEvent.VK_W, false);
				if(key == KeyEvent.VK_S) keysDown.put(KeyEvent.VK_S, false);
				if(key == KeyEvent.VK_A) keysDown.put(KeyEvent.VK_A, false);
				if(key == KeyEvent.VK_D) keysDown.put(KeyEvent.VK_D, false);
			}
		}
	}
	
	public void tick() {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			if(tempObject.getId() == ID.Player) {
				if(keysDown.get(KeyEvent.VK_W) == true) tempObject.setVelY(-5);
				if(keysDown.get(KeyEvent.VK_S) == true) tempObject.setVelY(5);
				if((keysDown.get(KeyEvent.VK_W) == true && keysDown.get(KeyEvent.VK_S) == true) || (keysDown.get(KeyEvent.VK_W) == false && keysDown.get(KeyEvent.VK_S) == false)) tempObject.setVelY(0);
				
				if(keysDown.get(KeyEvent.VK_A) == true) tempObject.setVelX(-5);
				if(keysDown.get(KeyEvent.VK_D) == true) tempObject.setVelX(5);
				if((keysDown.get(KeyEvent.VK_A) == true && keysDown.get(KeyEvent.VK_D) == true) || (keysDown.get(KeyEvent.VK_A) == false && keysDown.get(KeyEvent.VK_D) == false)) tempObject.setVelX(0);
			}
		}
	}
	
}
