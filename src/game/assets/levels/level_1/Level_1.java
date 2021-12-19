package game.assets.levels.level_1;

import game.assets.entities.enemies.Shooting_Enemy;
import game.assets.levels.def.*;
import game.assets.objects.Door;
import game.assets.tiles.Tile_Static;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.systems.gameObject.GameObject;
import game.system.systems.levelGeneration.Room;
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
        for (Point connection : this.generator.getConnections()) {
            this.objects.add(new Door(connection.x * 16, connection.y * 16, 10, false));
//            this.objects.add(new Tile_Static(connection.x * 16, connection.y * 16, 10, new Texture(TEXTURE_LIST.dungeon, 6, 7)));
        }

        addEnemies();
    }

    private void addEnemies() {
        Random r = this.generator.getRandom();
        int maxEnemies = 5;
        int minEnemies = 1;
        for (Room room : this.generator.getRooms()) {
            Rectangle rect = convertRectToWorld(room.rect);
//            int numEnemies = getRandNumBetw(minEnemies, maxEnemies, r);
//            for (int i = 0; i < numEnemies; i++) {
//                Point pos = getRandomPosInRect(rect, 0, r);
//                this.objects.add(new Shooting_Enemy(pos.x, pos.y));
//            }
            Point pos = getRandomPosInRect(rect, 0, r);
            this.objects.add(new Shooting_Enemy(pos.x, pos.y));
        }
    }

    private Point getRandomPosInRect(Rectangle rect, int margin, Random r) {
        int x = getRandNumBetw(rect.x + margin, rect.x + rect.width - margin, r);
        int y = getRandNumBetw(rect.y + margin, rect.y + rect.height - margin, r);

        return new Point(x, y);
    }

    private int getRandNumBetw(int low, int high, Random r) {
        return r.nextInt(high - low) + low;
    }

    private Rectangle convertRectToWorld(Rectangle rect) {
        return new Rectangle(rect.x * 16, rect.y * 16, rect.width * 16, rect.height * 16);
    }
}
