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
			if(temp.getId() == ID.Player) {
				Player player = (Player) temp;
				boolean onGround = false;
				int playerX = (player.getBoundsRect().x + (player.getBoundsRect().width/2));
				int playerY = (player.getBoundsRect().y + (player.getBoundsRect().height/2));
				for(Rectangle map_bound : ll.rectangle_bounds) { //rectangle collision
					if(player.getBoundsRect().intersects(map_bound)) {
						if(player.getBoundsRect().y+player.getBoundsRect().height-13 < map_bound.y && (player.getBoundsRect().x+player.getBoundsRect().width > map_bound.x+3 && player.getBoundsRect().x < map_bound.x+map_bound.width-3)) { //player on top of bounds
							player.setY(map_bound.y-player.getBoundsRect().height-5);
							onGround = true;
						}else if(player.getBoundsRect().y > (map_bound.y +map_bound.height-10) && (player.getBoundsRect().x+player.getBoundsRect().width > map_bound.x+3 && player.getBoundsRect().x < map_bound.x+map_bound.width-3)) { //player belof bounds
							player.setY((map_bound.y + map_bound.height-6));
							player.setVelY(0);
						}else if(playerX > map_bound.x +map_bound.width/2 && (player.getBoundsRect().y+player.getBoundsRect().height > map_bound.y+5)) { // player on right of bounds
							player.setX(map_bound.x+map_bound.width-20);
							player.setVelX(0);
						}else if(playerX < map_bound.x +map_bound.width/2 && (player.getBoundsRect().y+player.getBoundsRect().height > map_bound.y+5)) { //player on left of bounds
							player.setX(map_bound.x-player.getBoundsRect().width-19);
							player.setVelX(0);
						}
					}
				}
				for(Polygon poly : ll.polygon_bounds) {
					int tempX = 0;
					int tempY = 0;
					for(int j = 0;j<poly.npoints;j++) {
						tempX += poly.xpoints[i];
						tempY += poly.ypoints[i];
					}
					int polCenX = tempX/poly.npoints;
					int polCenY = tempY/poly.npoints;
					if(poly.intersects(player.getBoundsRect())) {
						int oldXPoint = poly.xpoints[poly.npoints-1];
						int oldYPoint = poly.xpoints[poly.npoints-1];
						for(int j = 0;j<poly.npoints-1;j++) {
							int newXPoint = poly.xpoints[j];
							int newYPoint = poly.ypoints[j];
							
							int playerBottomY = player.getBoundsRect().y+player.getBoundsRect().height;
							
							if((playerBottomY > oldYPoint && playerBottomY < newYPoint+3) && playerX > oldXPoint+8) { //polygons that are oriented from top left to bottom right
								player.setY(((oldYPoint-player.getBoundsRect().height)) - (newXPoint-playerX)+5);//-(newXPoint-playerX))-5);
								onGround = true;
							}else if((oldXPoint < newXPoint) && playerX < oldXPoint+8) {
								player.setY(oldYPoint-player.getBoundsRect().height-5);
								onGround = true;
							}
							
							/*if((playerBottomY > newYPoint && playerBottomY < oldYPoint+3) && playerX < oldXPoint-8) {
								player.setY(((newYPoint-player.getBoundsRect().height)) - (oldXPoint-playerX)-5);
								onGround = true;
							}else if((oldXPoint > newXPoint) && playerX < oldXPoint-8) {
								player.setY(newXPoint-player.getBoundsRect().height-5);
								onGround = true;
							}*/
							
							oldXPoint = newXPoint;
							oldYPoint = newYPoint;
						}
					}
				}
				player.onGround = onGround;
				/*if(player.getBoundsRect().intersects(new Rectangle(0, 192, 16*9, 16*4))) {
					int playerX = (player.getBoundsRect().x + player.getBoundsRect().width)/2;
					int playerY = (player.getBoundsRect().y + player.getBoundsRect().height)/2;
					int rectX = (0 + 16*9)/2;
					int rectY = (192 + 16*4)/2;
					if(player.getY() < 192) {
						player.setY(192-player.getBoundsRect().height-3);
						player.onGround = true;
					}else if(player.getY() > (192 + 16*4)) {
						player.setY((192 + 16*4));
						player.setVelY(1);
					}else if(player.getX() > 16*4) {
						player.setX(16*9-12);
						player.setVelX(0);
					}else if(player.getX() < 0) {
						player.setX(-player.getBoundsRect().width-12);
						player.setVelX(0);
					}
				}else {
					player.onGround = false;
					System.out.println(player.getY());
				}*/
			}
		}
	}
	
}
