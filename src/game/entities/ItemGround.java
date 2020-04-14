package game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.items.Item;
import game.main.GameObject;
import game.main.ID;
import game.textures.Textures;

public class ItemGround extends GameObject{

	private Textures textures;
	private Item inventory_item;
	
	private int x_diff, y_diff, timer;
	private boolean direction = false;
	
	public ItemGround(int x, int y, int z_index, ID id, Item inventory_item, Textures textures) {
		super(x, y, z_index, id);
		this.inventory_item = inventory_item;
		this.textures = textures;
	}
	
	public void tick() {
		timer++;
		
		if(timer >= 10) {
			timer = 0;
			if(direction) {
				y_diff++;
				if(y_diff >= 0) {
					direction = !direction;
				}
			}else {
				y_diff--;
				if(y_diff <= -5) {
					direction = !direction;
				}
			}
		}
	}

	public void render(Graphics g) {
		g.drawImage(textures.entity_shadow, x, y+5, null);
		g.drawImage(inventory_item.getTex(), x+x_diff, y+y_diff, null);
	}

	public Rectangle getBounds() {
		return null;
	}

	public Rectangle getSelectBounds() {
		return new Rectangle(x, y, 16, 16);
	}
	
	public Item getItem() {
		return this.inventory_item;
	}

}
