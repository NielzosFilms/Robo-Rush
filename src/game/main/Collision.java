package game.main;

import java.awt.Rectangle;

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
				for(Rectangle map_bound : ll.rectangle_bounds) {
					int playerX = (player.getBoundsRect().x + player.getBoundsRect().width)/2;
					int playerY = ((player.getBoundsRect().y + (player.getBoundsRect().height/2)));
					//Rectangle map_bound = ll.rectangle_bounds.get(i);
					
					if(player.getBoundsRect().intersects(map_bound)) {
						if(player.getY()+player.getBoundsRect().height-4 < map_bound.y) {
							player.setY(map_bound.y-player.getBoundsRect().height-3);
							onGround = true;
						}else if(player.getY() > (map_bound.y +map_bound.height-10)) {
							player.setY((map_bound.y + map_bound.height-4));
							player.setVelY(1);
						}else if(player.getX() > map_bound.x) {
							player.setX(map_bound.x+map_bound.width-12);
							player.setVelX(0);
						}else if(player.getX() < map_bound.x) {
							player.setX(map_bound.x-player.getBoundsRect().width-11);
							player.setVelX(0);
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
