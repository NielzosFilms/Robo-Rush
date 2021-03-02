package game.assets.levels.def;

import game.system.helpers.Offsets;

import java.awt.*;
import java.util.LinkedList;

public enum ROOM_TYPE {
    TBLR {
      public LinkedList<RoomSpawner> getSpawners(Point location) {
          LinkedList<RoomSpawner> spawners = new LinkedList<>();
          spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
          spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
          spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
          spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
          return spawners;
      }
    },

    T {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            return spawners;
        }
    },
    B {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            return spawners;
        }
    },
    L {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            return spawners;
        }
    },
    R {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
    },

    TB {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            return spawners;
        }
    },
    LR {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
    },

    TL {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            return spawners;
        }
    },
    TR {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
    },

    BL {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x - 1, location.y), new Point(-1, 0)));
            return spawners;
        }
    },
    BR {
        public LinkedList<RoomSpawner> getSpawners(Point location) {
            LinkedList<RoomSpawner> spawners = new LinkedList<>();
            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
            return spawners;
        }
    },;

//    TBR {
//        public LinkedList<RoomSpawner> getSpawners(Point location) {
//            LinkedList<RoomSpawner> spawners = new LinkedList<>();
//            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
//            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
//            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
//            return spawners;
//        }
//    },
//    RBL {
//        public LinkedList<RoomSpawner> getSpawners(Point location) {
//            LinkedList<RoomSpawner> spawners = new LinkedList<>();
//            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
//            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
//            spawners.add(new RoomSpawner(new Point(location.x-1, location.y), new Point(-1, 0)));
//            return spawners;
//        }
//    },
//    BLT {
//        public LinkedList<RoomSpawner> getSpawners(Point location) {
//            LinkedList<RoomSpawner> spawners = new LinkedList<>();
//            spawners.add(new RoomSpawner(new Point(location.x, location.y + 1), new Point(0, 1)));
//            spawners.add(new RoomSpawner(new Point(location.x-1, location.y), new Point(-1, 0)));
//            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
//            return spawners;
//        }
//    },
//    LTR {
//        public LinkedList<RoomSpawner> getSpawners(Point location) {
//            LinkedList<RoomSpawner> spawners = new LinkedList<>();
//            spawners.add(new RoomSpawner(new Point(location.x-1, location.y), new Point(-1, 0)));
//            spawners.add(new RoomSpawner(new Point(location.x, location.y - 1), new Point(0, -1)));
//            spawners.add(new RoomSpawner(new Point(location.x + 1, location.y), new Point(1, 0)));
//            return spawners;
//        }
//    };

    public abstract LinkedList<RoomSpawner> getSpawners(Point location);
}
