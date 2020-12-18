package game.assets.tiles;

import game.assets.tiles.tile.EdgeTextures;
import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.enums.TILE_TYPE;
import game.system.helpers.TileHelperFunctions;
import game.system.systems.gameObject.Collision;
import game.system.world.Chunk;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;

public class Tile_Grass_Plateau extends Tile implements EdgeTextures, Collision {
	private HashMap<TILE_TYPE, Texture> textures = new HashMap<>();
	public Tile_Grass_Plateau(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
		super(x, y, chunk_x, chunk_y, z_index, biome, chunk);
		textures.put(TILE_TYPE.center, new Texture(TEXTURE_LIST.grass_plateau, 1, 2));
		textures.put(TILE_TYPE.top, new Texture(TEXTURE_LIST.grass_plateau, 1, 2));
		textures.put(TILE_TYPE.bottom, new Texture(TEXTURE_LIST.grass_plateau, 1, 3));
		textures.put(TILE_TYPE.left, new Texture(TEXTURE_LIST.grass_plateau, 0, 2));
		textures.put(TILE_TYPE.right, new Texture(TEXTURE_LIST.grass_plateau, 1, 2));

		textures.put(TILE_TYPE.top_right, new Texture(TEXTURE_LIST.grass_plateau, 13, 0));
		textures.put(TILE_TYPE.bottom_right, new Texture(TEXTURE_LIST.grass_plateau, 2, 3));
		textures.put(TILE_TYPE.bottom_left, new Texture(TEXTURE_LIST.grass_plateau, 0, 3));
		textures.put(TILE_TYPE.top_left, new Texture(TEXTURE_LIST.grass_plateau, 11, 0));

		texture = textures.get(TILE_TYPE.center);
	}

	@Override
	public void findAndSetEdgeTexture() {
		this.tile_type = TileHelperFunctions.getTileType4DirTileOrBiome(this, chunk, z_index);
		if(textures.containsKey(tile_type)) {
			texture = textures.get(tile_type);
			/*if(tile_type == TILE_TYPE.bottom || tile_type == TILE_TYPE.bottom_left || tile_type == TILE_TYPE.bottom_right) {
				Tile_Grass_Plateau tile_grass_plateau = new Tile_Grass_Plateau(x, y + tileSize, chunk_x, chunk_y+1, z_index+1, biome, chunk);
				tile_grass_plateau.setTexture(new Texture(TEXTURE_LIST.grass_plateau, 1, 5));
				chunk.addTile(tile_grass_plateau);
			}*/
		}
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
	}

	@Override
	public void update() {

	}

	@Override
	public Rectangle getBounds() {
		if(tile_type == TILE_TYPE.center || tile_type == TILE_TYPE.top) {
			return null;
		}
		return new Rectangle(x, y, tileSize, tileSize);
	}
}
