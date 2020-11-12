package game.system.lighting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.LinkedList;

import game.system.main.Camera;
import game.system.main.Game;
import game.system.main.GameObject;
import game.system.main.Handler;
import game.system.world.World;

public class LightingSystem {

	private Handler handler;
	private World world;
	private Camera cam;
	
	public LightingSystem() {
		
	}
	
	public void setCam(Camera cam) {
		this.cam = cam;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		
		BufferedImage overlay = getBufferedImageMap(null, null, null);
		g.drawImage(overlay, 0, 0, null);
		
		LinkedList<Light> lights = handler.getLights(world);
		LinkedList<GameObject> objects = handler.getShadowObjects(world);
		for(Light light : lights) {
//			int lightMap_x = light.getX()-80;
//			int lightMap_y = light.getY()-80;
//			int light_x = light.getX();
//			int light_y = light.getY();
//			
//			int light_width = light.tex.getWidth()/2;
//			Polygon poly = new Polygon();
//			
//			LinkedList<Point> tmp_point = createRays(light_x, light_y, light_width, objects, g);
//			LinkedList<Point> points_sorted = new LinkedList<Point>();
//			Point tmp_top_left;
//			Point tmp_top_right;
//			Point tmp_bottom_right;
//			Point tmp_bottom_left;
//			
//			g.setColor(Color.red);
//			g.drawRect(light_x, light_y, 1, 1);
//			
//			for(Point tmp : tmp_point) {
//				g.setColor(Color.blue);
//				if(tmp != null) g.drawRect(tmp.x, tmp.y, 1, 1);
//			}
			
			/*while(tmp_point.size() > 0) {
				for(Point tmp : tmp_point) {
					if(tmp.y < light_y) {
						if(tmp.x < light_x) {
							
						}else {
							
						}
					}else {
						if(tmp.x < light_x) {
							
						}else {
							
						}
					}
				}
			}*/
			//if(tmp_point != null) {
				/*if(x != 0)*///poly.addPoint(tmp_point.x, tmp_point.y);
				//points_found.add(tmp_point);
			//}
			
			g.setColor(Color.blue);
			
			//BufferedImage img = getBufferedImageMap(light.tex, poly, new Point(light_x-light_width, light_y-light_width));
			
			//g.drawImage(img, (int)-cam.getX(), (int)-cam.getY(), null);
			
			//g.drawPolygon(poly);
		}
	}
	
	private LinkedList<Point> createRays(int light_x, int light_y, int light_half_width, LinkedList<GameObject> objects, Graphics g) {
		LinkedList<Point> points_found = new LinkedList<Point>();
		
		Line2D ray_top_left_corner = new Line2D.Float(light_x, light_y, light_x-light_half_width, light_y-light_half_width);
		Line2D ray_top_right_corner = new Line2D.Float(light_x, light_y, light_x+light_half_width, light_y-light_half_width);
		Line2D ray_bottom_right_corner = new Line2D.Float(light_x, light_y, light_x+light_half_width, light_y+light_half_width);
		Line2D ray_bottom_left_corner = new Line2D.Float(light_x, light_y, light_x-light_half_width, light_y+light_half_width);
		
		for(GameObject obj : objects) {
			if(obj.getBounds().inside(light_x, light_y)) {
				break;
			}
			
			int offset = 1;
			
			Point origin = new Point(light_x-light_half_width, light_y-light_half_width);
			Point frame_bottom_right = new Point(light_x+light_half_width, light_y+light_half_width);
			Point light = new Point(light_x, light_y);
			
			Point center = new Point(obj.getBounds().x+(obj.getBounds().width/2), obj.getBounds().y+(obj.getBounds().height/2));
			if(!checkInBounds(center.x, center.y, origin.x, origin.y, light_half_width*2, light_half_width*2)) {
				continue;
			}
			Point top_left = new Point(obj.getBounds().x, obj.getBounds().y);
			Point top_right = new Point(obj.getBounds().x+obj.getBounds().width, obj.getBounds().y);
			Point bottom_right = new Point(obj.getBounds().x+obj.getBounds().width, obj.getBounds().y+obj.getBounds().height);
			Point bottom_left = new Point(obj.getBounds().x, obj.getBounds().y+obj.getBounds().height);
			
			Line2D.Float ray_top_left = new Line2D.Float(light_x, light_y, top_left.x, top_left.y);
			
			Point top_left_offset = getRayFramePoint(light, origin, frame_bottom_right, top_left, offset);
			Point top_right_offset = getRayFramePoint(light, origin, frame_bottom_right, top_right, offset);
			
			
			Line2D.Float ray_top_left_offset = new Line2D.Float(light_x, light_y, top_left_offset.x, top_left_offset.y); //these lines dont continue to the frame border
			Line2D.Float ray_top_right_offset2 = new Line2D.Float(light_x, light_y, top_right_offset.x, top_right_offset.y);
			
			g.setColor(Color.yellow);
			g.drawLine((int)ray_top_left.getX1(), (int)ray_top_left.getY1(), (int)ray_top_left.getX2(), (int)ray_top_left.getY2());
			g.setColor(Color.green);
			g.drawLine((int)ray_top_left_offset.getX1(), (int)ray_top_left_offset.getY1(), (int)ray_top_left_offset.getX2(), (int)ray_top_left_offset.getY2());
			
			g.setColor(Color.green);
			g.drawLine((int)ray_top_right_offset2.getX1(), (int)ray_top_right_offset2.getY1(), (int)ray_top_right_offset2.getX2(), (int)ray_top_right_offset2.getY2());
			
			Line2D.Float ray_top_right = new Line2D.Float(light_x, light_y, top_right.x, top_right.y);
			Line2D.Float ray_top_right_offset = new Line2D.Float(light_x, light_y, top_right.x+offset, top_right.y-offset);
			
			Line2D.Float ray_bottom_right = new Line2D.Float(light_x, light_y, bottom_right.x, bottom_right.y);
			Line2D.Float ray_bottom_right_offset = new Line2D.Float(light_x, light_y, bottom_right.x+offset, bottom_right.y+offset);
			
			Line2D.Float ray_bottom_left = new Line2D.Float(light_x, light_y, bottom_left.x, bottom_left.y);
			Line2D.Float ray_bottom_left_offset = new Line2D.Float(light_x, light_y, bottom_left.x-offset, bottom_left.y+offset);
			
			Line2D.Float obj_top = new Line2D.Float(top_left.x, top_left.y, top_right.x, top_right.y);
			Line2D.Float obj_right = new Line2D.Float(top_right.x, top_right.y, bottom_right.x, bottom_right.y);
			Line2D.Float obj_bottom = new Line2D.Float(bottom_right.x, bottom_right.y, bottom_left.x, bottom_left.y);
			Line2D.Float obj_left = new Line2D.Float(bottom_left.x, bottom_left.y, top_left.x, top_left.y);
			
			Line2D.Float frame_top = new Line2D.Float(light_x-light_half_width, light_y-light_half_width, light_x+light_half_width, light_y-light_half_width);
			Line2D.Float frame_right = new Line2D.Float(light_x+light_half_width, light_y-light_half_width, light_x+light_half_width, light_y+light_half_width);
			Line2D.Float frame_bottom = new Line2D.Float(light_x-light_half_width, light_y+light_half_width, light_x+light_half_width, light_y+light_half_width);
			Line2D.Float frame_left = new Line2D.Float(light_x-light_half_width, light_y-light_half_width, light_x-light_half_width, light_y+light_half_width);
			
			Point point_top_left = getPointWithEverySide(ray_top_left, obj_top, obj_right, obj_bottom, obj_left, light);
			Point point_top_left_offset = getPointWithEverySideOffset(ray_top_left_offset, obj_top, obj_right, obj_bottom, obj_left, frame_top, frame_right, frame_bottom, frame_left, light);
			
			Point point_top_right = getPointWithEverySide(ray_top_right, obj_top, obj_right, obj_bottom, obj_left, light);
			Point point_top_right_offset = getPointWithEverySideOffset(ray_top_right_offset, obj_top, obj_right, obj_bottom, obj_left, frame_top, frame_right, frame_bottom, frame_left, light);
			
			Point point_bottom_right = getPointWithEverySide(ray_bottom_right, obj_top, obj_right, obj_bottom, obj_left, light);
			Point point_bottom_right_offset = getPointWithEverySideOffset(ray_bottom_right_offset, obj_top, obj_right, obj_bottom, obj_left, frame_top, frame_right, frame_bottom, frame_left, light);
			
			Point point_bottom_left = getPointWithEverySide(ray_bottom_left, obj_top, obj_right, obj_bottom, obj_left, light);
			Point point_bottom_left_offset = getPointWithEverySideOffset(ray_bottom_left_offset, obj_top, obj_right, obj_bottom, obj_left, frame_top, frame_right, frame_bottom, frame_left, light);
			
			points_found.add(point_top_left);
			points_found.add(point_top_left_offset);
			points_found.add(point_top_right);
			points_found.add(point_top_right_offset);
			points_found.add(point_bottom_right);
			points_found.add(point_bottom_right_offset);
			points_found.add(point_bottom_left);
			points_found.add(point_bottom_left_offset);
		}
		
		return points_found;
	}
	
	public Point getRayFramePoint(Point source, Point frame_top_left, Point frame_bottom_right, Point object, int offset) {
	    float angle = (float) Math.toDegrees(Math.atan2(Math.abs(object.y - source.y), Math.abs(object.x - source.x)));
	    
	    Point point_out = null;
	    
	    if(object.x < source.x && object.y < source.y) { 
	    	//top left
	    	float out = (float) ((Math.tan(Math.toRadians(angle))) * (frame_top_left.x - object.x -offset));
	    	point_out = new Point(frame_top_left.x, (int) (object.y+out));
	    	
	    }else if(object.x > source.x && object.y < source.y) { 
	    	//top right
	    	float out = (float) ( (Math.tan(Math.toRadians(angle))) * (object.x - frame_bottom_right.x +offset) );
	    	point_out = new Point(frame_bottom_right.x, (int) (object.y+out));
	    	
	    }else if(object.x > source.x && object.y > source.y) {
	    	//bottom right
	    	float out = (float) ( (Math.tan(Math.toRadians(angle))) * (frame_bottom_right.x - object.x -offset) );
	    	point_out = new Point(frame_bottom_right.x, (int) (object.y+out));
	    	
	    }else if(object.x < source.x && object.y > source.y) {
	    	//bottom left
	    	float out = (float) ( (Math.tan(Math.toRadians(angle))) * (object.x - frame_top_left.x -offset) );
	    	point_out = new Point(frame_top_left.x, (int) (object.y+out));
	    	
	    }else {
	    	point_out = frame_top_left;
	    }
	    
	    /*
	     * werkt alleen links onder
	     * hoef alleen iets met de angle te veranderen als je rechtsboven gaat staan is het 180 graden;
	     * of de out van mekaar aftrekken aanpassen
	     */

	    return point_out;
	}
	
	private boolean checkInBounds(int x, int y, int bx, int by, int bw, int bh) {
		return (x > bx && x < bx+bw && y > by && y < by+bh);
	}
	
	private float getDistance(Point p1, Point p2){
		float dis = (float) Math.sqrt((p2.x-p1.x)*(p2.x-p1.x) + (p2.y-p1.y)*(p2.y-p1.y));
		return dis;
	}
	
	private Point getPointWithEverySide(Line2D.Float ray, Line2D.Float top, Line2D.Float right, Line2D.Float bottom, Line2D.Float left, Point light) {
		Point2D tmp_top = getLineIntersection(ray, top);
		Point point_top = null;
		if(tmp_top != null) point_top = new Point((int)tmp_top.getX(),(int)tmp_top.getY());
		
		Point2D tmp_right = getLineIntersection(ray, right);
		Point point_right = null;
		if(tmp_right != null) point_right = new Point((int)tmp_right.getX(),(int)tmp_right.getY());
		
		Point2D tmp_bottom = getLineIntersection(ray, bottom);
		Point point_bottom = null;
		if(tmp_bottom != null) point_bottom = new Point((int)tmp_bottom.getX(),(int)tmp_bottom.getY());
		
		Point2D tmp_left = getLineIntersection(ray, left);
		Point point_left = null;
		if(tmp_left != null) point_left = new Point((int)tmp_left.getX(),(int)tmp_left.getY());
		
		Point[] points = {point_top, point_right, point_bottom, point_left};
		
		
		float tmp_dis = 0;
		Point point = null;
		for(Point p : points) {
			if(p == null) continue;
			if(tmp_dis == 0.0f) {
				tmp_dis = getDistance(light, p);
				point = p;
			}else {
				if(getDistance(light, p) < tmp_dis) {
					tmp_dis = getDistance(light, p);
					point = p;
				}
			}
		}
		return point;
	}
	
	private Point getPointWithEverySideOffset(Line2D.Float ray, Line2D.Float top, Line2D.Float right, Line2D.Float bottom, Line2D.Float left, 
			Line2D.Float frame_top, Line2D.Float frame_right, Line2D.Float frame_bottom, Line2D.Float frame_left, 
			Point light) {
		Point point_object = getPointWithEverySide(ray, top, right, bottom, left, light);
		float object_dis = 0;
		if(point_object != null) object_dis = getDistance(light, point_object);
		
		Point point_frame = getPointWithEverySide(ray, frame_top, frame_right, frame_bottom, frame_left, light);
		float frame_dis = 0;
		if(point_frame != null) frame_dis = getDistance(light, point_frame);
		
		if(point_object == null) {
			return point_frame;
		}
		
		if(object_dis < frame_dis) {
			return point_object;
		}
		return point_frame;
	}
	
	private BufferedImage getBufferedImageMap(BufferedImage img, Polygon poly, Point origin) {
		
		BufferedImage start = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		//fill screen with black
		
		for (int y = 0; y < start.getHeight(); y++) {
		    for (int x = 0; x < start.getWidth(); x++) {
		    	int light_color = 0;
		    	
//		    	int light_screen_x =  origin.x - (int)-cam.getX();
//		    	int light_screen_y = origin.y - (int)-cam.getY();
//	    		if(x-light_screen_x >= 0 && x-light_screen_x < img.getWidth() && y-light_screen_y >= 0 && y-light_screen_y < img.getHeight()) {
//	    			light_color = img.getRGB(x-light_screen_x, y-light_screen_y); //min iets om vanaf 0 te beginnen
//	    			//System.out.println(y-light_screen_y);
//	    		}
		    	
		    	int clr = (225 << 24) | (0 << 16) | (0 << 8) | 0;
		        
		        int a = ((byte)(225) << 24) | 0x00000000;
		        int a2 = ((byte)(225) << 24) | 0x00ffffff;
		        
		        int new_color = clr & a;
		        int new_color2 = clr & a2;
//		        if(x-light_screen_x >= 0 && x-light_screen_x < img.getWidth() && y-light_screen_y >= 0 && y-light_screen_y < img.getHeight()) {
//		        	int color = light_color & a2;
//		        	if(!poly.contains(new Point((int)(x-light_screen_x+origin.x), (int)(y-light_screen_y+origin.y)))) {
//		        		start.setRGB(x, y, new_color2);
//		        	}else {
//		        		start.setRGB(x, y, color);
//		        	}
//		        }else {
//		        	start.setRGB(x, y, new_color2);
//		        }
		        start.setRGB(x, y, new_color2);
		        //start.setRGB(x, y, new_color2);
		    }
		}
		
		start = blurImage(start);
		
		return start;
		
//		BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		
//		for (int y = 0; y < img2.getHeight(); y++) {
//		    for (int x = 0; x < img2.getWidth(); x++) {
//		    	int clr;
//		    	/*if(x > origin.x && x < origin.x + 160 && y > origin.y && y < origin.y+160) {
//		    		int xx = (int) (x - origin.x);
//		    		int yy = (int) (y - origin.y);
//		    		System.out.println(xx + " "+yy);
//		    		clr = img.getRGB(xx, yy);
//		    	}else {
//		    		int r = 0;
//			    	int g = 0;
//			    	int b = 0;
//			    	int a3 = 255;
//			    	clr = (a3 << 24) | (r << 16) | (g << 8) | b;
//		    	}*/
//		    	clr = img.getRGB(x, y);
//		    	
//		        int  red   = (clr & 0x00ff0000) >> 16;
//		        int  green = (clr & 0x0000ff00) >> 8;
//		        int  blue  =  clr & 0x000000ff;
//		        
//		        int a = ((byte)(225) << 24) | 0x00000000;
//		        int a2 = ((byte)(225) << 24) | 0x00ffffff;
//		        
//		        int new_color = clr & a;
//		        int new_color2 = clr & a2;
//		        if(!poly.contains(new Point((int)(x+origin.x), (int)(y+origin.y)))) {
//		        	img2.setRGB(x, y, new_color);
//		        }else {
//		        	img2.setRGB(x, y, new_color2);
//		        }
//		        //start.setRGB(x, y, new_color2);
//		    }
//		}
//		
//		img2 = blurImage(img2);
//		
//		return img2;
	}
	
	private BufferedImage blurImage(BufferedImage img) {
		int radius = 4;
	    int size = radius * 2 + 1;
	    float weight = 1.0f / (size * size);
	    float[] data = new float[size * size];

	    for (int i = 0; i < data.length; i++) {
	        data[i] = weight;
	    }

	    Kernel kernel = new Kernel(size, size, data);
	    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	    //tbi is BufferedImage
	    BufferedImage i = op.filter(img, null);
		
		return i;
	}
	
	public static Point2D getLineIntersection(Line2D.Float ray, Line2D.Float segment) {
	    if(ray.intersectsLine(segment)){
	    	final double x1,y1, x2,y2, x3,y3, x4,y4;
	        x1 = ray.x1; y1 = ray.y1; x2 = ray.x2; y2 = ray.y2;
	        x3 = segment.x1; y3 = segment.y1; x4 = segment.x2; y4 = segment.y2;
	        final double x = (
	                (x2 - x1)*(x3*y4 - x4*y3) - (x4 - x3)*(x1*y2 - x2*y1)
	                ) /
	                (
	                (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4)
	                );
	        final double y = (
	                (y3 - y4)*(x1*y2 - x2*y1) - (y1 - y2)*(x3*y4 - x4*y3)
	                ) /
	                (
	                (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4)
	                );

	        return new Point2D.Double(x, y);
	    	
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
