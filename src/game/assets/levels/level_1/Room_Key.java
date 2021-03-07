package game.assets.levels.level_1;

import game.assets.items.Item_Ground;
import game.assets.items.item.Item;
import game.assets.levels.RoomDoorTrigger;
import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.assets.levels.def.RoomSpawner;
import game.assets.tiles.Tile_Static;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.LinkedList;

public class Room_Key extends Room {
    private Item_Ground key;

    public Room_Key(Point location, ROOM_TYPE room_type) {
        super(location);

        this.room_type = room_type;

        addObject(new Tile_Static(0, 0, 0, new Texture(TEXTURE_LIST.dungeon, 8, 1)));

        this.key = new Item_Ground(0, 0, 10 , ID.Item, new Item(new Texture(TEXTURE_LIST.minimap, 0, 6), ITEM_ID.key));
        addObject(this.key);

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
        Texture marker = new Texture(TEXTURE_LIST.minimap, 3, 5);
        Texture icon = new Texture(TEXTURE_LIST.minimap, 0, 6);
        g.drawImage(room_type.getTexture().getTexure(), x, y, room_size, room_size, null);
        if(hasKey()) g.drawImage(icon.getTexure(), x, y, room_size, room_size, null);
        if(active) {
            g.drawImage(marker.getTexure(), x, y, room_size, room_size, null);
        }
    }

    private boolean hasKey() {
        for(LinkedList<GameObject> layer : objects) {
            for (GameObject object : layer) {
                if (object == key) {
                    return true;
                }
            }
        }
        return false;
    }
}
