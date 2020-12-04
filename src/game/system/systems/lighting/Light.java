package game.system.systems.lighting;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Light {

	private Point pos;
	public BufferedImage tex;
	
	public Light(Point pos, BufferedImage tex) {
		this.pos = pos;
		this.tex = tex;
	}
	
	public void setPosition(Point pos) {
		this.pos = pos;
	}
	
	public Point getPosition(){
		return this.pos;
	}
	
	public int getX() {
		return this.pos.x;
	}
	
	public int getY() {
		return this.pos.y;
	}
	
}
