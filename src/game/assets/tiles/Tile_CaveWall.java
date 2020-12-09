package game.assets.tiles;

import game.assets.items.Item;
import game.assets.tiles.floor.cave.Tile_Floor_Cave;
import game.enums.BIOME;
import game.enums.TILE_TYPE;
import game.system.helpers.TileHelperFunctions;
import game.system.main.Game;
import game.system.world.Chunk;
import game.textures.Fonts;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Tile_CaveWall extends Tile {
    private HashMap<TILE_TYPE, Texture> textures = new HashMap<>();
    private LinkedList<TILE_TYPE> types_to_add_bg = new LinkedList<>();

    public Tile_CaveWall(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk);

        texture = new Texture(TEXTURE_LIST.cave_list, 9, 3);

        textures.put(TILE_TYPE.bottom, new Texture(TEXTURE_LIST.cave_list, 9, 3));
        textures.put(TILE_TYPE.top, new Texture(TEXTURE_LIST.cave_list, 9, 0));
        textures.put(TILE_TYPE.right, new Texture(TEXTURE_LIST.cave_list, 11, 1));
        textures.put(TILE_TYPE.left, new Texture(TEXTURE_LIST.cave_list, 7, 1));

        textures.put(TILE_TYPE.bottom_right, new Texture(TEXTURE_LIST.cave_list, 13, 1));
        textures.put(TILE_TYPE.bottom_left, new Texture(TEXTURE_LIST.cave_list, 12, 1));
        textures.put(TILE_TYPE.top_right, new Texture(TEXTURE_LIST.cave_list, 12, 2));
        textures.put(TILE_TYPE.top_left, new Texture(TEXTURE_LIST.cave_list, 13, 2));

        textures.put(TILE_TYPE.top_right_inverse, new Texture(TEXTURE_LIST.cave_list, 7, 3));
        textures.put(TILE_TYPE.top_left_inverse, new Texture(TEXTURE_LIST.cave_list, 11, 3));
        textures.put(TILE_TYPE.bottom_right_inverse, new Texture(TEXTURE_LIST.cave_list, 7, 0));
        textures.put(TILE_TYPE.bottom_left_inverse, new Texture(TEXTURE_LIST.cave_list, 11, 0));

        types_to_add_bg.add(TILE_TYPE.bottom_left);
        types_to_add_bg.add(TILE_TYPE.bottom_right);
        types_to_add_bg.add(TILE_TYPE.top_left);
        types_to_add_bg.add(TILE_TYPE.top_right);
        types_to_add_bg.add(TILE_TYPE.top);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
        if(tile_type != null && Game.DEBUG_MODE) {
            g.setColor(Color.WHITE);
            g.setFont(Fonts.default_fonts.get(2));
            g.drawString(tile_type.name(), x, y+8);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, tileSize, tileSize);
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public void findAndSetEdgeTexture() {
        boolean top = TileHelperFunctions.checkSameNeighbourBiome(this, BIOME.Cave_floor, 0, -1);
        boolean right = TileHelperFunctions.checkSameNeighbourBiome(this, BIOME.Cave_floor, 1, 0);
        boolean bot = TileHelperFunctions.checkSameNeighbourBiome(this, BIOME.Cave_floor, 0, 1);
        boolean left = TileHelperFunctions.checkSameNeighbourBiome(this, BIOME.Cave_floor, -1, 0);

        boolean top_right = TileHelperFunctions.checkSameNeighbourBiome(this, BIOME.Cave_floor, 1, -1);
        boolean bot_right = TileHelperFunctions.checkSameNeighbourBiome(this, BIOME.Cave_floor, 1, 1);
        boolean bot_left = TileHelperFunctions.checkSameNeighbourBiome(this, BIOME.Cave_floor, -1, 1);
        boolean top_left = TileHelperFunctions.checkSameNeighbourBiome(this, BIOME.Cave_floor, -1, -1);
        tile_type =  TileHelperFunctions.getTypeFromBooleans8(top, right, bot, left, top_left, top_right, bot_left, bot_right);
        if(textures.containsKey(tile_type)) {
            texture = textures.get(tile_type);
            if(tile_type == TILE_TYPE.top) {
                Tile tile_right = TileHelperFunctions.getNeighbourTile(this, chunk, 1, 0, z_index);
                Tile tile_left = TileHelperFunctions.getNeighbourTile(this, chunk, -1, 0, z_index);
                if(tile_left != null && tile_right != null) {
                    if(tile_left.getTile_type() != TILE_TYPE.top && tile_right.getTile_type() == TILE_TYPE.top) {
                        texture = new Texture(TEXTURE_LIST.cave_list, 8, 0);
                    } else if(tile_left.getTile_type() == TILE_TYPE.top && tile_right.getTile_type() != TILE_TYPE.top) {
                        texture = new Texture(TEXTURE_LIST.cave_list, 10, 0);
                    } else if(tile_left.getTile_type() != TILE_TYPE.top && tile_right.getTile_type() != TILE_TYPE.top) {
                        texture = new Texture(TEXTURE_LIST.cave_list, 12, 3);
                    }
                }
            }
            if(types_to_add_bg.contains(tile_type)) {
                chunk.addTile(new Tile_Floor_Cave(x, y, chunk_x, chunk_y, 1, BIOME.Cave_wall, chunk));
            }
        }
    }

    public void update() {

    }

    public Item getItem() {
        return null;
    }
}
