package honours.ing.banq;

/**
 * @author Jeffrey Bakker
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Ansi {

    public static final String RESET = "\u001B[0m";

    public static final String BOLD_ON = "\u001B[1m";
    public static final String ITALICS_ON = "\u001B[3m";
    public static final String UNDERLINE_ON = "\u001B[4m";
    public static final String INVERSE_ON = "\u001B[7m";
    public static final String STRIKE_THROUGH_ON = "\u001B[9m";

    public static final String BOLD_OFF = "\u001B[22m";
    public static final String ITALICS_OFF = "\u001B[23m";
    public static final String UNDERLINE_OFF = "\u001B[24m";
    public static final String INVERSE_OFF = "\u001B[27m";
    public static final String STRIKE_THROUGH_OFF = "\u001B[29m";

    public static final String FG_BLACK = "\u001B[30m";
    public static final String FG_RED = "\u001B[31m";
    public static final String FG_GREEN = "\u001B[32m";
    public static final String FG_YELLOW = "\u001B[33m";
    public static final String FG_BLUE = "\u001B[34m";
    public static final String FG_PURPLE = "\u001B[35m";
    public static final String FG_CYAN = "\u001B[36m";
    public static final String FG_WHITE = "\u001B[37m";
    public static final String FG_DEFAULT = "\u001B[39m";

    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";
    public static final String BG_DEFAULT = "\u001B[49m";

}
