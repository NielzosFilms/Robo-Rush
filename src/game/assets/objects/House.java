package game.assets.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import game.assets.items.Item;
import game.enums.GAMESTATES;
import game.system.main.Game;
import game.system.main.GameObject;
import game.enums.ID;
import game.textures.Textures;

public class House extends GameObject {

    private ArrayList<ArrayList<BufferedImage>> tex_rows = new ArrayList<ArrayList<BufferedImage>>();

    public House(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        tex_rows.add(new ArrayList<BufferedImage>());
        tex_rows.add(new ArrayList<BufferedImage>());
        tex_rows.add(new ArrayList<BufferedImage>());

        tex_rows.get(0).add(Textures.tileSetHouseBlocks.get(86));
        tex_rows.get(0).add(Textures.tileSetHouseBlocks.get(87));
        tex_rows.get(0).add(Textures.tileSetHouseBlocks.get(88));
        tex_rows.get(0).add(Textures.tileSetHouseBlocks.get(89));

        tex_rows.get(1).add(Textures.tileSetHouseBlocks.get(96));
        tex_rows.get(1).add(Textures.tileSetHouseBlocks.get(97));
        tex_rows.get(1).add(Textures.tileSetHouseBlocks.get(98));
        tex_rows.get(1).add(Textures.tileSetHouseBlocks.get(99));

        tex_rows.get(2).add(Textures.tileSetHouseBlocks.get(106));
        tex_rows.get(2).add(Textures.tileSetHouseBlocks.get(107));
        tex_rows.get(2).add(Textures.tileSetHouseBlocks.get(108));
        tex_rows.get(2).add(Textures.tileSetHouseBlocks.get(109));
    }

    public void tick() {
        int player_cenY = Game.player.getY() + (16 + 8) / 2;
        int tree_cenY = y + (16 * 2);

        if (player_cenY > tree_cenY) {
            this.setZIndex(Game.player.getZIndex() - 1);
        } else {
            this.setZIndex(Game.player.getZIndex() + 1);
        }
    }

    public void render(Graphics g) {
        renderTreeTiles(g, x, y, this.width);
    }

    private void renderTreeTiles(Graphics g, int local_x, int local_y, int tile_size) {
        int r = 0;
        int c = 0;
        for (ArrayList<BufferedImage> row : tex_rows) {
            for (BufferedImage tex : row) {
                g.drawImage(tex, local_x + (c * tile_size), local_y + (r * tile_size), tile_size, tile_size, null);
                c++;
            }
            r++;
            c = 0;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x + 8, y + 16, 16 * 3, 16 * 2 - 8);
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x + 16, y + 32, 32, 16);
    }

    public Item getItem() {
        return null;
    }

    public void interact() {
        Game.game_state = GAMESTATES.Menu;
    }

    @Override
    public void destroyed() {

    }

    @Override
    public void hit() {

    }

}
