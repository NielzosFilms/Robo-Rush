package game.assets.levels.level_1;

import game.assets.entities.player.PLAYER_STAT;
import game.assets.items.Item_Ground;
import game.assets.items.item.Item;
import game.assets.levels.RoomDoorTrigger;
import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.assets.levels.def.RoomSpawner;
import game.assets.objects.BoundsObject;
import game.assets.objects.crate.Crate;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Room_Treasure extends Room {
    public Room_Treasure(Point location, ROOM_TYPE room_type) {
        super(location);
        this.room_type = room_type;

        Item item;
        if(new Random().nextInt(2) == 0) {
            item = new Item(new Texture(TEXTURE_LIST.items, 1, 0), ITEM_ID.power_up,
                    new HashMap<PLAYER_STAT, Float>() {{
                        put(PLAYER_STAT.move_speed, 0.5f);
                    }});
        } else {
            item = new Item(new Texture(TEXTURE_LIST.items, 1, 0), ITEM_ID.power_up,
                    new HashMap<PLAYER_STAT, Float>() {{
                        put(PLAYER_STAT.rate_of_fire, -6f);
                    }});
        }
        addObject(new Item_Ground(0, 0, 10, ID.Item, item));

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

        for(RoomSpawner spawner : room_type.getSpawners(location)) {
            Point door_direction = spawner.door_direction;
            addObject(new RoomDoorTrigger(door_direction.x*128 ,door_direction.y*128, spawner.door_direction));
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
        if(discovered) {
            Texture marker = new Texture(TEXTURE_LIST.minimap, 3, 5);
            Texture icon = new Texture(TEXTURE_LIST.minimap, 1, 5);
            g.drawImage(room_type.getTexture().getTexure(), x, y, room_size, room_size, null);
            g.drawImage(icon.getTexure(), x, y, room_size, room_size, null);
            if (active) {
                g.drawImage(marker.getTexure(), x, y, room_size, room_size, null);
            }
        }
    }
}
