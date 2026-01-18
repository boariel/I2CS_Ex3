package server;

import java.awt.*;

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
        // Main loop - keep showing menu after each game
        boolean keepPlaying = true;

        while (keepPlaying) {
            // Show menu first
            MenuScreen menu = new MenuScreen();
            MenuScreen.MenuState result = menu.show();

            if (result == MenuScreen.MenuState.PLAY) {
                // Start the game
                playGame();
            } else {
                // Exit if menu was closed
                keepPlaying = false;
            }
        }

        System.out.println("Thanks for playing!");
        System.exit(0);
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
                System.out.println("s - Move down (manual mode)");
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
        String finalResult = game.end(-1);
        System.out.println("=== GAME FINISHED ===");

        // Pass game stats to game over screen
        boolean won = game.getLives() > 0;
        int finalScore = game.getScore();
        showGameOverScreen(won, finalScore);
    }

    /**
     * Show game over screen and ask if player wants to continue.
     * Returns true to return to menu, false to exit.
     */
    private static boolean showGameOverScreen(boolean won, int score) {
        // Give a moment to see the final game state
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Reset canvas to menu size
        StdDraw.setCanvasSize(600, 500);
        StdDraw.setXscale(0, 600);
        StdDraw.setYscale(0, 500);

        StdDraw.clear(Color.BLACK);

        // Game Over title
        if (won) {
            StdDraw.setPenColor(Color.GREEN);
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 50));
            StdDraw.text(300, 350, "YOU WIN!");
        } else {
            StdDraw.setPenColor(Color.RED);
            StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 50));
            StdDraw.text(300, 350, "GAME OVER");
        }

        // Show final score
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
        StdDraw.text(300, 280, "Final Score: " + score);

        // Instructions
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 20));
        StdDraw.text(300, 210, "Press M to return to Menu");

        StdDraw.show();

        // Wait for user input
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'm' || key == 'M') {
                    return true; // Return to menu
                }
            }
            StdDraw.pause(10);
        }
    }

    /**
     * Get the last command character (for ManualAlgo)
     */
    public static Character getCMD() {
        return _cmd;
    }
}