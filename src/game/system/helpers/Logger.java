package game.system.helpers;

import game.system.main.Game;

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

public class Logger {
    private static String logs_dir = "logs";
    private static String log_filename = "combined";
    public static void print(String msg) {
        outputMessage(getStartAddition("INFO") + msg);
    }

    public static void printError(String msg) {
        outputMessage(getStartAddition("ERROR") + msg);
    }

    public static void printWarning(String msg) {
        outputMessage(getStartAddition("WARNING") + msg);
    }

    public static void printStackStrace() {
        outputMessage(getStartAddition("STACK_TRACE"));
    }

    private static String getStartAddition(String type) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy,HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String out = "[" + dtf.format(now) + "]";
        out += "[" + type + "]";
        out += "[" + stackTraceElements[3] + "]";
        return out + " >> ";
    }

    private static void outputMessage(String line) {
        System.out.println(line);
        if (!Game.DEV_MODE) {
            Helpers.createDirIfNotExisting(logs_dir);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();

            File log_file = new File(logs_dir + "/" + log_filename + ".log");
            try {
                FileWriter writer = new FileWriter(log_file, true);
                writer.write(line + "\n");
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
}
