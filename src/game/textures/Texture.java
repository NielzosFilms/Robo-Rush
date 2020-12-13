package game.textures;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Texture implements Serializable {
	private TEXTURE_LIST texture_list;
	private int x, y;
	private boolean coords_touched = false;
	private int index;

	public Texture(TEXTURE_LIST texture_list, int x, int y) {
		this.texture_list = texture_list;
		this.x = x;
		this.y = y;
		this.coords_touched = true;
	}

	public Texture(TEXTURE_LIST texture_list, int index) {
		this.texture_list = texture_list;
		this.index = index;
	}

	public TEXTURE_LIST getTexture_list() {
		return texture_list;
	}
	public void setTexture_list(TEXTURE_LIST texture_list) {
		this.texture_list = texture_list;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public BufferedImage getTexure() {
		return Textures.getTexture(this);
	}

	public boolean coordsTouched() {
		return coords_touched;
	}

	public int getIndex() {
		return index;
	}
}
