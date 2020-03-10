package game.lighting;

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

import game.main.Camera;
import game.main.Game;
import game.main.GameObject;
import game.main.Handler;
import game.world.World;

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
		LinkedList<Light> lights = handler.getLights(world);
		LinkedList<GameObject> objects = handler.getShadowObjects(world);
		for(Light light : lights) {
			int lightMap_x = light.getX()-80;
			int lightMap_y = light.getY()-80;
			int light_x = light.getX();
			int light_y = light.getY();
			
			int light_width = light.tex.getWidth()/2;
			/*LightingMap lightMap = new LightingMap(lightMap_x, lightMap_y);
			lightMap.addPointToMap(0, 0);*/
			
			LinkedList<Point> points_found = new LinkedList<Point>();
			Polygon poly = new Polygon();
			//poly.addPoint(light_x-80, light_y-80);
			for(int x = -light_width;x<light_width;x++) {
				/*g.setColor(new Color(0, 0, 255, 50));
				g.drawLine(light_x, light_y, light_x+x, light_y-light_width);*/
				Point point = new Point(light_x+x, light_y-light_width);
				for(GameObject obj : objects) {
					if(obj.getBounds().inside(light_x, light_y)) {
						return;
					}
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
					
					Point2D point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+x, light_y-light_width), 
							new Line2D.Float(bottom_left_x, bottom_left_y, bottom_right_x, bottom_right_y));
					
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+x, light_y-light_width), 
								new Line2D.Float(top_left_x, top_left_y, bottom_left_x, bottom_left_y));
					}
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+x, light_y-light_width), 
								new Line2D.Float(top_right_x, top_right_y, bottom_right_x, bottom_right_y));
					}
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+x, light_y-light_width), 
								new Line2D.Float(top_left_x, top_left_y, top_right_x, top_right_y));
					}
					
					if(point2d != null) {
						if(point2d.getY() > point.y) point = new Point((int)point2d.getX(), (int)point2d.getY());
					}
					
				}
				/*if(x != 0)*/poly.addPoint(point.x, point.y);
				/*g.setColor(new Color(0, 0, 255, 50));
				g.drawLine(light_x, light_y, point.x, point.y);*/
				
			}
			
			for(int y = -light_width;y<light_width;y++) {
				Point point = new Point(light_x+light_width, light_y+y);
				for(GameObject obj : objects) {
					if(obj.getBounds().inside(light_x, light_y)) {
						return;
					}
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
					
					Point2D point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+light_width, light_y+y), 
							new Line2D.Float(top_left_x, top_left_y, bottom_left_x, bottom_left_y));
					
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+light_width, light_y+y), 
								new Line2D.Float(bottom_left_x, bottom_left_y, bottom_right_x, bottom_right_y));
					}
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+light_width, light_y+y), 
								new Line2D.Float(top_left_x, top_left_y, top_right_x, top_right_y));
					}
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+light_width, light_y+y), 
								new Line2D.Float(top_right_x, top_right_y, bottom_right_x, bottom_right_y));
					}
					
					if(point2d != null) {
						if(point2d.getX() < point.x) point = new Point((int)point2d.getX(), (int)point2d.getY());
					}
				}
				poly.addPoint(point.x, point.y);
				/*g.setColor(new Color(0, 0, 255, 50));
				g.drawLine(light_x, light_y, point.x, point.y);*/
			}
			
			for(int x = light_width;x>-light_width;x--) {
				Point point = new Point(light_x+x, light_y+light_width);
				for(GameObject obj : objects) {
					if(obj.getBounds().inside(light_x, light_y)) {
						return;
					}
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
					
					Point2D point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+x, light_y+light_width), 
							new Line2D.Float(top_left_x, top_left_y, top_right_x, top_right_y));
					
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+x, light_y+light_width), 
								new Line2D.Float(top_right_x, top_right_y, bottom_right_x, bottom_right_y));
					}
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+x, light_y+light_width), 
								new Line2D.Float(top_left_x, top_left_y, bottom_left_x, bottom_left_y));
					}
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x+x, light_y+light_width), 
								new Line2D.Float(bottom_left_x, bottom_left_y, bottom_right_x, bottom_right_y));
					}
					
					if(point2d != null) {
						if(point2d.getY() < point.y) point = new Point((int)point2d.getX(), (int)point2d.getY());
					}
				}
				poly.addPoint(point.x, point.y);
				/*g.setColor(new Color(0, 0, 255, 50));
				g.drawLine(light_x, light_y, point.x, point.y);*/
			}
			
			for(int y = light_width;y>-light_width;y--) {
				Point point = new Point(light_x-light_width, light_y+y);
				for(GameObject obj : objects) {
					if(obj.getBounds().inside(light_x, light_y)) {
						return;
					}
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
					
					Point2D point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x-light_width, light_y+y), 
							new Line2D.Float(top_right_x, top_right_y, bottom_right_x, bottom_right_y));
					
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x-light_width, light_y+y), 
								new Line2D.Float(bottom_left_x, bottom_left_y, bottom_right_x, bottom_right_y));
						
					}
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x-light_width, light_y+y), 
								new Line2D.Float(top_left_x, top_left_y, top_right_x, top_right_y));
					}
					if(point2d == null) {
						point2d = getLineIntersection(new Line2D.Float(light_x, light_y, light_x-light_width, light_y+y), 
								new Line2D.Float(top_left_x, top_left_y, bottom_left_x, bottom_left_y));
						
					}
					
					if(point2d != null) {
						if(point2d.getX() > point.x) point = new Point((int)point2d.getX(), (int)point2d.getY());
					}
				}
				poly.addPoint(point.x, point.y);
				/*g.setColor(new Color(0, 0, 255, 50));
				g.drawLine(light_x, light_y, point.x, point.y);*/
			}
			
			g.setColor(Color.blue);
			
			BufferedImage img = getBufferedImageMap(light.tex, poly, new Point(light_x-light_width, light_y-light_width));
			
			g.drawImage(img, light_x-light_width, light_y-light_width, null);
			
			//g.drawPolygon(poly);
		}
	}
	
	private BufferedImage getBufferedImageMap(BufferedImage img, Polygon poly, Point origin) {
		
		BufferedImage start = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		//fill screen with black
		
		BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < img2.getHeight(); y++) {
		    for (int x = 0; x < img2.getWidth(); x++) {
		    	int clr;
		    	/*if(x > origin.x && x < origin.x + 160 && y > origin.y && y < origin.y+160) {
		    		int xx = (int) (x - origin.x);
		    		int yy = (int) (y - origin.y);
		    		System.out.println(xx + " "+yy);
		    		clr = img.getRGB(xx, yy);
		    	}else {
		    		int r = 0;
			    	int g = 0;
			    	int b = 0;
			    	int a3 = 255;
			    	clr = (a3 << 24) | (r << 16) | (g << 8) | b;
		    	}*/
		    	clr = img.getRGB(x, y);
		    	
		        int  red   = (clr & 0x00ff0000) >> 16;
		        int  green = (clr & 0x0000ff00) >> 8;
		        int  blue  =  clr & 0x000000ff;
		        
		        int a = ((byte)(225) << 24) | 0x00000000;
		        int a2 = ((byte)(225) << 24) | 0x00ffffff;
		        
		        int new_color = clr & a;
		        int new_color2 = clr & a2;
		        if(!poly.contains(new Point((int)(x+origin.x), (int)(y+origin.y)))) {
		        	img2.setRGB(x, y, new_color);
		        }else {
		        	img2.setRGB(x, y, new_color2);
		        }
		        //start.setRGB(x, y, new_color2);
		    }
		}
		
		img2 = blurImage(img2);
		
		return img2;
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
