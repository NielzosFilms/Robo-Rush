package game.assets.structures.waterfall;

import game.assets.structures.Structure;
import game.assets.tiles.Tile;
import game.assets.tiles.Tile_CaveWall;
import game.assets.tiles.Tile_Wall;
import game.assets.tiles.floor.wood.Tile_FloorWood;
import game.enums.BIOME;
import game.system.systems.GameObject;
import game.system.world.Chunk;
import game.system.world.World;
import game.system.world.biome_groups.BiomeGroup;

import java.awt.*;
import java.util.HashMap;

public class Structure_Waterfall extends Structure {
    public Structure_Waterfall(Long seed, GameObject world_object, BiomeGroup biomeGroup) {
        super(seed, world_object, biomeGroup);
        generation.setHeight_scale(0.1f);
    }

    public Tile getGeneratedTile(int x, int y, float height, float temp, float moist, Chunk chunk, int world_x, int world_y) {
        if(generation.getBiomeWithCoords(world_x, world_y) == BIOME.Cave_wall) {
            return new Tile_CaveWall(world_x, world_y, x, y, 2, chunk);
        } else {
            return new Tile_FloorWood(world_x, world_y, x, y, 1, chunk);
        }
    }

    public void generateNewChunksOffScreen(int camX, int camY, int camW, int camH, World world) {
        for (int y = camY - 32; y < camY + camH + 16; y++) {
            for (int x = camX - 32; x < camX + camW + 16; x++) {
                if (world.getActiveChunks().containsKey(new Point(x, y))) {
                    Chunk chunk = world.getActiveChunks().get(new Point(x, y));
                    chunk.tick();
                    int world_x = x * 16;
                    int world_y = y * 16;
                    for(int xx=0; xx<16; xx++) {
                        if(generation.getBiomeWithCoords(world_x + xx * 16, world_y) == BIOME.Cave_floor) {
                            if (!world.getActiveChunks().containsKey(new Point(x, y-16))) {
                                world.getActiveChunks().put(
                                        new Point(x, y-16),
                                        new Chunk(x, y-16, world));
                            }
                            break;
                        }
                    }
                    for(int xx=0; xx<16; xx++) {
                        if(generation.getBiomeWithCoords(world_x + xx * 16, world_y + 15 * 16) == BIOME.Cave_floor) {
                            if (!world.getActiveChunks().containsKey(new Point(x, y+16))) {
                                world.getActiveChunks().put(
                                        new Point(x, y+16),
                                        new Chunk(x, y+16, world));
                            }
                            break;
                        }
                    }

                    for(int yy=0; yy<16; yy++) {
                        if(generation.getBiomeWithCoords(world_x, world_y + yy * 16) == BIOME.Cave_floor) {
                            if (!world.getActiveChunks().containsKey(new Point(x-16, y))) {
                                world.getActiveChunks().put(
                                        new Point(x-16, y),
                                        new Chunk(x-16, y, world));
                            }
                            break;
                        }
                    }
                    for(int yy=0; yy<16; yy++) {
                        if(generation.getBiomeWithCoords(world_x + 15 * 16, world_y + yy * 16) == BIOME.Cave_floor) {
                            if (!world.getActiveChunks().containsKey(new Point(x+16, y))) {
                                world.getActiveChunks().put(
                                        new Point(x+16, y),
                                        new Chunk(x+16, y, world));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
