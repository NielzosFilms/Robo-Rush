package game.assets.levels.def;

import game.system.helpers.Offsets;

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
    },

    N {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            return spawners;
        }
    },
    S {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            return spawners;
        }
    },
    W {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            return spawners;
        }
    },
    E {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
    },

    NS {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            return spawners;
        }
    },
    EW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
    },

    NW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            return spawners;
        }
    },
    NE {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
    },

    SW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            return spawners;
        }
    },
    SE {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
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
    },
    ESW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x-1, location.y), new Point(-1, 0)));
            return spawners;
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
    },
    NEW {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x-1, location.y), new Point(-1, 0)));
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
    };

    public abstract LinkedList<RoomSpawner> getSpawners(Point location);
}
