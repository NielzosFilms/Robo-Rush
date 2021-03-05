package game.assets.levels.def;

import game.system.helpers.Offsets;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.LinkedList;

public enum ROOM_TYPE {
    NESW {
      public LinkedList<RoomSpawner> getSpawners(Point location) {
          LinkedList<RoomSpawner> spawners = new LinkedList<>();
          spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
          spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
          spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
          spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
          return spawners;
      }
      public Texture getTexture() {
          return new Texture(TEXTURE_LIST.minimap, 1, 0);
      }
    },

    N {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 0, 1);
        }
    },
    S {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 2, 1);
        }
    },
    W {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 3, 1);
        }
    },
    E {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 1, 1);
        }
    },

    NS {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 2, 2);
        }
    },
    EW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 3, 2);
        }
    },

    NW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 1, 3);
        }
    },
    NE {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 0, 3);
        }
    },

    SW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 1, 2);
        }
    },
    SE {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 0, 2);
        }
    },

    NES {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 0, 4);
        }
    },
    ESW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x-1, location.y), new Point(-1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 1, 4);
        }
    },
    NSW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x-1, location.y), new Point(-1, 0)));
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 2, 4);
        }
    },
    NEW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x-1, location.y), new Point(-1, 0)));
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
        public Texture getTexture() {
            return new Texture(TEXTURE_LIST.minimap, 3, 4);
        }
    };

    public abstract LinkedList<RoomSpawner> getSpawners(Point location);
    public abstract Texture getTexture();
}
