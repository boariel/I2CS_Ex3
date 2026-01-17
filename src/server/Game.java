package server;
import java.awt.Color;

/**
 * Utility class with game constants and helper methods.
 */
public class Game {
    // Direction constants
    public static final int UP = 1;
    public static final int DOWN = 3;
    public static final int LEFT = 2;
    public static final int RIGHT = 4;
    public static final int STAY = 0;
    public static final int ERR = -1;
    // Game state constants
    public static final int INIT = 0;
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static final int DONE = 3;
    /**
     * Convert a Color to an integer RGB value.
     */
    public static int getIntColor(Color c, int code) {
        return c.getRGB();
    }
}