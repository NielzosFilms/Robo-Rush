package game.main;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.LinkedList;

import game.entities.Player;
import game.world.LevelLoader;

public class Collision {

	private Handler handler;
	private LevelLoader ll;
	
	public Collision(Handler handler, LevelLoader ll) {
		this.handler = handler;
		this.ll = ll;
	}
	
	public void tick() {
		for(LinkedList<GameObject> list : handler.object) {
			for(int i = 0;i<list.size();i++) {
				GameObject temp = list.get(i);
				
			}
		}
	}
	
}
