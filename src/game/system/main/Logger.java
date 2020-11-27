package game.system.main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Logger {
    public static void print(String msg) {
        System.out.println(getStartAddition("INFO") + msg);
    }

    public static void printError(String msg) {
        System.out.println(getStartAddition("ERROR") + msg);
    }

    public static void printWarning(String msg) {
        System.out.println(getStartAddition("WARNING") + msg);
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
}
