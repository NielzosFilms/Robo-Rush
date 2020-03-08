package game.lighting;

import java.awt.Polygon;

public class LightingMap {

	public Polygon poly_map;
	private int x, y;
	
	public LightingMap(int x, int y) {
		this.x = x;
		this.y = y;
		this.poly_map = new Polygon();
	}
	
	public void addPointToMap(int x, int y) {
		poly_map.addPoint(x, y);
	}
	
	public void createLightMap() {
		
	}
	
}
