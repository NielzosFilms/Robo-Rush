package game.system.helpers;

import java.util.HashMap;
import java.util.Random;
import java.awt.Point;

import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.enums.TILE_TYPE;
import game.system.main.Game;
import game.system.world.Chunk;

public class TileHelperFunctions {
    public static Tile getNeighbourTile(Tile tile, Chunk this_chunk, int offset_x, int offset_y, int tilemap_index) {
        int x = tile.getChunkX(), y = tile.getChunkY();
        HashMap<Point, Tile> chunk_tiles = this_chunk.getTileMap(tilemap_index);
        if (chunk_tiles.containsKey(new Point(x + offset_x, y + offset_y))) {
            return (Tile) chunk_tiles.get(new Point(x + offset_x, y + offset_y));
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
                if(neighbour.getTileMap(tilemap_index).containsKey(new Point(tmp_x, tmp_y))) {
                    return (Tile) neighbour.getTileMap(tilemap_index).get(new Point(tmp_x, tmp_y));
                }
            }
        }
        return null;
    }
}
