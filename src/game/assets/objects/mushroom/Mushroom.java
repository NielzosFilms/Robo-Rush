package game.assets.objects.mushroom;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import game.assets.items.Item;
import game.system.systems.GameObject;
import game.enums.ID;

public class Mushroom extends GameObject {
	private Random r;

	public Mushroom(int x, int y, int z_index, ID id) {
		super(x, y, z_index, id);
		this.r = new Random();

		int num = r.nextInt(20);

		/*if (num == 0) {
			this.tex = Textures.mushrooms.get(3);
		} else if (num >= 1 && num <= 5) {
			this.tex = Textures.mushrooms.get(1);
		} else if (num >= 6 && num <= 10) {
			this.tex = Textures.mushrooms.get(2);
		} else if (num >= 11 && num <= 15) {
			this.tex = Textures.mushrooms.get(4);
		} else {
			this.tex = Textures.mushrooms.get(0);
		}*/
		this.tex = null;

		// this.tex = game.textures.mushroom;
	}

	public void tick() {

	}

	public void render(Graphics g) {
		//g.drawImage(this.tex, x, y, this.width, this.height, null);
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

	@Override
	public void hit(int damage) {

	}

}
