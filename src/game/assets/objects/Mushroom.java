package game.assets.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.assets.items.Item;
import game.system.main.GameObject;
import game.enums.ID;
import game.textures.Textures;

public class Mushroom extends GameObject {

	private BufferedImage tex;
	private Random r;

	public Mushroom(int x, int y, int z_index, ID id) {
		super(x, y, z_index, id);
		this.r = new Random();

		int num = r.nextInt(20);

		if (num == 0) {
			this.tex = Textures.mushrooms.get(3);
		} else if (num >= 1 && num <= 5) {
			this.tex = Textures.mushrooms.get(1);
		} else if (num >= 6 && num <= 10) {
			this.tex = Textures.mushrooms.get(2);
		} else if (num >= 11 && num <= 15) {
			this.tex = Textures.mushrooms.get(4);
		} else {
			this.tex = Textures.mushrooms.get(0);
		}

		// this.tex = game.textures.mushroom;
	}

	public void tick() {

	}

	public void render(Graphics g) {
		g.drawImage(this.tex, x, y, this.width, this.height, null);
	}

	public Rectangle getBounds() {
		return null;
	}

	public Rectangle getSelectBounds() {
		return new Rectangle(x + 4, y, 12, 16);
	}

	public Item getItem() {
		return null;
	}

	public void interact() {

	}

	@Override
	public void destroyed() {

	}

}
