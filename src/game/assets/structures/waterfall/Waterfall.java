package game.assets.structures.waterfall;

import game.enums.ID;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.Interactable;
import game.system.world.biome_groups.BiomeGroup_Cave;
import game.textures.TEXTURE_LIST;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.textures.Animation;
import game.textures.Texture;
import game.textures.Textures;

import java.awt.*;
import java.util.HashMap;

public class Waterfall extends GameObject implements Bounds, Interactable {
    private HashMap<Point, Texture> tiles = new HashMap<>();
    private HashMap<Point, Animation> water_tiles = new HashMap<>();
    private Structure_Waterfall structure;

    public Waterfall(int x, int y, int z_index) {
        super(x, y, z_index, ID.Waterfall);

        tiles.put(new Point(0, 0), new Texture(TEXTURE_LIST.waterfall_list, 0, 0));
        tiles.put(new Point(0, 1), new Texture(TEXTURE_LIST.waterfall_list, 0, 1));
        tiles.put(new Point(0, 2), new Texture(TEXTURE_LIST.waterfall_list, 0, 2));

        tiles.put(new Point(2, 0), new Texture(TEXTURE_LIST.waterfall_list, 2, 0));
        tiles.put(new Point(2, 1), new Texture(TEXTURE_LIST.waterfall_list, 2, 1));
        tiles.put(new Point(2, 2), new Texture(TEXTURE_LIST.waterfall_list, 2, 2));

        int init_x = 0;
        int init_y = 4;
        for(int yy=0; yy<=2; yy++) {
            for(int xx=0; xx<=2; xx++) {
                water_tiles.put(new Point(xx, yy), new Animation(Textures.water_speed,
                        new Texture(TEXTURE_LIST.waterfall_list, init_x + xx, init_y + yy),
                        new Texture(TEXTURE_LIST.waterfall_list, init_x + xx + 3, init_y + yy),
                        new Texture(TEXTURE_LIST.waterfall_list, init_x + xx + 6, init_y + yy)
                        ));
            }
        }

//        structure = new Structure_Waterfall(Game.gameController.getSeed(), this, new BiomeGroup_Cave());
    }

    public void tick() {
        int player_cenY = (int) Game.gameController.getPlayer().getBounds().getCenterY();
        int cenY = (int) getBounds().getCenterY();

        if (player_cenY > cenY) {
            this.setZIndex(Game.gameController.getPlayer().getZIndex() - 1);
        } else {
            this.setZIndex(Game.gameController.getPlayer().getZIndex() + 1);
        }

        for(Point key : water_tiles.keySet()) {
            water_tiles.get(key).runAnimation();
        }
    }

    public void render(Graphics g) {
        for(Point key : tiles.keySet()) {
            g.drawImage(tiles.get(key).getTexure(), x + key.x * 16, y + key.y * 16, 16, 16, null);
        }
        for(Point key : water_tiles.keySet()) {
            water_tiles.get(key).drawAnimation(g, x + key.x * 16, y + key.y * 16, 16, 16);
        }
        if(Game.DEBUG_MODE) {
            g.setColor(Color.green);
            g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y+16, 3 * 16, 2 * 16);
    }

    @Override
    public Rectangle getTopBounds() {
        return null;
    }

    @Override
    public Rectangle getBottomBounds() {
        return null;
    }

    @Override
    public Rectangle getLeftBounds() {
        return null;
    }

    @Override
    public Rectangle getRightBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x + 16, y + 16, 16, 32);
    }

    public void interact() {
//        Game.world.setActiveStructure(structure);
    }
}
