package game.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Item {
	
	public int amount;
	public String type;
	private BufferedImage tex;
	
	public Item(String type, BufferedImage tex) {
		this.amount = 0;
		this.type = type;
		this.tex = tex;
		
		/*switch(this.type) {
			case "wood":
				this.tex = Textures.item;
				break;
			default:
				this.tex = Textures.item;
				break;
		}*/
	}
	
	public void render(Graphics g, int x, int y) {
		Font font = new Font("Serif", Font.PLAIN, 3);
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
	
}
