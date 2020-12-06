package game.assets.tiles;

import game.assets.items.Item;
import game.enums.TILE_TYPE;
import game.system.helpers.TileHelperFunctions;
import game.system.systems.menu.Text;
import game.textures.TEXTURE_LIST;
import game.system.main.Game;
import game.system.world.Chunk;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;

public class Tile_Wall extends Tile {
    private int original_z;
    private Texture texture_bot;

    public Tile_Wall(int x, int y, int chunk_x, int chunk_y, int z_index, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, null, chunk);
        this.original_z = z_index;

        texture = new Texture(TEXTURE_LIST.walls_list, 0, 5);
    }

    public void tick() {
        /*int player_cenY = (int) Game.world.getPlayer().getBounds().getCenterY();
        int cenY = (int) getBounds().getCenterY();

        if (player_cenY > cenY) {
            this.setZIndex(original_z);
        } else {
            this.setZIndex(Game.world.getPlayer().getZIndex() + 1);
        }*/
    }

    public void render(Graphics g) {
        g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
        if(texture_bot != null) {
            g.drawImage(texture_bot.getTexure(), x, y + tileSize, tileSize, tileSize, null);
        }
    }

    public Rectangle getBounds() {
        if(texture_bot != null) return new Rectangle(x, y, tileSize, tileSize*2);
        return new Rectangle(x, y, tileSize, tileSize);
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public void findAndSetEdgeTexture(int tilemap_index) {
        boolean top = TileHelperFunctions.checkSameNeighbourTile(this, chunk, 0, -1, z_index);
        boolean right = TileHelperFunctions.checkSameNeighbourTile(this, chunk, 1, 0, z_index);
        boolean left = TileHelperFunctions.checkSameNeighbourTile(this, chunk, -1, 0, z_index);
        boolean bot = TileHelperFunctions.checkSameNeighbourTile(this, chunk, 0, 1, z_index);
        if(top && left && !right && !bot) {
            texture = new Texture(TEXTURE_LIST.walls_list, 2, 3);
            texture_bot = new Texture(TEXTURE_LIST.walls_list, 2, 4);
        } else if(top && right && !left && !bot){
            texture = new Texture(TEXTURE_LIST.walls_list, 0, 3);
            texture_bot = new Texture(TEXTURE_LIST.walls_list, 0, 4);
        } else if(bot && left && !right && ! top) {
            texture = new Texture(TEXTURE_LIST.walls_list, 2, 0);
        } else if(bot && right && !left && ! top) {
            texture = new Texture(TEXTURE_LIST.walls_list, 0, 0);
        } else if(left && right) {
            texture = new Texture(TEXTURE_LIST.walls_list, 1, 3);
            texture_bot = new Texture(TEXTURE_LIST.walls_list, 1, 4);
        } else if(top && bot) {
            texture = new Texture(TEXTURE_LIST.walls_list, 0, 1);
        }
    }

    public void update(int tilemap_index) {
        findAndSetEdgeTexture(tilemap_index);
    }

    public Item getItem() {
        return null;
    }
}
