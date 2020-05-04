import javafx.application.Application;
import javafx.stage.Stage;

public class Settings {

    public static boolean FULLSCREEN_MODE = true; // set to false if you're Brazilien
    public static boolean ACTIVE_DB_MODE = false; // is DB activated
    public static boolean ACTIVE_EMAIL_CONFIRM = false; // if true confirmation email will be sent
    public static boolean DEBUG_MODE = true; // debug if true (Show Exception traces etc)
    public static boolean SKIP_AUTHENTICATION = true; // skip authentication

    public static Stage appStage;
    public static Application application;
    public static int SIDEBAR_STATE = 0;
    public static String dataPath = "C:\\Users\\hbais\\Desktop\\P2CP\\CodeWin\\CodeWin\\src\\data\\";
    public static String projectPath = "C:\\Users\\hbais\\Desktop\\P2CP\\CodeWin\\CodeWin\\src\\";

    // display-what modes
    public static int DISPLAYING_COURSE_OVERVIEW = 0x00;
    public static int DISPLAYING_CHAPTER_OVERVIEW = 0x01;
    public static int DISPLAYING_COURSE = 0x02;
    public static int DISPLAYING_STATISTICS = 0x03;
    public static int DISPLAYING_PARAMETERS = 0x04;
    public static int DISPLAYING_QUESTION = 0x05;

    // sidebar states
    public static int SIDEBAR_EXPANDED = 0x00;
    public static int SIDEBAR_SHRINKED = 0x01;
    public static double SIDEBAR_DELTA = 0; // set after LoggedIn initialized (and stage shown)

    public static double SIDEBAR_WIDTH = 0;

    public static double SIDEBAR_EXTEND_COEFF = 4;

    // social
    public static String githubLink = "https://github.com/hbFree/CodeWin";

    // log files
    public static String logfile = "C:\\Users\\hbais\\Desktop\\P2CP\\CodeWin\\CodeWin\\errors.log";
}
