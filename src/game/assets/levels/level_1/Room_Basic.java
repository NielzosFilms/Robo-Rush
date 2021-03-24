package game.assets.levels.level_1;

import game.assets.entities.TargetDummy;
import game.assets.entities.enemies.Shooting_Enemy;
import game.assets.levels.RoomDoorTrigger;
import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.assets.levels.def.RoomSpawner;
import game.assets.objects.BoundsObject;
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
import java.util.Random;

public class Room_Basic extends Room {
    private Random r = new Random();

    public Room_Basic(Point location, ROOM_TYPE room_type) {
        super(location);
        this.room_type = room_type;
        //addObject(new Tree(0, 0, 10, ID.Tree, BIOME.Forest));
        //addObject(new Tile_Static(0, 0, 1, new Texture(TEXTURE_LIST.dungeon, 0, 0)));

        this.next_wave_threshold = 2;

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

        int wave_count = r.nextInt(4) + 1;
        for(int i=0; i<wave_count; i++) {
            int enemy_count = r.nextInt(5) + 1;
            for(int j = 0; j<enemy_count; j++) {
                addEnemyToWave(i, new Shooting_Enemy(r.nextInt(200)-100, r.nextInt(200)-100));
            }
        }
        spawnNextWave();
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
        if(discovered) {
            Texture marker = new Texture(TEXTURE_LIST.minimap, 3, 5);
            g.drawImage(room_type.getTexture().getTexure(), x, y, room_size, room_size, null);
            if (active) {
                g.drawImage(marker.getTexure(), x, y, room_size, room_size, null);
            }
        }
    }
}
