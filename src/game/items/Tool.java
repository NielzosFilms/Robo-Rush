package game.items;

import java.awt.image.BufferedImage;

public class Tool {

	private int amount;
	private String type;
	private int durability;
	private BufferedImage tex;
	
	public Tool(String type, int durability) {
		this.amount = 0;
		this.type = type;
		this.durability = durability;
	}
	
	public void increaseAmount(int val) {
		this.amount = this.amount + val;
	}
	
	public int getDurability() {
		return this.durability;
	}
	public void setDurability(int durability) {
		this.durability = durability;
	}
	
}
