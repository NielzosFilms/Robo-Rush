package game.assets.levels.level_1;

import game.assets.entities.TargetDummy;
import game.assets.levels.RoomDoorTrigger;
import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.assets.levels.def.RoomSpawner;
import game.assets.objects.tree.Tree;
import game.assets.tiles.Tile_Static;
import game.enums.BIOME;
import game.enums.ID;
import game.system.systems.gameObject.GameObject;
import game.textures.Fonts;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.LinkedList;

public class Room_Basic extends Room {

    public Room_Basic(Point location, ROOM_TYPE room_type) {
        super(location);
        this.room_type = room_type;
        //addObject(new Tree(0, 0, 10, ID.Tree, BIOME.Forest));
        addObject(new Tile_Static(0, 0, 1, new Texture(TEXTURE_LIST.dungeon, 0, 0)));

        for(RoomSpawner spawner : room_type.getSpawners(location)) {
            Point door_direction = spawner.door_direction;
            addObject(new RoomDoorTrigger(door_direction.x*64 ,door_direction.y*64, door_direction));
        }
    }

    @Override
    public void tick() {

    }

    public void render(Graphics g) {
//        int factor = 32;
//        Point coords = new Point(location.x*factor, location.y*factor);
//        Point center = new Point(coords.x + factor/2, coords.y + factor/2);
//        g.setColor(Color.white);
//        g.drawRect(coords.x, coords.y, 32, 32);
//
//        for(RoomSpawner spawner : room_type.getSpawners(location)) {
//            Point offset = spawner.door_direction;
//            g.setColor(Color.orange);
//            g.fillRect(center.x + offset.x*13, center.y + offset.y * 13, 2, 2);
//        }
//
//        g.setColor(Color.white);
//        g.setFont(Fonts.default_fonts.get(5));
//        g.drawString(room_type.toString(), center.x, center.y);
    }

    public void drawRoomMiniMap(Graphics g, int x, int y, int room_size, boolean active) {
        if(active) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(Color.gray);
        }
        g.fillRect(x, y, room_size, room_size);
        g.setColor(Color.darkGray);
        g.drawRect(x, y, room_size, room_size);
    }
}