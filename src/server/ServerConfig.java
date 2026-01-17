package server;

/**
 * Configuration parameters for your custom Pacman game server.
 */
public class ServerConfig {
    public static final String MY_ID = "1234";
    public static final int CASE_SCENARIO = 4; // [0,4]
    public static final long RANDOM_SEED = 31; // Random seed
    public static final boolean CYCLIC_MODE = true;
    public static final int DT = 200; // Delay time [20,200]
    public static final double RESOLUTION_NORM = 1.2; // [0.75,1.2]

    // Algorithm selection
    private static PacManAlgo _manualAlgo = new ManualAlgo();
    private static PacManAlgo _myAlgo = new SmartAlgo();

    // Switch between manual and automatic mode
    // public static final PacManAlgo ALGO = _manualAlgo;  // Uncomment for manual
    public static final PacManAlgo ALGO = _myAlgo;  // Automatic mode
}