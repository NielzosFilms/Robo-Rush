package game.system.helpers;

import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.system.main.Game;
import game.system.world.Chunk;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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

//    public static int getTransition(Tile tile, Chunk this_chunk, int tilemap_index) {
//        int retByte = getByte(tile, this_chunk, tilemap_index, edge_offsets, 0);
//        if(retByte == 0b00000000) retByte = getByte(tile, this_chunk, tilemap_index, corner_offset, 4);
//        return retByte;
//    }

//    private static int getByte(Tile tile, Chunk this_chunk, int tilemap_index, Point[] offsets, int byte_offset) {
//        int retByte = 0b00000000;
//        for(int i=0; i<offsets.length; i++) {
//            Tile offset_tile = TileHelperFunctions.getNeighbourTile(tile, this_chunk, offsets[i].x, offsets[i].y, tilemap_index);
//            if(offset_tile == null) {
//                retByte = unsetBit(retByte, i + byte_offset);
//            } else if(offset_tile.getClass() != tile.getClass()) {
//                retByte = setBit(retByte, i + byte_offset);
//            }
//        }
//        return retByte;
//    }

    private static int setBit(int ret, int pos) {
        return ret | (1 << pos);
    }

    private static int unsetBit(int ret, int pos) {
        return ret & ~(1 << pos);
    }

    public static void createTransitionTiles(Tile tile, Chunk chunk, Class tile_transition_class) {
//        int z_index = tile.getZIndex();
//        int tile_chunk_x = tile.getChunkX();
//        int tile_chunk_y = tile.getChunkY();
//        try {
//            Constructor<?> ctor = tile_transition_class.getConstructor(int.class, int.class, int.class, int.class, int.class, BIOME.class, Chunk.class);
//            for (Point offset : Offsets.all_offsets) {
//                Tile offset_tile = TileHelperFunctions.getNeighbourTile(tile, chunk, offset.x, offset.y, z_index);
//
//                if (offset_tile == null || (offset_tile.getClass() != tile.getClass() && offset_tile.getClass() != tile_transition_class)) {
//                    Point offset_world_coords = new Point(tile.getX() + offset.x * 16, tile.getY() + offset.y * 16);
//
//                    Point chunk_coords = Game.gameController.getChunkPointWithCoords(offset_world_coords.x, offset_world_coords.y);
//                    Point tile_chunk_coords = TileHelperFunctions.getTileChunkCoords(tile_chunk_x + offset.x, tile_chunk_y + offset.y);
//
//                    Chunk offset_chunk = Game.gameController.getChunkWithCoordsPoint(chunk_coords);
//                    if (offset_chunk != null) {
//                        Tile tile_to_add = (Tile) ctor.newInstance(offset_world_coords.x, offset_world_coords.y, tile_chunk_coords.x, tile_chunk_coords.y, z_index, BIOME.NULL, offset_chunk);
//                        offset_chunk.addTile(tile_to_add);
//                    } else {
//                        Tile tile_to_add = (Tile) ctor.newInstance(offset_world_coords.x, offset_world_coords.y, tile_chunk_coords.x, tile_chunk_coords.y, z_index, BIOME.NULL, null);
//                        Game.gameController.getHandler().addTile(tile_to_add);
//                    }
//                }
//            }
//        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }
}
