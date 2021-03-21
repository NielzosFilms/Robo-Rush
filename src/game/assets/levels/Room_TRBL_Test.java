package game.assets.levels;

import game.assets.entities.enemies.Shooting_Enemy;
import game.assets.entities.enemies.Test_Enemy;
import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.enums.ID;
import game.system.helpers.JsonLoader;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class Room_TRBL_Test extends Room {
    public Room_TRBL_Test(Point location) {
        super(location);
        this.room_type = ROOM_TYPE.NESW;

        JsonLoader loader = new JsonLoader("assets/levels/level_1/starting room.json");
        this.objects = loader.getLoadedObjects();

        this.next_wave_threshold = 2;

        //addObject(new Tree(0, 0, 10, ID.Tree, BIOME.Forest));
//        addObject(new Tile_Static(0, 0, 1, new Texture(TEXTURE_LIST.dungeon, 1, 0)));
//        addObject(new RoomDoorTrigger(64, 0, new Point(1, 0)));
//        addObject(new RoomDoorTrigger(-64, 0, new Point(-1, 0)));
//        addObject(new RoomDoorTrigger(0, 64, new Point(0, -1)));
//        addObject(new RoomDoorTrigger(0, -64, new Point(0, 1)));

//        addEnemyToWave(0, new Shooting_Enemy(64, 64));
//        addEnemyToWave(0, new Shooting_Enemy(100, 64));
//        addEnemyToWave(0, new Shooting_Enemy(64, 100));
//        addEnemyToWave(0, new Shooting_Enemy(100, 100));
//
//        addEnemyToWave(1, new Shooting_Enemy(64, 64));
//        addEnemyToWave(1, new Shooting_Enemy(100, 64));
//        addEnemyToWave(1, new Shooting_Enemy(64, 100));
//
//        addEnemyToWave(2, new Shooting_Enemy(64, 64));
//        addEnemyToWave(2, new Shooting_Enemy(100, 100));


        closeDoors();
    }

    @Override
    public void tick() {
        if(this.currentEnemyCount() < this.next_wave_threshold) {
            if(nextWaveExists()) {
                spawnNextWave();
            } else {
                if(currentEnemyCount() == 0) {
                    openDoors();
                }
            }
        }
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

    @Override
    public void drawRoomMiniMap(Graphics g, int x, int y, int room_size, boolean active) {
        Texture icon = new Texture(TEXTURE_LIST.minimap, 2, 5);
        Texture marker = new Texture(TEXTURE_LIST.minimap, 3, 5);
        g.drawImage(room_type.getTexture().getTexure(), x, y, room_size, room_size, null);
        g.drawImage(icon.getTexure(), x, y, room_size, room_size, null);
        if(active) {
            g.drawImage(marker.getTexure(), x, y, room_size, room_size, null);
        }
    }
}
