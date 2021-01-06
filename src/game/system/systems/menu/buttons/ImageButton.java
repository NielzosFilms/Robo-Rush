package game.system.systems.menu.buttons;

import game.system.main.Game;
import game.system.systems.hud.Selection;
import game.textures.Texture;

import java.awt.*;

public abstract class ImageButton extends Button {
	protected Texture texture;
	protected Selection selected = new Selection();
	public ImageButton(int x, int y, int width, int height, Texture texture) {
		super(x, y, width, height, "");
		this.texture = texture;
	}

	public void render(Graphics g) {
		g.drawImage(texture.getTexure(), x, y, width, height, null);
		this.setColor(g);
		g.fillRect(x, y, width, height);
		renderAfter(g);
	}

	public void renderAfter(Graphics g) {

	}

	public Texture getTexture() {
		return this.texture;
	}
}
