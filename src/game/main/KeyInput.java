package game.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import game.entities.Player;

public class KeyInput extends KeyAdapter {

	private Handler handler;
	
	//public Map<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();
	/*
	 * 0 = W
	 * 1 = S
	 * 2 = A
	 * 3 = D
	 * 4 = Space
	 * 5 = Shift
	 * 6 = Ctrl
	 * 7 = Tab
	 */
	public boolean[] keysDown = {false, false, false, false, false, false, false, false};
	
	public KeyInput(Handler handler) {
		this.handler = handler;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Player) {
				if(key == KeyEvent.VK_W) keysDown[0] = true;
				if(key == KeyEvent.VK_S) keysDown[1] = true;
				if(key == KeyEvent.VK_A) keysDown[2] = true;
				if(key == KeyEvent.VK_D) keysDown[3] = true;
				if(key == KeyEvent.VK_SPACE) keysDown[4] = true;
				if(key == KeyEvent.VK_SHIFT) keysDown[5] = true;
				if(key == KeyEvent.VK_CONTROL) {
					keysDown[6] = true;
					if(!Game.showHitboxes) Game.showHitboxes = true;
					else if(Game.showHitboxes) Game.showHitboxes = false;
				}
			}
		}
		
		if(key == KeyEvent.VK_ESCAPE) System.exit(1);
		
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Player) {
				if(key == KeyEvent.VK_W) keysDown[0] = false;
				if(key == KeyEvent.VK_S) keysDown[1] = false;
				if(key == KeyEvent.VK_A) keysDown[2] = false;
				if(key == KeyEvent.VK_D) keysDown[3] = false;
				if(key == KeyEvent.VK_SHIFT) keysDown[5] = false;
				if(key == KeyEvent.VK_CONTROL) keysDown[6] = false;
				if(key == KeyEvent.VK_SPACE) keysDown[4] = false;
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
