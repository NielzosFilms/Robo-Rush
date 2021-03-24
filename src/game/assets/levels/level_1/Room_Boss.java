package game.assets.levels.level_1;

import game.assets.entities.enemies.Boss_Enemy;
import game.assets.entities.enemies.Shooting_Enemy;
import game.assets.levels.RoomDoorTrigger;
import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.assets.levels.def.RoomSpawner;
import game.assets.objects.BoundsObject;
import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class Room_Boss extends Room {
    public Room_Boss(Point location, ROOM_TYPE room_type) {
        super(location);
        this.room_type = room_type;

//        addObject(new Enemy(0 ,0, 10, ID.Enemy));

        for(RoomSpawner spawner : room_type.getSpawners(location)) {
            Point door_direction = spawner.door_direction;
            addObject(new RoomDoorTrigger(door_direction.x*128 ,door_direction.y*128, spawner.door_direction));
        }

        addObject(new BoundsObject(-128, -128, 128, 16));
        addObject(new BoundsObject(-128, -128, 16, 128));

        addObject(new BoundsObject(16, -128, 128, 16));
        addObject(new BoundsObject(128, -128, 16, 128));

        addObject(new BoundsObject(-128, 16, 16, 128));
        addObject(new BoundsObject(-128, 128, 128, 16));

        addObject(new BoundsObject(128, 16, 16, 128));
        addObject(new BoundsObject(16, 128, 128, 16));

        addObject(new BoundsObject(0, -144, 16, 16));
        addObject(new BoundsObject(0, 144, 16, 16));
        addObject(new BoundsObject(-144, 0, 16, 16));
        addObject(new BoundsObject(144, 0, 16, 16));

        addEnemyToWave(0, new Boss_Enemy(0, 0));

        spawnNextWave();

        closeDoors();
    }

    @Override
    public void tick() {
        if(currentEnemyCount() == 0) {
            openDoors();
        }
    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void drawRoomMiniMap(Graphics g, int x, int y, int room_size, boolean active) {
        if(discovered) {
            Texture marker = new Texture(TEXTURE_LIST.minimap, 3, 5);
            Texture icon = new Texture(TEXTURE_LIST.minimap, 0, 5);
            g.drawImage(room_type.getTexture().getTexure(), x, y, room_size, room_size, null);
            g.drawImage(icon.getTexure(), x, y, room_size, room_size, null);
            if (active) {
                g.drawImage(marker.getTexure(), x, y, room_size, room_size, null);
            }
        }
    }
}
