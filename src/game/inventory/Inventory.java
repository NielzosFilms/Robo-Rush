package game.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import game.items.Item;
import game.main.Game;
import game.main.MouseInput;
import game.textures.Textures;

public class Inventory {
	
	public HashMap<Point, Item> items;
	private int size_x, size_y;
	private int x, y;
	private Textures textures;
	private MouseInput mouseInput;
	private Item mouse_holding;

	public Inventory(int size_x, int size_y, Textures textures, MouseInput mouseInput) {
		this.size_x = size_x;
		this.size_y = size_y;
		this.x = Game.WIDTH/5;
		this.y = Game.HEIGHT/2-(textures.inventory_bg.getHeight()/2);
		this.textures = textures;
		this.mouseInput = mouseInput;
		this.mouse_holding = null;
		
		this.items = new HashMap<Point, Item>();
		
		for(int y = 0;y<2;y++) {
			for(int x = 0;x<size_x;x++) {
				items.put(new Point(x, y), new Item("wood", textures.wood));
			}
		}
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g, Graphics2D g2d) {
		//g.drawImage(textures.inventory_bg, x, y, null);
		for(int y = 0;y<size_y;y++) {
			for(int x = 0;x<size_x;x++) {
				g.setColor(new Color(0, 0, 0, 127));
				g.fillRect(this.x+(x*20), this.y+(y*20), 20, 20);
				if(items.containsKey(new Point(x, y))) {
					Item item = items.get(new Point(x, y));
					item.render(g, g2d, this.x+(x*20)+2, this.y+(y*20)+2);
				}
				if(mouseInput.mouseOverLocalVar(this.x+(x*20), this.y+(y*20), 20, 20)) {
					g.setColor(new Color(255, 255, 255, 80));
					g.fillRect(this.x+(x*20), this.y+(y*20), 20, 20);
				}
				g.setColor(new Color(0, 0, 0, 200));
				g.drawRect(this.x+(x*20), this.y+(y*20), 20, 20);
			}
		}
		if(mouse_holding != null) {
			mouse_holding.render(g, g2d, mouseInput.mouse_x-8, mouseInput.mouse_y-8);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			for(int y = 0;y<size_y;y++) {
				for(int x = 0;x<size_x;x++) {
					if(mouse_holding == null) {
						if(items.containsKey(new Point(x, y))) {
							int item_x = this.x+(x*20);
							int item_y = this.y+(y*20);
							int item_w = 20;
							int item_h = 20;
							if(mouseInput.mouseOverLocalVar(item_x, item_y, item_w, item_h)) {
								Item item = items.get(new Point(x, y));
								mouse_holding = item;
								items.remove(new Point(x, y), item);
							}
						}
					}else {
						int item_x = this.x+(x*20);
						int item_y = this.y+(y*20);
						int item_w = 20;
						int item_h = 20;
						if(mouseInput.mouseOverLocalVar(item_x, item_y, item_w, item_h)) {
							if(!items.containsKey(new Point(x, y))) {
								items.put(new Point(x, y), mouse_holding);
								mouse_holding = null;
							}
						}
					}
				}
			}
		}
	}
	
	/*public void addItem(Point point, Item item) {
		this.items.put(point, item);
	}
	
	public Item getItem(Point point) {
		return this.items.get(point);
	}*/
	
}
