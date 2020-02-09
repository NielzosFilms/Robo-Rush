package game.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

	private Handler handler;
	private Game game;
	
	public MouseInput(Handler handler, Game game) {
		this.handler = handler;
		this.game = game;
	}
	
	public void tick() {
		
	}
	
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			
		}else if(e.getButton() == MouseEvent.BUTTON3) {
			
		}
		
	}
	
	public void mouseReleased(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			
		}else if(e.getButton() == MouseEvent.BUTTON3) {
			
		}
		
	}
	
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if(mx > x && mx < x + width) {
			if(my > y && my < y + height) {
				return true;
			}else return false;
		}else return false;
	}
	
}
