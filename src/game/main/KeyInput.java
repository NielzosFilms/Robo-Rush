package game.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import game.entities.Player;

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
		
		if(key == KeyEvent.VK_ESCAPE) System.exit(1);
		
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
				/*if(keysDown.get(KeyEvent.VK_W) == true) tempObject.setVelY(-5);
				if(keysDown.get(KeyEvent.VK_S) == true) tempObject.setVelY(5);
				if((keysDown.get(KeyEvent.VK_W) == true && keysDown.get(KeyEvent.VK_S) == true) || (keysDown.get(KeyEvent.VK_W) == false && keysDown.get(KeyEvent.VK_S) == false)) tempObject.setVelY(0);*/
				
			}
		}
	}
	
}
