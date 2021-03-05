package game.assets.levels.level_1;

import game.assets.levels.RoomDoorTrigger;
import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.assets.levels.def.RoomSpawner;
import game.assets.objects.crate.Crate;
import game.enums.ID;

import java.awt.*;

public class Room_Treasure extends Room {
    public Room_Treasure(Point location, ROOM_TYPE room_type) {
        super(location);
        this.room_type = room_type;

        addObject(new Crate(0, 0, 10, ID.Crate));

        for(RoomSpawner spawner : room_type.getSpawners(location)) {
            Point door_direction = spawner.door_direction;
            addObject(new RoomDoorTrigger(door_direction.x*64 ,door_direction.y*64, door_direction));
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void drawRoomMiniMap(Graphics g, int x, int y, int room_size, boolean active) {
        if(active) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.orange);
        }
        g.fillRect(x, y, room_size, room_size);
        g.setColor(Color.darkGray);
        g.drawRect(x, y, room_size, room_size);
    }
}
