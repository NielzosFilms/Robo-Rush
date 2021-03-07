package game.assets.levels.level_1;

import game.assets.entities.enemies.Enemy;
import game.assets.levels.RoomDoorTrigger;
import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.assets.levels.def.RoomSpawner;
import game.enums.ID;
import game.system.helpers.JsonLoader;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.LinkedList;

public class Room_Boss extends Room {
    public Room_Boss(Point location, ROOM_TYPE room_type) {
        super(location);
        this.room_type = room_type;

//        addObject(new Enemy(0 ,0, 10, ID.Enemy));

        for(RoomSpawner spawner : room_type.getSpawners(location)) {
            Point door_direction = spawner.door_direction;
            RoomDoorTrigger doorTrigger = new RoomDoorTrigger(door_direction.x*64 ,door_direction.y*64, door_direction);
            //doorTrigger.setNeedsKey(true);
            addObject(doorTrigger);
        }

        addEnemyToWave(0, new Enemy(0 ,0, 10, ID.Enemy));

        addEnemyToWave(1, new Enemy(0 ,0, 10, ID.Enemy));
        addEnemyToWave(1, new Enemy(-64 ,-64, 10, ID.Enemy));

        spawnCurrentWave();
    }

    @Override
    public void tick() {
        if(getEnemyCount() == 0) {
            spawnCurrentWave();
        }
    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void drawRoomMiniMap(Graphics g, int x, int y, int room_size, boolean active) {
        Texture marker = new Texture(TEXTURE_LIST.minimap, 3, 5);
        Texture icon = new Texture(TEXTURE_LIST.minimap, 0, 5);
        g.drawImage(room_type.getTexture().getTexure(), x, y, room_size, room_size, null);
        g.drawImage(icon.getTexure(), x, y, room_size, room_size, null);
        if(active) {
            g.drawImage(marker.getTexure(), x, y, room_size, room_size, null);
        }
    }

    private int getEnemyCount() {
        if(this.objects.size() < Game.gameController.getPlayer().getZIndex()) return -1;
        int enemy_count = 0;
        for(GameObject entity : this.objects.get(Game.gameController.getPlayer().getZIndex())) {
            if(entity.getId() == ID.Enemy) enemy_count++;
        }
        return enemy_count;
    }
}
