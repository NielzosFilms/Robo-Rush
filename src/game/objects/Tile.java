package game.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import game.main.GameObject;
import game.main.ID;
import game.textures.Animation;
import game.textures.Textures;
import game.world.Chunk;
import game.world.World;

public class Tile extends GameObject {
	private Random r = new Random();

	private BufferedImage tex;
	public int tex_id;
	private Textures textures;
	private String biome;

	private int text_id_bg;
	private String biome_bg;
	private BufferedImage tex_bg;

	public Tile(int x, int y, int z_index, ID id, Textures textures, String biome) {
		super(x, y, z_index, id);
		this.tex = tex;
		this.textures = textures;
		this.biome = biome;

		if (biome == "forest") {
			this.tex_id = 18;
			this.tex = textures.tileSetForestBlocks.get(this.tex_id);
		} else if (biome == "desert" || biome == "beach") {
			this.tex_id = 0;
			this.tex = textures.tileSetDesertBlocks.get(this.tex_id);
		} else if (biome == "dirt") {
			this.tex_id = 28;
			this.tex = textures.tileSetForestBlocks.get(this.tex_id);
		}else if (biome == "ocean") {
			this.tex_id = 0;
			// this.tex = textures.tileSetWaterBlocks.get(this.tex_id);
		}
	}

	public void tick() {
	}

	public void render(Graphics g) {
		// g.setColor(Color.red);
		if (biome == "ocean") {
			//water.runAnimation();
			textures.water.drawAnimation(g, x, y);
		} else {
			if (tex_bg != null) {
				if(biome_bg == "ocean") {
					//water.runAnimation();
					textures.water.drawAnimation(g, x, y);
				} else {
					g.drawImage(tex_bg, x, y, null);
				}
			}
			g.drawImage(tex, x, y, null);
		}
	}

	public int getTextureId(HashMap<Point, GameObject> chunk_tiles, Point currentPoint, int tex_id, World world,
			Chunk this_chunk) {
		boolean top = true, right = true, bottom = true, left = true;
		boolean top_left = true, top_right = true, bottom_left = true, bottom_right = true;
		int x = currentPoint.x;
		int y = currentPoint.y;

		top = checkSameNeighbourBiome(chunk_tiles, this_chunk, x, y, 0, -1);
		right = checkSameNeighbourBiome(chunk_tiles, this_chunk, x, y, 1, 0);
		bottom = checkSameNeighbourBiome(chunk_tiles, this_chunk, x, y, 0, 1);
		left = checkSameNeighbourBiome(chunk_tiles, this_chunk, x, y, -1, 0);

		top_left = checkSameNeighbourBiome(chunk_tiles, this_chunk, x, y, -1, -1);
		top_right = checkSameNeighbourBiome(chunk_tiles, this_chunk, x, y, 1, -1);
		bottom_left = checkSameNeighbourBiome(chunk_tiles, this_chunk, x, y, -1, 1);
		bottom_right = checkSameNeighbourBiome(chunk_tiles, this_chunk, x, y, 1, 1);

		if (tex_id == 18) {
			if (biome == "forest") {
				if (top && right && bottom && left) {
					if (!top_left && !bottom_right) {
						return 21;
					} else if (!top_right && !bottom_left) {
						return 22;
					} else if (!top_left) {
						return 14;
					} else if (!top_right) {
						return 12;
					} else if (!bottom_left) {
						return 2;
					} else if (!bottom_right) {
						return 0;
					}
				}

				if (!top && !right && !bottom && !left) {
					return 7;
				} else if (!top && !right) {
					return 4;
				} else if (!right && !bottom) {
					return 10;
				} else if (!bottom && !left) {
					return 9;
				} else if (!left && !top) {
					return 3;
				} else if (!top) {
					return 13;
				} else if (!right) {
					return 6;
				} else if (!bottom) {
					return 1;
				} else if (!left) {
					return 8;
				}

				//random grass tiles
				int num = r.nextInt(25);
				if(num == 0) {
					int[] ids = new int[]{20, 26, 30, 31, 32};
					int i = r.nextInt(ids.length);
					return ids[i];
				} else if(num >= 1 && num <= 4) {
					int[] ids = new int[]{19, 24, 25};
					int i = r.nextInt(ids.length);
					return ids[i];
				}
			} else if (biome == "desert") {
				return 0;
			}
		}
		return tex_id;
	}

	private Boolean checkSameNeighbourBiome(HashMap<Point, GameObject> chunk_tiles, Chunk this_chunk, int x, int y, int offset_x, int offset_y) {
		int offset_x_16 = offset_x * 16;
		int offset_y_16 = offset_y * 16;
		if (chunk_tiles.containsKey(new Point(x + offset_x_16, y + offset_y_16))) {
			Tile temp_tile = (Tile) chunk_tiles.get(new Point(x + offset_x_16, y + offset_y_16));
			if (this.biome != temp_tile.biome) {
				this.biome_bg = temp_tile.biome;
				return false;
			}
		} else {
			float[] arr = World.getHeightMapValuePoint(this_chunk.x + ((x - this_chunk.x) / 16) + offset_x,
					this_chunk.y + ((y - this_chunk.y) / 16) + offset_y);
			if (this.biome != getBiomeFromHeightMap(arr)) {
				this.biome_bg = getBiomeFromHeightMap(arr);
				return false;
			}
		}
		return true;
	}

	public static String getBiomeFromHeightMap(float[] point) {
		float osn = point[0];
		float temp_osn = point[1];
		float moist_osn = point[2];

		return World.getBiome(osn, temp_osn, moist_osn);
	}

	public void setTexture(int tex_id) {
		this.tex_id = tex_id;

		if (biome == "forest" || biome == "dirt") {
			this.tex = textures.tileSetForestBlocks.get(tex_id);
		} else if (biome == "desert" || biome == "beach") {
			this.tex = textures.tileSetDesertBlocks.get(tex_id);
		}

		// background texture
		if (biome_bg == "ocean") {
			this.tex_bg = textures.tileSetWaterBlocks.get(0); // water
		} else if (biome_bg == "desert" || biome_bg == "beach") {
			this.tex_bg = textures.tileSetDesertBlocks.get(0); // sand
		} else if(biome_bg == "dirt") {
			this.tex_bg = textures.tileSetForestBlocks.get(28); // dirt
		} else {
			this.tex_bg = null;
		}
	}

	public Rectangle getBounds() {
		/*if(this.biome == "ocean") {
			return new Rectangle(this.x+4, this.y+4, 8, 8);
		}*/
		return null;
	}

	public Rectangle getSelectBounds() {
		return null;
	}

}
