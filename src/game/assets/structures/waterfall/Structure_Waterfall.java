package game.assets.structures.waterfall;

import game.assets.structures.Structure;
import game.assets.tiles.Tile;
import game.assets.tiles.floor.wood.Tile_FloorWood;
import game.system.systems.GameObject;
import game.system.world.Chunk;

public class Structure_Waterfall extends Structure {
    public Structure_Waterfall(Long seed, GameObject world_object) {
        super(seed, world_object);
    }

    public Tile getGeneratedTile(int x, int y, float height, float temp, float moist, Chunk chunk, int world_x, int world_y) {
        return new Tile_FloorWood(world_x, world_y, x, y, 1, chunk);
    }
}
