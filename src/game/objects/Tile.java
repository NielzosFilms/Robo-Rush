package game.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import game.main.GameObject;
import game.main.ID;
import game.textures.Textures;
import game.world.Chunk;
import game.world.World;

public class Tile extends GameObject{

	private BufferedImage tex;
	public int tex_id;
	
	public Tile(int x, int y, int z_index, ID id, int tex_id) {
		super(x, y, z_index, id);
		this.tex = tex;
		this.tex_id = tex_id;
		this.tex = Textures.tileSetBlocks.get(this.tex_id);
	}
	
	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawImage(tex, x, y, null);
	}

	public int getTextureId(HashMap<Point, GameObject> chunk_tiles, Point currentPoint, int tex_id, World world, Chunk this_chunk) {
		boolean top = true, right = true, bottom = true, left = true;
		boolean top_left = true, top_right = true, bottom_left = true, bottom_right = true;
		int x = currentPoint.x;
		int y = currentPoint.y;
		
		int[] grass_tex_ids = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17};
		
		if(isInArray(grass_tex_ids, tex_id)) {
		
			if(chunk_tiles.containsKey(new Point(x, y-16))) {
				Tile top_tile = (Tile)chunk_tiles.get(new Point(x, y-16));
				if(!isInArray(grass_tex_ids, top_tile.tex_id)) {
					top = false;
				}
			}else {
				float[] arr = Chunk.getHeightMapValuePoint(this_chunk.x+((x-this_chunk.x)/16), this_chunk.y+((y-this_chunk.y)/16)-1);
				int new_tex_id = getTextureIdFromHeightMap(arr);
				if(!isInArray(grass_tex_ids, new_tex_id)) {
					top = false;
				}
			}
			if(chunk_tiles.containsKey(new Point(x+16, y))) {
				Tile right_tile = (Tile)chunk_tiles.get(new Point(x+16, y));
				if(!isInArray(grass_tex_ids, right_tile.tex_id)) {
					right = false;
				}
			}else {
				float[] arr = Chunk.getHeightMapValuePoint(this_chunk.x+((x-this_chunk.x)/16)+1, this_chunk.y+((y-this_chunk.y)/16));
				int new_tex_id = getTextureIdFromHeightMap(arr);
				if(!isInArray(grass_tex_ids, new_tex_id)) {
					right = false;
				}
			}
			if(chunk_tiles.containsKey(new Point(x, y+16))) {
				Tile bottom_tile = (Tile)chunk_tiles.get(new Point(x, y+16));
				if(!isInArray(grass_tex_ids, bottom_tile.tex_id)) {
					bottom = false;
				}
			}else {
				float[] arr = Chunk.getHeightMapValuePoint(this_chunk.x+((x-this_chunk.x)/16), this_chunk.y+((y-this_chunk.y)/16)+1);
				int new_tex_id = getTextureIdFromHeightMap(arr);
				if(!isInArray(grass_tex_ids, new_tex_id)) {
					bottom = false;
				}
			}
			if(chunk_tiles.containsKey(new Point(x-16, y))) {
				Tile left_tile = (Tile)chunk_tiles.get(new Point(x-16, y));
				if(!isInArray(grass_tex_ids, left_tile.tex_id)) {
					left = false;
				}
			}else {
				float[] arr = Chunk.getHeightMapValuePoint(this_chunk.x+((x-this_chunk.x)/16)-1, this_chunk.y+((y-this_chunk.y)/16));
				int new_tex_id = getTextureIdFromHeightMap(arr);
				if(!isInArray(grass_tex_ids, new_tex_id)) {
					left = false;
				}
			}
			
			
			
			if(chunk_tiles.containsKey(new Point(x-16, y-16))) {
				Tile top_left_tile = (Tile)chunk_tiles.get(new Point(x-16, y-16));
				if(!isInArray(grass_tex_ids, top_left_tile.tex_id)) {
					top_left = false;
				}
			}else {
				float[] arr = Chunk.getHeightMapValuePoint(this_chunk.x+((x-this_chunk.x)/16)-1, this_chunk.y+((y-this_chunk.y)/16)-1);
				int new_tex_id = getTextureIdFromHeightMap(arr);
				if(!isInArray(grass_tex_ids, new_tex_id)) {
					top_left = false;
				}
			}
			if(chunk_tiles.containsKey(new Point(x+16, y-16))) {
				Tile top_right_tile = (Tile)chunk_tiles.get(new Point(x+16, y-16));
				if(!isInArray(grass_tex_ids, top_right_tile.tex_id)) {
					top_right = false;
				}
			}else {
				float[] arr = Chunk.getHeightMapValuePoint(this_chunk.x+((x-this_chunk.x)/16)+1, this_chunk.y+((y-this_chunk.y)/16)-1);
				int new_tex_id = getTextureIdFromHeightMap(arr);
				if(!isInArray(grass_tex_ids, new_tex_id)) {
					top_right = false;
				}
			}
			if(chunk_tiles.containsKey(new Point(x-16, y+16))) {
				Tile bottom_left_tile = (Tile)chunk_tiles.get(new Point(x-16, y+16));
				if(!isInArray(grass_tex_ids, bottom_left_tile.tex_id)) {
					bottom_left = false;
				}
			}else {
				float[] arr = Chunk.getHeightMapValuePoint(this_chunk.x+((x-this_chunk.x)/16)-1, this_chunk.y+((y-this_chunk.y)/16)+1);
				int new_tex_id = getTextureIdFromHeightMap(arr);
				if(!isInArray(grass_tex_ids, new_tex_id)) {
					bottom_left = false;
				}
			}
			if(chunk_tiles.containsKey(new Point(x+16, y+16))) {
				Tile bottom_right_tile = (Tile)chunk_tiles.get(new Point(x+16, y+16));
				if(!isInArray(grass_tex_ids, bottom_right_tile.tex_id)) {
					bottom_right = false;
				}
			}else {
				float[] arr = Chunk.getHeightMapValuePoint(this_chunk.x+((x-this_chunk.x)/16)+1, this_chunk.y+((y-this_chunk.y)/16)+1);
				int new_tex_id = getTextureIdFromHeightMap(arr);
				if(!isInArray(grass_tex_ids, new_tex_id)) {
					bottom_right = false;
				}
			}
			
			
			if(tex_id == 0) {
				if(top && right && bottom && left) {
					if(!top_left) {
						return 13;
					}else if(!top_right) {
						return 12;
					}else if(!bottom_left) {
						return 8;
					}else if(!bottom_right) {
						return 7;
					}
				}
				
				if(!top && !right && !bottom && !left) {
					return 9;
				} else if(!top && !right) {
					return 6;
				} else if(!right && !bottom) {
					return 11;
				} else if(!bottom && !left) {
					return 10;
				} else if(!left && !top) {
					return 5;
				} else if(!top) {
					return 3;
				} else if(!right) {
					return 4;
				} else if(!bottom) {
					return 1;
				} else if(!left) {
					return 2;
				}
			}
		}
		return tex_id;	
	}
	
	public int getTextureIdFromHeightMap(float[] point) {
		float osn = point[0];
		float temp_osn = point[1];
		float moist_osn = point[2];
		
		if((temp_osn > -0.5 && temp_osn < 0.5) && (moist_osn > 0.5)) {
			if(osn < -0.2) {
				return 14;
			} else {
				return 0;
			}
			
		}
		
		return 14;
	}
	
	private Boolean isInArray(int[] arr, int val) {
		for(int tmp : arr) {
			if(tmp == val) {
				return true;
			}
		}
		return false;
	}
	
	public void setTexture(int tex_id) {
		this.tex_id = tex_id;
		this.tex = Textures.tileSetBlocks.get(tex_id);
	}
	
}
