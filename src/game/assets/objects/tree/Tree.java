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
import game.system.audioEngine.AudioFiles;
import game.system.audioEngine.AudioPlayer;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.systems.gameObject.*;
import game.enums.ID;
import game.system.systems.hitbox.HitboxContainer;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.enums.BIOME;

public class Tree extends GameObject implements Collision, Destroyable, Hitable, Health {

	private ArrayList<ArrayList<Texture>> tex_rows;

	private HealthBar health = new HealthBar(0, 0, 0, 10, 1);

	private BIOME biome;
	private Player player;

	private Random r = new Random();

	private boolean destroyedCalled;

	public Tree(int x, int y, int z_index, ID id, BIOME biome) {
		super(x, y, z_index, id);
		this.biome = biome;
		this.player = Game.world.getPlayer();

		tex_rows = new ArrayList<>();
		int tree_type = r.nextInt(2);
		tex_rows.add(new ArrayList<>());
		tex_rows.add(new ArrayList<>());

		tex_rows.get(0).add(new Texture(TEXTURE_LIST.nature_list, 0, 0));
		tex_rows.get(0).add(new Texture(TEXTURE_LIST.nature_list, 1, 0));

		tex_rows.get(1).add(new Texture(TEXTURE_LIST.nature_list, 0, 1));
		tex_rows.get(1).add(new Texture(TEXTURE_LIST.nature_list, 1, 1));

	}

	public void tick() {
		int player_cenX = player.getX() + 8;
		int player_cenY = player.getY() + (16 + 8) / 2;
		int tree_cenY = y + 8;

		if (player_cenY > tree_cenY) {
			this.setZIndex(player.getZIndex() - 1);
		} else {
			this.setZIndex(player.getZIndex() + 1);
		}

		health.setXY(x - 4, y - 8);
	}

	public void render(Graphics g) {
		renderTreeTiles(g, x - 8, y - this.width, this.height);
	}

	private void renderTreeTiles(Graphics g, int local_x, int local_y, int tile_size) {
		int r = 0;
		int c = 0;
		for (ArrayList<Texture> row : tex_rows) {
			for (Texture tex : row) {
				g.drawImage(tex.getTexure(), local_x + (c * tile_size), local_y + (r * tile_size), tile_size, tile_size, null);
				c++;
			}
			r++;
			c = 0;
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y + 3, this.width, this.height - 6);
	}

	@Override
	public void destroyed() {
		AudioPlayer.playSound(AudioFiles.tree_broken, 0.7f, false, 0);
		Game.world.getHandler().addObject(new Item_Ground((int)getBounds().getCenterX(), (int)getBounds().getCenterY(), 15, ID.Item, new Item_Stick(2)));
		Game.world.getHandler().addObject(new Item_Ground((int)getBounds().getCenterX(), (int)getBounds().getCenterY(), 15, ID.Item, new Item_Stick(2)));
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
	public void hit(HitboxContainer hitboxContainer, int hit_hitbox_index) {
		health.subtractHealth(hitboxContainer.getHitboxes().get(hit_hitbox_index).getDamage());
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
