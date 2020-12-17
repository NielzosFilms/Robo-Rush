package game.assets.objects.tree;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import game.assets.entities.Player;
import game.assets.items.Item;
import game.system.main.Game;
import game.system.systems.gameObject.Collision;
import game.system.systems.gameObject.GameObject;
import game.enums.ID;
import game.system.systems.gameObject.HasItem;
import game.system.systems.gameObject.Interactable;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.enums.BIOME;

public class Tree extends GameObject implements Collision, Interactable, HasItem {

	private ArrayList<ArrayList<Texture>> tex_rows;

	private BIOME biome;
	private Player player;

	private Random r = new Random();

	public Tree(int x, int y, int z_index, ID id, BIOME biome) {
		super(x, y, z_index, id);
		this.biome = biome;
		this.player = Game.world.getPlayer();

		tex_rows = new ArrayList<ArrayList<Texture>>();
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
	}

	public void render(Graphics g) {
		renderTreeTiles(g, x - 8, y - this.width, this.height);
		// g.drawRect(getBounds().x, getBounds().y, getBounds().width,
		// getBounds().height);
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

	public Rectangle getSelectBounds() {
		return new Rectangle(x, y, this.width, this.height);
	}

	public Item getItem() {
		// return new ItemWood(5, this.id, Textures.wood);
		return null;
	}

	public void interact() {
		ArrayList<Texture> row_1 = this.tex_rows.get(0);
		ArrayList<Texture> row_2 = this.tex_rows.get(1);

		this.tex_rows.clear();

		this.tex_rows.add(row_2);
		this.tex_rows.add(row_1);
	}

}
