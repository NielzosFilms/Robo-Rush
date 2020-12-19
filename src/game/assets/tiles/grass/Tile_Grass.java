package game.assets.tiles.grass;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

import game.assets.items.item.Placeable;
import game.assets.objects.rock.Pebble;
import game.assets.objects.tree.Tree;
import game.assets.tiles.tile.Tile;
import game.assets.tiles.tile.Transition;
import game.assets.tiles.tile.UpdateAble;
import game.enums.ID;
import game.system.helpers.Logger;
import game.system.helpers.Offsets;
import game.system.helpers.TileHelperFunctions;
import game.system.helpers.TransitionHelpers;
import game.system.main.Game;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.enums.BIOME;
import game.system.world.Chunk;

public class Tile_Grass extends Tile implements UpdateAble, Transition {

    public Tile_Grass(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk);

        texture = new Texture(TEXTURE_LIST.forest_list, 1, 1);
        placeEntityOnTile();
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(this.texture.getTexure(), x, y, this.tileSize, this.tileSize, null);
    }

    private void placeEntityOnTile() {
        Random r = new Random();
        if(r.nextInt(101) == 0) {
            if(r.nextInt(2) == 0) {
                chunk.addEntity(new Tree(x, y, 10, ID.Tree, biome));
            } else {
                chunk.addEntity(new Pebble(x, y, 10, ID.Pebble));
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void createTransitionTiles() {
        for(Point offset : Offsets.all_offsets) {
            Tile offset_tile = TileHelperFunctions.getNeighbourTile(this, chunk, offset.x, offset.y, z_index);
            if(offset_tile == null || offset_tile.getClass() != this.getClass()) {
                Point offset_world_coords = new Point(x + offset.x * 16, y + offset.y * 16);
                Chunk offset_chunk = Game.world.getChunkWithCoordsPoint(Game.world.getChunkPointWithCoords(offset_world_coords.x, offset_world_coords.y));
                if(offset_chunk != null) {
                    Point chunk_coords = TileHelperFunctions.getTileChunkCoords(chunk_x + offset.x, chunk_y + offset.y);
                    offset_chunk.addTile(new Tile_Grass_Transition(offset_world_coords.x, offset_world_coords.y, chunk_coords.x, chunk_coords.y, z_index, BIOME.NULL, offset_chunk));
                }
            }
        }
    }
}
