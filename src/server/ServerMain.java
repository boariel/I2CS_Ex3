package server;

/**
 * Main class for running your custom Pac-Man game server.
 *
 * Controls:
 * - SPACE: Start/Pause game
 * - h: Help
 * - w,a,x,d: Manual controls (if ManualAlgo is selected)
 *
 * Configuration is done via ServerConfig.java
 */
public class ServerMain {
    private static Character _cmd;

    public static void main(String[] args) {
        playGame();
    }

    public static void playGame() {
        // Create your custom game instance
        MyGame game = new MyGame();

        // Initialize game with config parameters
        System.out.println(game.init(
                ServerConfig.CASE_SCENARIO,
                ServerConfig.MY_ID,
                ServerConfig.CYCLIC_MODE,
                ServerConfig.RANDOM_SEED,
                ServerConfig.RESOLUTION_NORM,
                ServerConfig.DT,
                -1
        ));

        // Get the algorithm (automatic or manual)
        PacManAlgo algo = ServerConfig.ALGO;

        System.out.println("=== YOUR CUSTOM PAC-MAN GAME ===");
        System.out.println("Algorithm: " + algo.getInfo());
        System.out.println("Press SPACE to start!");
        System.out.println("Press 'h' for help");
        System.out.println("================================");

        // Main game loop
        while (game.getStatus() != PacmanGame.DONE) {
            // Get keyboard input
            _cmd = game.getKeyChar();

            // Handle space bar (start/pause)
            if (_cmd != null && _cmd == ' ') {
                game.play();
            }

            // Handle help
            if (_cmd != null && _cmd == 'h') {
                System.out.println("=== HELP ===");
                System.out.println("SPACE - Start/Pause game");
                System.out.println("w - Move up (manual mode)");
                System.out.println("a - Move left (manual mode)");
                System.out.println("x - Move down (manual mode)");
                System.out.println("d - Move right (manual mode)");
                System.out.println("h - This help message");
                System.out.println("Configure game in ServerConfig.java");
                System.out.println("============");
            }

            // Get move from algorithm
            int direction = algo.move(game);

            // Execute the move
            game.move(direction);

            // Small delay for smooth gameplay
            try {
                Thread.sleep(ServerConfig.DT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Game over
        System.out.println(game.end(-1));
        System.out.println("=== GAME FINISHED ===");
        System.out.println("Thanks for playing!");
    }

    /**
     * Get the last command character (for ManualAlgo)
     */
    public static Character getCMD() {
        return _cmd;
    }
}