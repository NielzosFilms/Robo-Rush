package game.assets.tiles.floor.wood;

import game.assets.items.Item;
import game.assets.items.Item_Ground;
import game.assets.tiles.Tile;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.enums.TEXTURE_LIST;
import game.system.helpers.Helpers;
import game.system.main.Game;
import game.system.systems.inventory.InventorySystem;
import game.system.world.Chunk;
import game.textures.Texture;

import java.awt.*;

public class Item_FloorWood extends Item {
    public Item_FloorWood(int amount) {
        super(amount, ITEM_ID.Tile_FloorWood);
        this.placeable = true;
        this.tex = new Texture(TEXTURE_LIST.floorTiles_list, 1, 1);
        this.itemGround = new Item_Ground(0, 0, 1, ID.Item, this);
    }

    public boolean place(int x, int y) {
        Chunk chunk = Game.world.getChunkWithCoordsPoint(Game.world.getChunkPointWithCoords(x, y));
        if(chunk != null) {
            Point tile_coords = Helpers.getTileCoords(new Point(x, y), InventorySystem.item_w, InventorySystem.item_h);
            int tile_x = tile_coords.x / 16 - chunk.x;
            int tile_y = tile_coords.y / 16 - chunk.y;
            Tile tile = new Tile_FloorWood(tile_coords.x, tile_coords.y, tile_x, tile_y, 3, chunk);
            if(!chunk.tileExists(tile)) {
                chunk.addTile(tile);
                chunk.updateSameTiles(tile);
                return true;
            }
        }
        return false;
    }
}
