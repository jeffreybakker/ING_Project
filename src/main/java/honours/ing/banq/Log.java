package honours.ing.banq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Jeffrey Bakker
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Log {

    private static final String BASE_MESSAGE = "%s[%s] %s -> %s%s"; // ANSI-SET PRIORITY TAG MSG ANSI-RESET
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
    private static final String LOGS_DIRECTORY = "./log";

    private static PrintStream file;
    private static PrintStream out;

    public enum Priority {
        DEBUG           (Ansi.FG_WHITE),
        VERBOSE         (Ansi.FG_CYAN),
        INFO            (Ansi.FG_GREEN),
        WARN            (Ansi.FG_YELLOW),
        ERROR           (Ansi.FG_RED),
        WTF             (Ansi.FG_BLACK + Ansi.BG_RED);

        private String colorCode;

        Priority(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getColorCode() {
            return colorCode;
        }
    }

    public static void d(String tag, String msg) {
        log(Priority.WARN, tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable t) {
        log(Priority.WARN, tag, msg, t);
    }

    public static void e(String tag, String msg) {
        log(Priority.WARN, tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable t) {
        log(Priority.WARN, tag, msg, t);
    }

    public static void i(String tag, String msg) {
        log(Priority.WARN, tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable t) {
        log(Priority.WARN, tag, msg, t);
    }

    public static void v(String tag, String msg) {
        log(Priority.WARN, tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable t) {
        log(Priority.WARN, tag, msg, t);
    }

    public static void w(String tag, String msg) {
        log(Priority.WARN, tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable t) {
        log(Priority.WARN, tag, msg, t);
    }

    public static void wtf(String tag, String msg) {
        log(Priority.WTF, tag, msg, null);
    }

    public static void wtf(String tag, String msg, Throwable t) {
        log(Priority.WTF, tag, msg, t);
    }

    public static synchronized void log(Priority priority, String tag, String msg, Throwable t) {
        file.println(String.format(
                BASE_MESSAGE,
                "", priority.name(), tag, msg, ""));
        out.println(String.format(
                BASE_MESSAGE,
                priority.getColorCode(), priority.name(), tag, msg, Ansi.RESET));

        if (t != null) {
            String[] lines = getStackTraceString(t).split("\n");
            for (String line : lines) {
                file.println(String.format(
                        BASE_MESSAGE,
                        "", priority.name(), tag, line, ""));
                out.println(String.format(
                        BASE_MESSAGE,
                        priority.getColorCode(), priority.name(), tag, line, Ansi.RESET));
            }
        }
    }

    public static String getStackTraceString(Throwable t) {
        StringBuilder res = new StringBuilder();
        res.append(t.getClass().getName()).append(": ").append(t.getMessage()).append('\n');

        for (StackTraceElement ste : t.getStackTrace()) {
            res.append("\tat ").append(ste.toString()).append('\n');
        }

        return res.toString();
    }

    public static void start() throws IOException {
        Date now = Calendar.getInstance().getTime();

        File dir = new File(LOGS_DIRECTORY);
        dir.mkdirs();

        File file = new File(LOGS_DIRECTORY + "/" + DATE_FORMAT.format(now) + ".txt");
        file.createNewFile();

        Log.file = new PrintStream(new FileOutputStream(file));
        Log.out = System.out;
    }

    public static void stop() {
        file.close();
    }

}
