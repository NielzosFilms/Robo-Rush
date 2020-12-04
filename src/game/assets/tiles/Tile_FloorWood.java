package game.assets.tiles;

import game.enums.BIOME;
import game.enums.TEXTURE_LIST;
import game.system.helpers.TileHelperFunctions;
import game.system.world.Chunk;
import game.textures.Texture;

import java.awt.*;

public class Tile_FloorWood extends Tile {
	private boolean
			top = true,
			right = true,
			bottom = true,
			left = true;

	private final Texture
			tex_top_left = new Texture(TEXTURE_LIST.floorTiles_list, 0, 0),
			tex_top = new Texture(TEXTURE_LIST.floorTiles_list, 1, 0),
			tex_top_right = new Texture(TEXTURE_LIST.floorTiles_list, 2, 0),
			tex_left = new Texture(TEXTURE_LIST.floorTiles_list, 0, 1),
			tex_center = new Texture(TEXTURE_LIST.floorTiles_list, 1, 1),
			tex_right = new Texture(TEXTURE_LIST.floorTiles_list, 2, 1),
			tex_bot_left = new Texture(TEXTURE_LIST.floorTiles_list, 0, 2),
			tex_bot = new Texture(TEXTURE_LIST.floorTiles_list, 1, 2),
			tex_bot_right = new Texture(TEXTURE_LIST.floorTiles_list, 2, 2);

	public Tile_FloorWood(int x, int y, int chunk_x, int chunk_y, int z_index, Chunk chunk) {
		super(x, y, chunk_x, chunk_y, z_index, null, chunk);
		//chunk.updateSameTiles(this);
	}

	public void render(Graphics g) {
		g.drawImage(texture.getTexure(), x, y, 16, 16, null);
	}

	public Rectangle getBounds() {
		return null;
	}

	public Rectangle getSelectBounds() {
		return null;
	}

	public void findAndSetEdgeTexture(int tilemap_index) {
		top = TileHelperFunctions.checkSameNeighbourTile(this, chunk, 0, -1, tilemap_index);
		right = TileHelperFunctions.checkSameNeighbourTile(this, chunk, 1, 0, tilemap_index);
		bottom = TileHelperFunctions.checkSameNeighbourTile(this, chunk, 0, 1, tilemap_index);
		left = TileHelperFunctions.checkSameNeighbourTile(this, chunk, -1, 0, tilemap_index);

		texture = tex_center;

		if(connections() >= 2) {
			if (!top && !right && !bottom && !left) {
				texture = tex_center;
			} else if (!top && !right) {
				texture = tex_top_right;
			} else if (!top && !left) {
				texture = tex_top_left;
			} else if (!bottom && !right) {
				texture = tex_bot_right;
			} else if (!bottom && !left) {
				texture = tex_bot_left;
			} else if (!top) {
				texture = tex_top;
			} else if (!bottom) {
				texture = tex_bot;
			} else if (!left) {
				texture = tex_left;
			} else if (!right) {
				texture = tex_right;
			}
		}
	}

	private int connections() {
		int ret = 0;
		if(top) ret += 1;
		if(right) ret += 1;
		if(bottom) ret += 1;
		if(left) ret += 1;
		return ret;
	}

	public void update(int tilemap_index) {
		findAndSetEdgeTexture(tilemap_index);
	}
}
