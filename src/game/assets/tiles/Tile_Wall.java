package game.assets.tiles;

import game.assets.items.Item;
import game.enums.BIOME;
import game.enums.TEXTURE_LIST;
import game.system.main.Game;
import game.system.world.Chunk;
import game.textures.Texture;

import java.awt.*;

public class Tile_Wall extends Tile {
    private int original_z;
    private Texture top_texture = new Texture(TEXTURE_LIST.house_list, 0, 3);
    private Texture bot_texture = new Texture(TEXTURE_LIST.house_list, 0, 4);
    public Tile_Wall(int x, int y, int chunk_x, int chunk_y, int z_index, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, null, chunk);
        this.original_z = z_index;
    }

    public void tick() {
        int player_cenY = (int) Game.world.getPlayer().getBounds().getCenterY();
        int cenY = (int) getBounds().getCenterY();

        if (player_cenY > cenY) {
            this.setZIndex(original_z);
        } else {
            this.setZIndex(Game.world.getPlayer().getZIndex() + 1);
        }
    }

    public void render(Graphics g) {
        g.drawImage(top_texture.getTexure(), x, y, tileSize, tileSize, null);
        g.drawImage(bot_texture.getTexure(), x, y+16, tileSize, tileSize, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y + 16, 16, 16);
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public void findAndSetEdgeTexture(int tilemap_index) {

    }

    public void update(int tilemap_index) {

    }

    public Item getItem() {
        return null;
    }
}
