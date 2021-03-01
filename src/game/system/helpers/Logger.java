package game.system.helpers;

import game.assets.levels.def.ROOM_TYPE;
import game.assets.levels.def.Room;
import game.assets.levels.def.RoomSpawner;
import game.system.main.Game;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Logger {
    private static String logs_dir = "logs";
    private static String log_filename = "combined";
    public static void print(String msg) {
        outputMessage("INFO", msg);
    }

    public static void printError(String msg) {
        outputMessage("ERROR", msg);
    }

    public static void printWarning(String msg) {
        outputMessage("WARNING", msg);
    }

    public static void printStackStrace() {
        outputMessage("STACK_TRACE", "");
    }

    private static String getStartAddition(String type) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy,HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String out = "[" + dtf.format(now) + "]";
        out += "[" + type + "]";
        out += "[" + stackTraceElements[4] + "]";
        return out;
    }

    private static void outputMessage(String type, String line) {
        if(!line.equals("")) {
            line = " >> " + line;
        }
        System.out.println(getStartAddition(type) + line);
        if (!Game.DEV_MODE) {
            Helpers.createDirIfNotExisting(logs_dir);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();

            File log_file = new File(logs_dir + "/" + log_filename + ".log");
            try {
                FileWriter writer = new FileWriter(log_file, true);
                writer.write(getStartAddition(type) + line + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearLogs() {
        File log_file = new File(logs_dir + "/" + log_filename + ".log");
        if(log_file.exists()) {
            log_file.delete();
        }
    }

    public static void printRoomMatrix(HashMap<Point, Room> rooms, int room_count, LinkedList<RoomSpawner> spawners) {
        System.out.println("--- Generated Level ---");
        String no_room = " . ", room = " 0 ";
        for(int y=-room_count+1; y<room_count; y++) {
            StringBuilder row = new StringBuilder();
            for(int x=-room_count+1; x<room_count; x++) {
                if(rooms.containsKey(new Point(x, y))) {
                    if(x == 0 && y == 0) {
                        row.append(" x ");
                    } else if(rooms.get(new Point(x, y)).getRoomType() == ROOM_TYPE.TBLR) {
                        row.append(" O ");
                    } else {
                        row.append(room);
                    }
                } else {
                    boolean appended = false;
                    for(RoomSpawner spawner : spawners) {
                        if(spawner.location.equals(new Point(x, y))) {
                            row.append(" * ");
                            appended = true;
                            break;
                        }
                    }
                    if(!appended)
                        row.append(no_room);
                }
            }
            System.out.println(row.toString());
        }
        System.out.println("-----------------------");
    }
}
