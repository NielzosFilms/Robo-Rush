package game.main;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;

import game.entities.Player;

public class Collision {

	private Handler handler;
	private LevelLoader ll;
	
	public Collision(Handler handler, LevelLoader ll) {
		this.handler = handler;
		this.ll = ll;
	}
	
	public void tick() {
		for(int i = 0;i<handler.object.size();i++) {
			GameObject temp = handler.object.get(i);
			
		}
	}
	
}
