package game.lighting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.main.GameObject;
import game.main.Handler;
import game.world.World;

public class LightingSystem {

	private Handler handler;
	private World world;
	
	public LightingSystem() {
		
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		LinkedList<Light> lights = handler.getLights(world);
		LinkedList<GameObject> objects = handler.getShadowObjects(world);
		for(Light light : lights) {
			int lightMap_x = light.getX()-80;
			int lightMap_y = light.getY()-80;
			int light_x = light.getX();
			int light_y = light.getY();
			LightingMap lightMap = new LightingMap(lightMap_x, lightMap_y);
			lightMap.addPointToMap(0, 0);
			
			LinkedList<Point> points_found = new LinkedList<Point>();
			Polygon poly = new Polygon();
			poly.addPoint(light_x-80, light_y-80);
			for(int x = -80;x<80;x++) {
				g.setColor(new Color(0, 0, 255, 50));
				g.drawLine(light_x, light_y, light_x+x, light_y-80);
				Point point = new Point(light_x+x, light_y-80);
				for(GameObject obj : objects) {
					int cenX = obj.getBounds().x+(obj.getBounds().width/2);
					int cenY = obj.getBounds().y+(obj.getBounds().height/2);
					
					int top_left_x = obj.getBounds().x;
					int top_left_y = obj.getBounds().y;
					
					int top_right_x = obj.getBounds().x+obj.getBounds().width;
					int top_right_y = obj.getBounds().y;
					
					int bottom_right_x = obj.getBounds().x+obj.getBounds().width;
					int bottom_right_y = obj.getBounds().y+obj.getBounds().height;
					
					int bottom_left_x = obj.getBounds().x;
					int bottom_left_y = obj.getBounds().y+obj.getBounds().height;
					
					
					
					Point2D point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+x, light_y-80), 
							new Line2D.Float(bottom_left_x, bottom_left_y, bottom_right_x, bottom_right_y));
					
					if(point2d != null) {
						if(point2d.getY() > point.y) point = new Point((int)point2d.getX(), (int)point2d.getY());
					}
				}
				poly.addPoint(point.x, point.y);
			}
			poly.addPoint(light_x+80, light_y-80);
			poly.addPoint(light_x+80, light_y+80);
			poly.addPoint(light_x-80, light_y+80);
			
			g.setColor(Color.blue);
			
			g.drawPolygon(poly);
		}
	}
	
	public static Point2D getLineIntersection(Line2D ray, Line2D segment) {
	    if(ray.intersectsLine(segment)){
	        double rx1 = ray.getX1(),
	                ry1 = ray.getY1(),
	                rx2 = ray.getX2(),
	                ry2 = ray.getY2(),
	                sx1 = segment.getX1(),
	                sy1 = segment.getY1(),
	                sx2 = segment.getX2(),
	                sy2 = segment.getY2(),
	                rdx = rx2 - rx1,
	                rdy = ry2 - ry1,
	                sdx = sx2 - sx1,
	                sdy = sy2 - sy1,
	                t1, t2,
	                ix, iy;

	        t2 = (rdx * (sy1 - ry1) + rdy * (rx1 - sx1)) / (sdx * rdy - sdy * rdx);
	        t1 = (sx1 + sdx * t2 - rx1) / rdx;

	        if(t1 > 0/* && 1 > t2 && t2 > 0*/){
	            ix = rx1 + rdx * t1;
	            iy = ry1 + rdy * t1;
	            return new Point2D.Float((int) ix, (int) iy);
	        }else
	            return null;
	    }else
	        return null;
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
}
