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
import game.system.menu.Text;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;

public class House extends GameObject {

    private ArrayList<ArrayList<Texture>> tex_rows = new ArrayList<ArrayList<Texture>>();

    public House(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        tex_rows.add(new ArrayList<Texture>());
        tex_rows.add(new ArrayList<Texture>());
        tex_rows.add(new ArrayList<Texture>());

        tex_rows.get(0).add(new Texture(TEXTURE_LIST.house_list, 6, 8));
        tex_rows.get(0).add(new Texture(TEXTURE_LIST.house_list, 7, 8));
        tex_rows.get(0).add(new Texture(TEXTURE_LIST.house_list, 8, 8));
        tex_rows.get(0).add(new Texture(TEXTURE_LIST.house_list, 9, 8));

        tex_rows.get(1).add(new Texture(TEXTURE_LIST.house_list, 6, 9));
        tex_rows.get(1).add(new Texture(TEXTURE_LIST.house_list, 7, 9));
        tex_rows.get(1).add(new Texture(TEXTURE_LIST.house_list, 8, 9));
        tex_rows.get(1).add(new Texture(TEXTURE_LIST.house_list, 9, 9));

        tex_rows.get(2).add(new Texture(TEXTURE_LIST.house_list, 6, 10));
        tex_rows.get(2).add(new Texture(TEXTURE_LIST.house_list, 7, 10));
        tex_rows.get(2).add(new Texture(TEXTURE_LIST.house_list, 8, 10));
        tex_rows.get(2).add(new Texture(TEXTURE_LIST.house_list, 9, 10));
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
        for (ArrayList<Texture> row : tex_rows) {
            for (Texture tex : row) {
                g.drawImage(tex.getTexure(), local_x + (c * tile_size), local_y + (r * tile_size), tile_size, tile_size, null);
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
    public void hit(int damage) {

    }

}
