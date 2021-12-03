package game.assets.levels.level_1;

import game.assets.levels.def.*;
import game.assets.objects.Door;
import game.assets.tiles.Tile_Static;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Level_1 extends Level {

    public Level_1() {
        super();
    }

    @Override
    protected void generateLevel(Random rand) {
        this.generator.generate();
        // convert the generation to tiles and objects;
        this.objects.addAll(this.generator.getDungeonInTiles(TEXTURE_LIST.dungeon));
        for(Point connection : this.generator.getConnections()) {
            this.objects.add(new Door(connection.x * 16, connection.y * 16, 10, false));
//            this.objects.add(new Tile_Static(connection.x * 16, connection.y * 16, 10, new Texture(TEXTURE_LIST.dungeon, 6, 7)));
        }
    }
}
