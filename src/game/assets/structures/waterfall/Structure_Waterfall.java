package game.assets.structures.waterfall;

import game.assets.structures.Structure;
import game.assets.tiles.Tile;
import game.assets.tiles.Tile_CaveWall;
import game.assets.tiles.Tile_Wall;
import game.assets.tiles.floor.cave.Tile_Floor_Cave;
import game.assets.tiles.floor.wood.Tile_FloorWood;
import game.enums.BIOME;
import game.system.audioEngine.AudioClip;
import game.system.audioEngine.AudioPlayer;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.systems.GameObject;
import game.system.world.Chunk;
import game.system.world.JsonStructureLoader;
import game.system.world.World;
import game.system.world.biome_groups.BiomeGroup;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Structure_Waterfall extends Structure {
    public Structure_Waterfall(Long seed, GameObject world_object, BiomeGroup biomeGroup) {
        super(seed, world_object, biomeGroup);
        /*generation.setHeight_scale(0.05f);
        generation.setTemp_scale(0.05f);
        boolean good_seed = false;


        while(!good_seed) {
            float[][] height = generation.getHeightOsn(0, 0, 16, 16);
            float[][] temp = generation.getTemperatureOsn(0, 0, 16, 16);
            float[][] moist = generation.getTemperatureOsn(0, 0, 16, 16);
            for(int y=0; y<16; y++) {
                for(int x=0; x<16; x++) {
                    if(generation.getBiome(height[x][y], temp[x][y], moist[x][y]) == BIOME.Cave_floor) {
                        good_seed = true;
                    }
                }
            }
            generation.setNewSeed(Game.world.getNextSeed());
        }*/
    }

    public void generate(World world) {
        generated = false;
        JsonStructureLoader loader = new JsonStructureLoader("assets/structures/puzzle_1.json");
        //chunks = loader.getChunks(world);
        loader.addAllToChunk(chunks, world);
        player_spawn = loader.getPlayerSpawn();
        generated = true;
    }

    public void entered(World world) {
    }

    public LinkedList<Tile> getGeneratedTile(int x, int y, float height, float temp, float moist, Chunk chunk, int world_x, int world_y) {
            //return new Tile_CaveWall(world_x, world_y, x, y, 2, chunk);
        LinkedList<Tile> ret = new LinkedList<>();
        /*if(generation.getBiomeWithCoords(world_x, world_y) == BIOME.Cave_floor) {
            ret.add(new Tile_Floor_Cave(world_x, world_y, x, y, 1, BIOME.Cave_floor, chunk));
            if(generation.getBiomeWithCoords(world_x, world_y-16) != BIOME.Cave_floor)
                ret.add(new Tile_CaveWall(world_x, world_y-16, x, y-1, 2, BIOME.Cave_wall, chunk));
            if(generation.getBiomeWithCoords(world_x+16, world_y) != BIOME.Cave_floor)
                ret.add(new Tile_CaveWall(world_x+16, world_y, x+1, y, 2, BIOME.Cave_wall, chunk));
            if(generation.getBiomeWithCoords(world_x, world_y+16) != BIOME.Cave_floor)
                ret.add(new Tile_CaveWall(world_x, world_y+16, x, y+1, 2, BIOME.Cave_wall, chunk));
            if(generation.getBiomeWithCoords(world_x-16, world_y) != BIOME.Cave_floor)
                ret.add(new Tile_CaveWall(world_x-16, world_y, x-1, y, 2, BIOME.Cave_wall, chunk));

            if(generation.getBiomeWithCoords(world_x+16, world_y-16) != BIOME.Cave_floor)
                ret.add(new Tile_CaveWall(world_x+16, world_y-16, x+1, y-1, 2, BIOME.Cave_wall, chunk));
            if(generation.getBiomeWithCoords(world_x+16, world_y+16) != BIOME.Cave_floor)
                ret.add(new Tile_CaveWall(world_x+16, world_y+16, x+1, y+1, 2, BIOME.Cave_wall, chunk));
            if(generation.getBiomeWithCoords(world_x-16, world_y+16) != BIOME.Cave_floor)
                ret.add(new Tile_CaveWall(world_x-16, world_y+16, x-1, y+1, 2, BIOME.Cave_wall, chunk));
            if(generation.getBiomeWithCoords(world_x-16, world_y-16) != BIOME.Cave_floor)
                ret.add(new Tile_CaveWall(world_x-16, world_y-16, x-1, y-1, 2, BIOME.Cave_wall, chunk));
        }*/
        return ret;
    }

    public void generateNewChunksOffScreen(int camX, int camY, int camW, int camH, World world) {
        /*for (int y = camY - 32; y < camY + camH + 16; y++) {
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
                                world.getActiveChunks().get(new Point(x, y-16)).updateTiles();
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
                                world.getActiveChunks().get(new Point(x, y+16)).updateTiles();
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
                                world.getActiveChunks().get(new Point(x-16, y)).updateTiles();
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
                                world.getActiveChunks().get(new Point(x+16, y)).updateTiles();
                            }
                            break;
                        }
                    }
                }
            }
        }*/
    }
}
