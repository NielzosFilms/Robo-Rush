package game.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class GameObject {

	protected BufferedImage tex;
	protected int x, y;
	protected ID id;
	protected int z_index;
	protected double velX;
	protected double velY;

	public GameObject(int x, int y, int z_index, ID id) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.z_index = z_index;
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return this.y;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public ID getId() {
		return id;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}

	public double getVelX() {
		return velX;
	}

	public double getVelY() {
		return velY;
	}

	public int getZIndex() {
		return z_index;
	}

	public void setZIndex(int z_index) {
		this.z_index = z_index;
	}

	public BufferedImage getTexture() {
		return this.tex;
	}

	public void setTexture(BufferedImage tex) {
		this.tex = tex;
	}

	public abstract Rectangle getBounds();

	public abstract Rectangle getSelectBounds();

}
