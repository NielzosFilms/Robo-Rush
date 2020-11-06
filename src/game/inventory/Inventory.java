package game.inventory;

import game.main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Inventory {
	private final int slot_w = InventorySystem.slot_w, slot_h = InventorySystem.slot_h;
	private int x = 100, y = 100;
	private int size_x, size_y;
	private ArrayList<InventorySlot> slots = new ArrayList<>();

	public Inventory(int size_x, int size_y) {
		this.size_x = size_x;
		this.size_y = size_y;

		for(int y = 0; y < size_y; y++) {
			for(int x = 0; x < size_x; x++) {
				// translate x, y to screen coords
				slots.add(new InventorySlot(this.x, this.y,x + x * slot_w, y + y * slot_h));
			}
		}
	}

	public void tick() {}

	public void render(Graphics g) {
		for(InventorySlot slot : slots) {
			slot.render(g);
		}
	}

	public void mouseLeftClick(MouseEvent e) {
	}
	public void mouseMiddleClick(MouseEvent e) {
	}
	public void mouseRightClick(MouseEvent e) {
	}

	public Rectangle getInventoryBounds() {
		int width = size_x * slot_w;
		int height = size_y * slot_h;
		return new Rectangle(x, y, width, height);
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
		for(InventorySlot slot : slots) {
			slot.setInvXY(this.x, this.y);
		}
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
}
