package game.system.main;

import java.util.LinkedList;

import game.assets.entities.Player;
import game.system.world.Chunk;
import game.system.world.World;

public class Collision {

	private Handler handler;
	private World world;
	private Player player;

	public Collision(Handler handler, World world, Player player) {
		this.handler = handler;
		this.world = world;
		this.player = player;
	}

	public void tick() {
		LinkedList<GameObject> objects = new LinkedList<GameObject>();
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
		for (Chunk chunk : chunks_on_screen) {
			LinkedList<GameObject> chunk_content = chunk.getEntities();
			for (GameObject obj : chunk_content) {
				objects.add(obj);
			}
		}
		for (LinkedList<GameObject> list : handler.object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject temp = list.get(i);
				objects.add(temp);
			}
		}

		for (GameObject temp : objects) {
			if (temp.getBounds() != null) {
				if (temp.getBounds().intersects(player.getBounds())) {
					int player_x = player.getBounds().x;
					int player_y = player.getBounds().y;
					int player_x_diff = player.getBounds().x - player.getX();
					int player_y_diff = player.getBounds().y - player.getY();
					int player_cenX = player_x + (player.getBounds().width / 2);
					int player_cenY = player_y + (player.getBounds().height / 2);
					int player_bottomY = player_y + player.getBounds().height;
					int player_rightX = player_x + player.getBounds().width;
					int obj_x = temp.getBounds().x;
					int obj_y = temp.getBounds().y;
					int obj_x_diff = temp.getBounds().x - temp.getX();
					int obj_y_diff = temp.getBounds().y - temp.getY();
					int obj_cenX = obj_x + (temp.getBounds().width / 2);
					int obj_cenY = obj_y + (temp.getBounds().height / 2);
					int obj_bottomY = obj_y + temp.getBounds().height;
					int obj_rightX = obj_x + temp.getBounds().width;

					if (player_cenY < obj_cenY && player_bottomY < obj_y + 4) {
						player.setY(obj_y - player.getBounds().height - player_y_diff);
					} else if (player_cenY > obj_cenY && player_y > obj_bottomY - 4) {
						player.setY(obj_y + temp.getBounds().height - player_y_diff);
					} else if (player_cenX < obj_cenX && player_rightX < obj_x + 4) {
						player.setX(obj_x - player.getBounds().width - player_x_diff);
					} else if (player_cenX > obj_cenX && player_x > obj_rightX - 4) {
						player.setX(obj_x + temp.getBounds().width - player_x_diff);
					}
				}
			}
		}

	}

}
