package game.assets.entities.enemies;

import java.awt.*;

import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.systems.gameObject.Collision;
import game.system.systems.gameObject.GameObject;
import game.enums.ID;
import game.system.systems.gameObject.Hitable;
import game.textures.Fonts;

public class Enemy extends GameObject implements Collision, Hitable {
	float max_vel = 2f;

	public Enemy(int x, int y, int z_index, ID id) {
		super(x, y, z_index, id);
	}

	public void tick() {
		buffer_x += velX;
		buffer_y += velY;
		x = Math.round(buffer_x);
		y = Math.round(buffer_y);

		Point mouse = Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, Game.world.getCam());

		int player_x = Game.world.getPlayer().getX();
		int player_y = Game.world.getPlayer().getY();

		velX += (mouse.x - x) * 0.01f;
		velY += (mouse.y - y) * 0.01f;
		velX -= (velX) * 0.03f;
		velY -= (velY) * 0.03f;

		/*velX = Helpers.clampFloat(velX, -max_vel, max_vel);
		velY = Helpers.clampFloat(velY, -max_vel, max_vel);*/
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(x-8, y-8, 16, 16);

		g.setColor(Color.green);
		Point mouse = Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, Game.world.getCam());
		g.drawLine(x, y, mouse.x, mouse.y);

		g.setColor(new Color(139, 255, 139));
		g.setFont(Fonts.default_fonts.get(5));
		g.drawString(String.valueOf(Helpers.getAngle(mouse, new Point(x, y))), mouse.x, mouse.y);
	}

	@Override
	public Rectangle getBounds() {
		//return new Rectangle(x, y, 16, 16);
		return null;
	}

	@Override
	public void hit(int damage) {

	}
}
