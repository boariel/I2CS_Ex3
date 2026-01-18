package server;

/**
 * Configuration parameters for your custom Pacman game server.
 * These can be modified through the settings menu.
 */
public class ServerConfig {
    public static String MY_ID = "1234";
    public static int CASE_SCENARIO = 0; // [0,4] - can be changed in menu
    public static long RANDOM_SEED = 31; // Random seed
    public static boolean CYCLIC_MODE = true; // can be changed in menu
    public static int DT = 200; // Delay time [20,200]
    public static double RESOLUTION_NORM = 1.2; // [0.75,1.2]
    private static PacManAlgo _manualAlgo = new ManualAlgo();
    public static final PacManAlgo ALGO = _manualAlgo;  // Uncomment for manual
}