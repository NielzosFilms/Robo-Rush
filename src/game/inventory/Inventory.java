package game.inventory;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

import game.items.Item;
import game.main.Game;
import game.textures.Textures;

public class Inventory {
	
	public HashMap<Point, Item> items;
	private int size_x, size_y;
	private int x, y;
	private Textures textures;

	public Inventory(int size_x, int size_y, Textures textures) {
		this.size_x = size_x;
		this.size_y = size_y;
		this.x = Game.WIDTH/5;
		this.y = Game.HEIGHT/2-(textures.inventory_bg.getHeight()/2);
		this.textures = textures;
		
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
		g.drawImage(textures.inventory_bg, x, y, null);
		for(int y = 0;y<size_y;y++) {
			for(int x = 0;x<size_x;x++) {
				if(items.containsKey(new Point(x, y))) {
					Item item = items.get(new Point(x, y));
					item.render(g, g2d, this.x+(x*16), this.y+(y*16));
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
