package game.assets.objects.tree;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import game.assets.HealthBar;
import game.assets.entities.Player;
import game.assets.items.Item_Ground;
import game.assets.items.item.Item;
import game.assets.objects.stick.Item_Stick;
import game.audio.SoundEffect;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.systems.gameObject.*;
import game.enums.ID;
import game.system.systems.hitbox.HitboxContainer;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.enums.BIOME;
import game.textures.Textures;

public class Tree extends GameObject implements Bounds, Destroyable, Hitable, Health {

	private HealthBar health = new HealthBar(0, 0, 0, 10, 1);

	private BIOME biome;
	private Player player;

	private Random r = new Random();

	private boolean destroyedCalled;

	public Tree(int x, int y, int z_index, ID id, BIOME biome) {
		super(x, y, Game.gameController.getPlayer().getZIndex(), id);
		this.biome = biome;
		this.player = Game.gameController.getPlayer();
		int tree_type = r.nextInt(2);

	}

	public void tick() {
		health.setXY(x - 4, y - 8);
	}

	public void render(Graphics g) {
		g.drawImage(Textures.tree, x-16, y-32, null);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y + 3, this.width, this.height - 6);
	}

	@Override
	public Rectangle getTopBounds() {
		return null;
	}

	@Override
	public Rectangle getBottomBounds() {
		return null;
	}

	@Override
	public Rectangle getLeftBounds() {
		return null;
	}

	@Override
	public Rectangle getRightBounds() {
		return null;
	}

	@Override
	public void destroyed() {
		SoundEffect.tree_broken.play();
		Game.gameController.getHandler().addObject(new Item_Ground((int)getBounds().getCenterX(), (int)getBounds().getCenterY(), 15, ID.Item, new Item_Stick(2)));
		Game.gameController.getHandler().addObject(new Item_Ground((int)getBounds().getCenterX(), (int)getBounds().getCenterY(), 15, ID.Item, new Item_Stick(2)));
		destroyedCalled = true;
	}

	@Override
	public boolean destroyedCalled() {
		return destroyedCalled;
	}

	@Override
	public boolean canRemove() {
		return true;
	}

	@Override
	public void hit(int damage, int knockback_angle, float knockback, GameObject hit_by) {
		health.subtractHealth(damage);
	}

	@Override
	public int getHealth() {
		return health.getHealth();
	}

	@Override
	public HealthBar getHealthBar() {
		return health;
	}

	@Override
	public boolean dead() {
		return health.dead();
	}
}
