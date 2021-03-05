package game.assets.levels;

import game.assets.entities.enemies.Enemy;
import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.enums.ID;
import game.system.helpers.JsonLoader;

import java.awt.*;

public class Room_TRBL_Test extends Room {
    public Room_TRBL_Test(Point location) {
        super(location);
        this.room_type = ROOM_TYPE.NESW;

        JsonLoader loader = new JsonLoader("assets/levels/level_1/starting room.json");
        this.objects = loader.getLoadedObjects();

        //addObject(new Tree(0, 0, 10, ID.Tree, BIOME.Forest));
//        addObject(new Tile_Static(0, 0, 1, new Texture(TEXTURE_LIST.dungeon, 1, 0)));
//        addObject(new RoomDoorTrigger(64, 0, new Point(1, 0)));
//        addObject(new RoomDoorTrigger(-64, 0, new Point(-1, 0)));
//        addObject(new RoomDoorTrigger(0, 64, new Point(0, -1)));
//        addObject(new RoomDoorTrigger(0, -64, new Point(0, 1)));

        addObject(new Enemy(0 ,0, 10, ID.Enemy));
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
//        int factor = 64;
//        Point center = new Point(location.x*factor - (location.x*factor/2), location.y*factor - (location.y*factor/2));
//        g.setColor(Color.yellow);
//        g.fillRect(center.x+16, center.y+16, 2, 2);
//
//        g.setColor(Color.white);
//        g.drawRect(center.x, center.y, 32, 32);
    }
}
