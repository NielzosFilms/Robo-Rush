package game.assets.tiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.awt.Point;

import game.enums.BIOME;
import game.system.main.Game;
import game.system.world.Chunk;
import game.system.world.World;

public class TileHelperFunctions {
    private static Random r = new Random();

    public static boolean checkSameNeighbourTile(Tile tile, Chunk this_chunk, int offset_x, int offset_y) {
        int x = tile.getChunkX(), y = tile.getChunkY();
        HashMap<Point, Tile> chunk_tiles = this_chunk.getTileMap();
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
                if(neighbour.getTileMap().containsKey(new Point(tmp_x, tmp_y))) {
                    Tile temp_tile = (Tile) neighbour.getTileMap().get(new Point(tmp_x, tmp_y));
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

    public static BIOME getBestNeighbourBiome(BIOME biome, HashMap<Point, Tile> chunk_tiles, Chunk this_chunk, int x,
            int y, int offset_x, int offset_y) {

        HashMap<BIOME, Integer> biomes = new HashMap<BIOME, Integer>();

        int offset_x_16 = offset_x * 16;
        int offset_y_16 = offset_y * 16;
        if (chunk_tiles.containsKey(new Point(x + offset_x_16, y + offset_y_16))) {
            Tile temp_tile = (Tile) chunk_tiles.get(new Point(x + offset_x_16, y + offset_y_16));
            if (biome != temp_tile.biome) {
                if (biomes.containsKey(temp_tile.biome)) {
                    biomes.put(temp_tile.biome, biomes.get(temp_tile.biome) + 1);
                } else {
                    biomes.put(temp_tile.biome, 0);
                }
            }
        } else {
            float[] arr = Game.world.getHeightMapValuePoint(this_chunk.x + ((x - this_chunk.x) / 16) + offset_x,
                    this_chunk.y + ((y - this_chunk.y) / 16) + offset_y);
            if (biome != getBiomeFromHeightMap(arr)) {
                if (biomes.containsKey(getBiomeFromHeightMap(arr))) {
                    biomes.put(getBiomeFromHeightMap(arr), biomes.get(getBiomeFromHeightMap(arr)) + 1);
                } else {
                    biomes.put(getBiomeFromHeightMap(arr), 0);
                }
            }
        }

        int max = Collections.max(biomes.values());

        ArrayList<BIOME> keys = new ArrayList<>();
        for (HashMap.Entry<BIOME, Integer> entry : biomes.entrySet()) {
            if (entry.getValue() == max) {
                keys.add(entry.getKey());
            }
        }

        if (keys.size() == 1) {
            return keys.get(0);
        } else {
            return keys.get(r.nextInt(keys.size() - 1));
        }
    }
}
