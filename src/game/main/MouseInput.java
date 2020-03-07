package game.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import game.inventory.Inventory;

public class MouseInput extends MouseAdapter implements MouseMotionListener, MouseWheelListener {
	
	public int mouse_x, mouse_y;
	private Inventory inventory;
	private Camera cam;
	
	public MouseInput() {
		this.mouse_x = 0;
		this.mouse_y = 0;
		this.inventory = inventory;
		
	}
	
	public void setCam(Camera cam) {
		this.cam = cam;
	}
	
	public void tick() {
		
	}
	
	public void mousePressed(MouseEvent e) {
		inventory.mouseClicked(e);
		
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
	
	public void mouseClicked(MouseEvent e) {
		//inventory.mouseClicked(e);
	}
	
	public void mouseMoved(MouseEvent e) {
		this.mouse_x = (int) (e.getX()/Game.SCALE_WIDTH);
		this.mouse_y = (int) (e.getY()/Game.SCALE_HEIGHT);
	}
	
	public void mouseDragged(MouseEvent e) {
		//System.out.println("Mouse dragged"+ e.getX()+ " | "+e.getY());
    }
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		//System.out.println("Mouse Wheel: "+e.getWheelRotation());
		if(!inventory.getInventoryOpen()) {
			int new_index = inventory.getHotbarSelected() + e.getWheelRotation();
			if(new_index > inventory.getSizeX()-1) {
				new_index = 0;
			}else if(new_index < 0) {
				new_index = inventory.getSizeX()-1;
			}
			inventory.setHotbarSelected(new_index);
		}
	}
	
	public boolean mouseOverLocalVar(int x, int y, int width, int height) {
		if(mouse_x > x && mouse_x < x + width) {
			if(mouse_y > y && mouse_y < y + height) {
				return true;
			}else return false;
		}else return false;
	}
	
	public boolean mouseOverWorldVar(int x, int y, int width, int height) {
		int mx = (int) (mouse_x + -cam.getX());
		int my = (int) (mouse_y + -cam.getY());
		if(mx > x && mx < x + width) {
			if(my > y && my < y + height) {
				return true;
			}else return false;
		}else return false;
	}
	
	public boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if(mx > x && mx < x + width) {
			if(my > y && my < y + height) {
				return true;
			}else return false;
		}else return false;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
}
