package game.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.GameObject;
import game.textures.Textures;

public class Item {
	
	public int amount;
	public String type;
	private BufferedImage tex;
	private Textures textures;
	private GameObject object;
	
	public Item(GameObject object, String type, int amount, Textures textures) {
		this.amount = amount;
		this.type = type;
		this.object = object;
		
		switch(this.type) {
			case "Mushroom":
				this.tex = textures.mushrooms.get(2);
				break;
			default:
				this.tex = textures.wood;
				break;
		}
	}
	
	public void render(Graphics g, int x, int y) {
		Font font = new Font("SansSerif", Font.PLAIN, 3);
		g.setColor(Color.white);
		g.drawImage(this.tex, x, y, null);
		g.setFont(font);
		g.drawString(""+this.amount, x+13, y+13);
	}
	
	public void increaseAmount(int val) {
		this.amount = this.amount + val;
	}
	
	public BufferedImage getTex() {
		return this.tex;
	}
	
	public GameObject getObject() {
		return this.object;
	}
	
}
