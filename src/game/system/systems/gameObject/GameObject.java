package game.system.systems.gameObject;

import java.awt.Graphics;
import java.io.Serializable;

import game.enums.ID;
import game.system.helpers.JsonLoader;
import game.textures.Texture;
import org.json.simple.JSONObject;

public abstract class GameObject implements Serializable {

	protected Texture tex;
	protected int x, y;
	protected int width = 16, height = 16;
	protected ID id;
	protected int z_index;
	protected float velX;
	protected float velY;
	protected float buffer_x, buffer_y;

	public GameObject(int x, int y, int z_index, ID id) {
		this.x = x;
		this.y = y;
		this.buffer_x = x;
		this.buffer_y = y;
		this.id = id;
		this.z_index = z_index;
	}

	public GameObject(JSONObject json, int z_index, int division, JsonLoader loader) {

	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public void setX(int x) {
		this.x = x;
		this.buffer_x = x;
	}

	public void setY(int y) {
		this.y = y;
		this.buffer_y = y;
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

	public Texture getTexture() {
		return this.tex;
	}

	public void setTexture(Texture tex) {
		this.tex = tex;
	}
}
