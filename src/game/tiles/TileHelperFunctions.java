package game.tiles;

import java.util.HashMap;
import java.awt.Point;

import game.world.BIOME;
import game.world.Chunk;
import game.world.World;

public class TileHelperFunctions {
    public static Boolean checkSameNeighbourBiome(BIOME biome, HashMap<Point, Tile> chunk_tiles, Chunk this_chunk,
            int x, int y, int offset_x, int offset_y) {
        int offset_x_16 = offset_x * 16;
        int offset_y_16 = offset_y * 16;
        if (chunk_tiles.containsKey(new Point(x + offset_x_16, y + offset_y_16))) {
            Tile temp_tile = (Tile) chunk_tiles.get(new Point(x + offset_x_16, y + offset_y_16));
            if (biome != temp_tile.biome) {
                // this.biome_bg = temp_tile.biome;
                return false;
            }
        } else {
            float[] arr = World.getHeightMapValuePoint(this_chunk.x + ((x - this_chunk.x) / 16) + offset_x,
                    this_chunk.y + ((y - this_chunk.y) / 16) + offset_y);
            if (biome != getBiomeFromHeightMap(arr)) {
                // this.biome_bg = getBiomeFromHeightMap(arr);
                return false;
            }
        }
        return true;
    }

    public static BIOME getBiomeFromHeightMap(float[] point) {
        float osn = point[0];
        float temp_osn = point[1];
        float moist_osn = point[2];

        return World.getBiome(osn, temp_osn, moist_osn);
    }
}
