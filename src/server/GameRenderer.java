package server;
import java.awt.Color;

public class GameRenderer {
    private static final int CELL_SIZE = 18; // Smaller cells for larger maze
    private boolean initialized = false;

    public void draw(MyGame game) {
        int[][] board = game.getGame(0);

        if (!initialized) {
            // Initialize canvas
            int width = board.length * CELL_SIZE;
            int height = board[0].length * CELL_SIZE + 60;
            StdDraw.setCanvasSize(width, height);
            StdDraw.setXscale(0, width);
            StdDraw.setYscale(0, height);
            StdDraw.enableDoubleBuffering();
            initialized = true;
        }

        StdDraw.clear(Color.BLACK);

        // Get color codes
        int blueColor = game.getBlueColor();
        int pinkColor = game.getPinkColor();
        int greenColor = game.getGreenColor();
        int blackColor = game.getBlackColor();

        // Draw board
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                drawCell(x, y, board[x][y], blueColor, pinkColor, greenColor, blackColor);
            }
        }

        // Draw Pac-Man
        String[] posStr = game.getPos(0).split(",");
        Index2D pacmanPos = new Index2D(
                Integer.parseInt(posStr[0]),
                Integer.parseInt(posStr[1])
        );
        drawPacMan(pacmanPos, board[0].length);

        // Draw ghosts
        GhostCL[] ghosts = game.getGhosts(0);
        if (ghosts != null) {
            for (GhostCL ghost : ghosts) {
                drawGhost(ghost, board[0].length);
            }
        }

        // Draw HUD
        drawHUD(game, board[0].length);

        StdDraw.show();
    }

    private void drawCell(int x, int y, int cellColor, int blueColor, int pinkColor,
                          int greenColor, int blackColor) {
        double cx = (x + 0.5) * CELL_SIZE;
        // Flip Y coordinate for proper display
        double cy = (y + 0.5) * CELL_SIZE;

        if (cellColor == blueColor) {
            // Wall
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.filledSquare(cx, cy, CELL_SIZE / 2.0);
            StdDraw.setPenColor(Color.CYAN);
            StdDraw.square(cx, cy, CELL_SIZE / 2.0);
        } else if (cellColor == pinkColor) {
            // Dot
            StdDraw.setPenColor(Color.PINK);
            StdDraw.filledCircle(cx, cy, 2);
        } else if (cellColor == greenColor) {
            // Power pellet
            StdDraw.setPenColor(Color.GREEN);
            StdDraw.filledCircle(cx, cy, 5);
        }
        // Black/empty cells - draw nothing (already cleared to black)
    }

    private void drawPacMan(Index2D pos, int height) {
        double cx = (pos.getX() + 0.5) * CELL_SIZE;
        double cy = (pos.getY() + 0.5) * CELL_SIZE;

        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.filledCircle(cx, cy, CELL_SIZE * 0.4);

        // Draw mouth (simple triangle)
        StdDraw.setPenColor(Color.BLACK);
        double[] mouthX = {cx, cx + CELL_SIZE * 0.3, cx};
        double[] mouthY = {cy, cy, cy + CELL_SIZE * 0.3};
        StdDraw.filledPolygon(mouthX, mouthY);
    }

    private void drawGhost(GhostCL ghost, int height) {
        String[] posStr = ghost.getPos(0).split(",");
        Index2D ghostPos = new Index2D(
                Integer.parseInt(posStr[0]),
                Integer.parseInt(posStr[1])
        );

        double cx = (ghostPos.getX() + 0.5) * CELL_SIZE;
        double cy = (ghostPos.getY() + 0.5) * CELL_SIZE;

        // Change appearance based on eatable status
        if (ghost.remainTimeAsEatable(0) > 0) {
            // Eatable ghost - blue and smaller
            StdDraw.setPenColor(Color.CYAN);
            StdDraw.filledCircle(cx, cy, CELL_SIZE * 0.3);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.circle(cx, cy, CELL_SIZE * 0.3);
        } else {
            // Normal ghost - red and larger
            StdDraw.setPenColor(Color.RED);
            StdDraw.filledCircle(cx, cy, CELL_SIZE * 0.4);

            // Draw eyes
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.filledCircle(cx - 3, cy + 2, 2);
            StdDraw.filledCircle(cx + 3, cy + 2, 2);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.filledCircle(cx - 3, cy + 2, 1);
            StdDraw.filledCircle(cx + 3, cy + 2, 1);
        }
    }

    private void drawHUD(MyGame game, int boardHeight) {
        int hudY = boardHeight * CELL_SIZE + 30;

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));

        // Draw game info
        String info = game.getData(0);
        StdDraw.text(100, hudY, info);

        // Draw status
        int status = game.getStatus();
        String statusText = "";
        if (status == PacmanGame.INIT) statusText = "INIT";
        else if (status == PacmanGame.PLAY) statusText = "PLAYING";
        else if (status == PacmanGame.PAUSE) statusText = "PAUSED - Press SPACE to start";
        else if (status == PacmanGame.DONE) {
            statusText = "GAME OVER!";
            StdDraw.setPenColor(Color.RED);
        }

        StdDraw.text(300, hudY, statusText);
    }
}