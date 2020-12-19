package game.system.helpers;

import game.assets.tiles.grass.Tile_Grass;
import game.assets.tiles.tile.Tile;
import game.system.world.Chunk;

import java.awt.*;

public class TransitionHelpers {
    public static Point[] edge_offsets = new Point[]{
            new Point(0, -1),
            new Point(1, 0),
            new Point(0, 1),
            new Point(-1, 0),
    };

    public static Point[] corner_offset = new Point[]{
            new Point(1, -1),
            new Point(1, 1),
            new Point(-1, 1),
            new Point(-1, -1),
    };

    public static int getTransition(Tile tile, Chunk this_chunk, int tilemap_index) {
        int retByte = getByte(tile, this_chunk, tilemap_index, edge_offsets, 0);
        if(retByte == 0b00000000) retByte = getByte(tile, this_chunk, tilemap_index, corner_offset, 4);
        return retByte;
    }

    private static int getByte(Tile tile, Chunk this_chunk, int tilemap_index, Point[] offsets, int byte_offset) {
        int retByte = 0b00000000;
        for(int i=0; i<offsets.length; i++) {
            Tile offset_tile = TileHelperFunctions.getNeighbourTile(tile, this_chunk, offsets[i].x, offsets[i].y, tilemap_index);
            if(offset_tile == null) {
                retByte = unsetBit(retByte, i + byte_offset);
            } else if(offset_tile.getClass() == Tile_Grass.class) {
                //Logger.print("classes matched");
                retByte = setBit(retByte, i + byte_offset);
            }
        }
        return retByte;
    }

    private static int setBit(int ret, int pos) {
        return ret | (1 << pos);
    }

    private static int unsetBit(int ret, int pos) {
        return ret & ~(1 << pos);
    }
}
