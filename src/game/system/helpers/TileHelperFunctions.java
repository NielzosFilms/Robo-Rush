package game.system.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.awt.Point;

import game.assets.tiles.Tile;
import game.enums.BIOME;
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
            return tile.getClass() == temp_tile.getClass();
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
                    return tile.getClass() == temp_tile.getClass();
                }
            } else {
                // Checks neighbour chunk coords with generated biome value to same tile biome
                float[] arr = Game.world.getHeightMapValuePoint(this_chunk.x + x + offset_x, this_chunk.y + y + offset_y);
                return tile.getBiome() == getBiomeFromHeightMap(arr);
            }
        }
        return false;
    }

    public static BIOME getBiomeFromHeightMap(float[] point) {
        float osn = point[0];
        float temp_osn = point[1];
        float moist_osn = point[2];

        return World.getBiome(osn, temp_osn, moist_osn);
    }

    public static boolean checkSameNeighbourTile(Tile tile, Chunk this_chunk, int offset_x, int offset_y, int tilemap_index) {
        int x = tile.getChunkX(), y = tile.getChunkY();
        HashMap<Point, Tile> chunk_tiles = this_chunk.getTileMap(tilemap_index);
        if (chunk_tiles.containsKey(new Point(x + offset_x, y + offset_y))) {
            // Checks same chunk tiles to same tileclass
            Tile temp_tile = (Tile) chunk_tiles.get(new Point(x + offset_x, y + offset_y));
            return tile.getClass() == temp_tile.getClass();
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
                    return tile.getClass() == temp_tile.getClass();
                }
            }
        }
        return false;
    }
}
