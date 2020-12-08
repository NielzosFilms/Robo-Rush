package game.system.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.awt.Point;

import game.assets.tiles.Tile;
import game.enums.BIOME;
import game.enums.TILE_TYPE;
import game.system.main.Game;
import game.system.world.Chunk;
import game.system.world.World;

public class TileHelperFunctions {
    private static Random r = new Random();

    public static boolean checkSameNeighbourTileOrBiome(Tile tile, Chunk this_chunk, int offset_x, int offset_y, int tilemap_index) {
        int x = tile.getChunkX(), y = tile.getChunkY();
        HashMap<Point, Tile> chunk_tiles = this_chunk.getTileMap(tilemap_index);
        if (chunk_tiles.containsKey(new Point(x + offset_x, y + offset_y))) {
            // Checks same chunk tiles to same tileclass
            Tile temp_tile = (Tile) chunk_tiles.get(new Point(x + offset_x, y + offset_y));
            if(tile.getClass() == temp_tile.getClass()) return true;
        } else {
            Chunk neighbour = null;
            int tmp_x = x + offset_x;
            int tmp_y = y + offset_y;

            if(tmp_x < 0 && tmp_y < 0 || tmp_x > 15 && tmp_y > 15 ||
                tmp_x < 0 && tmp_y > 15 || tmp_x > 15 && tmp_y < 0) {
                neighbour = this_chunk.getNeighbourChunk(offset_x, offset_y);
            } else if(tmp_x < 0 || tmp_x > 15) {
                neighbour = this_chunk.getNeighbourChunk(offset_x, 0);
            } else if(tmp_y < 0 || tmp_y > 15) {
                neighbour = this_chunk.getNeighbourChunk(0, offset_y);
            }
            if(tmp_x < 0) tmp_x = 15;
            if(tmp_x > 15) tmp_x = 0;
            if(tmp_y < 0) tmp_y = 15;
            if(tmp_y > 15) tmp_y = 0;
            if(neighbour != null) {
                // Checks neighbour chunk tiles to same tileclass
                if(neighbour.getTileMap(tilemap_index).containsKey(new Point(tmp_x, tmp_y))) {
                    Tile temp_tile = (Tile) neighbour.getTileMap(tilemap_index).get(new Point(tmp_x, tmp_y));
                    if(tile.getClass() == temp_tile.getClass()) return true;
                }
            } else {
                // Checks neighbour chunk coords with generated biome value to same tile biome
                float[] arr = Game.world.getGeneration().getHeightMapValuePoint(this_chunk.x + x + offset_x, this_chunk.y + y + offset_y);
                return tile.getBiome() == getBiomeFromHeightMap(arr);
            }
        }
        return false;
    }

    public static BIOME getBiomeFromHeightMap(float[] point) {
        float osn = point[0];
        float temp_osn = point[1];
        float moist_osn = point[2];

        return Game.world.getGeneration().getBiome(osn, temp_osn, moist_osn);
    }

    public static boolean checkSameNeighbourTile(Tile tile, Chunk this_chunk, int offset_x, int offset_y, int tilemap_index) {
        int x = tile.getChunkX(), y = tile.getChunkY();
        HashMap<Point, Tile> chunk_tiles = this_chunk.getTileMap(tilemap_index);
        if (chunk_tiles.containsKey(new Point(x + offset_x, y + offset_y))) {
            // Checks same chunk tiles to same tileclass
            Tile temp_tile = (Tile) chunk_tiles.get(new Point(x + offset_x, y + offset_y));
            if(tile.getClass() == temp_tile.getClass()) return true;
        } else {
            Chunk neighbour = null;
            int tmp_x = x + offset_x;
            int tmp_y = y + offset_y;

            if(tmp_x < 0 && tmp_y < 0 || tmp_x > 15 && tmp_y > 15 ||
                    tmp_x < 0 && tmp_y > 15 || tmp_x > 15 && tmp_y < 0) {
                neighbour = this_chunk.getNeighbourChunk(offset_x, offset_y);
            } else if(tmp_x < 0 || tmp_x > 15) {
                neighbour = this_chunk.getNeighbourChunk(offset_x, 0);
            } else if(tmp_y < 0 || tmp_y > 15) {
                neighbour = this_chunk.getNeighbourChunk(0, offset_y);
            }
            if(tmp_x < 0) tmp_x = 15;
            if(tmp_x > 15) tmp_x = 0;
            if(tmp_y < 0) tmp_y = 15;
            if(tmp_y > 15) tmp_y = 0;
            if(neighbour != null) {
                // Checks neighbour chunk tiles to same tileclass
                if(neighbour.getTileMap(tilemap_index).containsKey(new Point(tmp_x, tmp_y))) {
                    Tile temp_tile = (Tile) neighbour.getTileMap(tilemap_index).get(new Point(tmp_x, tmp_y));
                    if(tile.getClass() == temp_tile.getClass()) return true;
                }
            }
        }
        return false;
    }

    public static TILE_TYPE getTileType4DirTile(Tile tile, Chunk this_chunk, int tilemap_index) {
        boolean top, right, bottom, left;
        top = checkSameNeighbourTile(tile, this_chunk, 0, -1, tilemap_index);
        right = checkSameNeighbourTile(tile, this_chunk, 1, 0, tilemap_index);
        bottom = checkSameNeighbourTile(tile, this_chunk, 0, 1, tilemap_index);
        left = checkSameNeighbourTile(tile, this_chunk, -1, 0, tilemap_index);
        return getTypeFromBooleans4(top, right, bottom, left);
    }

    public static TILE_TYPE getTileType4DirTileOrBiome(Tile tile, Chunk this_chunk, int tilemap_index) {
        boolean top, right, bottom, left;
        top = checkSameNeighbourTileOrBiome(tile, this_chunk, 0, -1, tilemap_index);
        right = checkSameNeighbourTileOrBiome(tile, this_chunk, 1, 0, tilemap_index);
        bottom = checkSameNeighbourTileOrBiome(tile, this_chunk, 0, 1, tilemap_index);
        left = checkSameNeighbourTileOrBiome(tile, this_chunk, -1, 0, tilemap_index);
        return getTypeFromBooleans4(top, right, bottom, left);
    }

    public static TILE_TYPE getTileType8DirTile(Tile tile, Chunk this_chunk, int tilemap_index) {
        boolean top, right, bottom, left;
        boolean top_left, top_right, bottom_left, bottom_right;
        top = checkSameNeighbourTile(tile, this_chunk, 0, -1, tilemap_index);
        right = checkSameNeighbourTile(tile, this_chunk, 1, 0, tilemap_index);
        bottom = checkSameNeighbourTile(tile, this_chunk, 0, 1, tilemap_index);
        left = checkSameNeighbourTile(tile, this_chunk, -1, 0, tilemap_index);

        top_left = checkSameNeighbourTile(tile, this_chunk, -1, -1, tilemap_index);
        top_right = checkSameNeighbourTile(tile, this_chunk, 1, -1, tilemap_index);
        bottom_left = checkSameNeighbourTile(tile, this_chunk, -1, 1, tilemap_index);
        bottom_right = checkSameNeighbourTile(tile, this_chunk, 1, 1, tilemap_index);
        return getTypeFromBooleans8(top, right, bottom, left, top_left, top_right, bottom_left, bottom_right);
    }

    public static TILE_TYPE getTileType8DirTileOrBiome(Tile tile, Chunk this_chunk, int tilemap_index) {
        boolean top, right, bottom, left;
        boolean top_left, top_right, bottom_left, bottom_right;
        top = checkSameNeighbourTileOrBiome(tile, this_chunk, 0, -1, tilemap_index);
        right = checkSameNeighbourTileOrBiome(tile, this_chunk, 1, 0, tilemap_index);
        bottom = checkSameNeighbourTileOrBiome(tile, this_chunk, 0, 1, tilemap_index);
        left = checkSameNeighbourTileOrBiome(tile, this_chunk, -1, 0, tilemap_index);

        top_left = checkSameNeighbourTileOrBiome(tile, this_chunk, -1, -1, tilemap_index);
        top_right = checkSameNeighbourTileOrBiome(tile, this_chunk, 1, -1, tilemap_index);
        bottom_left = checkSameNeighbourTileOrBiome(tile, this_chunk, -1, 1, tilemap_index);
        bottom_right = checkSameNeighbourTileOrBiome(tile, this_chunk, 1, 1, tilemap_index);
        return getTypeFromBooleans8(top, right, bottom, left, top_left, top_right, bottom_left, bottom_right);
    }

    private static TILE_TYPE getTypeFromBooleans8(
            boolean top, boolean right, boolean bottom, boolean left,
            boolean top_left, boolean top_right, boolean bottom_left, boolean bottom_right) {
        if (top && right && bottom && left) {
            if (!top_left) {
                return TILE_TYPE.top_left_inverse;
            } else if (!top_right) {
                return TILE_TYPE.top_right_inverse;
            } else if (!bottom_left) {
                return TILE_TYPE.bottom_left_inverse;
            } else if (!bottom_right) {
                return TILE_TYPE.bottom_right_inverse;
            }
        }
        return getTypeFromBooleans4(top, right, bottom, left);
    }

    private static TILE_TYPE getTypeFromBooleans4(
            boolean top, boolean right, boolean bottom, boolean left) {
        if (top && right && bottom && left) {
            return TILE_TYPE.center;
        } else if (left && right) {
            if(top) {
                return TILE_TYPE.bottom;
            } else if(bottom) {
                return TILE_TYPE.top;
            }
            return TILE_TYPE.top;
        } else if (top && bottom) {
            if(left) {
                return TILE_TYPE.right;
            } else if(right) {
                return TILE_TYPE.left;
            }
            return TILE_TYPE.left;
        } else if (bottom && left) {
            return TILE_TYPE.top_right;
        } else if (top && left) {
            return TILE_TYPE.bottom_right;
        } else if (right && top) {
            return TILE_TYPE.bottom_left;
        } else if (right && bottom) {
            return TILE_TYPE.top_left;
        } else if(top) {
            return TILE_TYPE.bottom;
        } else if(bottom) {
            return TILE_TYPE.top;
        } else if(left) {
            return TILE_TYPE.right;
        } else if(right) {
            return TILE_TYPE.left;
        }
        // TODO no difference between left and right side

        return TILE_TYPE.center;
    }
}
