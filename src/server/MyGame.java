package server;

import java.awt.*;

public class MyGame implements PacmanGame {
    // Game states
    private int status;
    private int[][] board;
    private Index2D pacmanPos;
    private MyGhost[] ghosts;
    private int score;
    private int lives;
    private boolean cyclic;
    private int level;
    private int dt;
    private double eatableTime;
    private GameRenderer renderer;
    private Character lastKeyChar;

    // Color codes
    private int blueColor;
    private int pinkColor;
    private int greenColor;
    private int blackColor;
    private int yellowColor;

    public MyGame() {
        this.status = INIT;
        this.lives = 3;
        this.score = 0;

        // Initialize color codes
        blueColor = Color.BLUE.getRGB();
        pinkColor = Color.PINK.getRGB();
        greenColor = Color.GREEN.getRGB();
        blackColor = Color.BLACK.getRGB();
        yellowColor = Color.YELLOW.getRGB();
    }

    @Override
    public String init(int level, String id, boolean isCyclic, long seed,
                       double resolutionNorm, int dt, int ghostType) {
        this.status = INIT;
        this.level = level;
        this.cyclic = isCyclic;
        this.dt = dt;
        this.eatableTime = 10.0; // 10 seconds of power mode
        this.lives = 3;
        this.score = 0;

        // Load level
        loadLevel(level);

        // Find Pac-Man starting position
        pacmanPos = findStartPosition();

        // Create ghosts (more ghosts on higher levels)
        // First ghost is always smart (GREEDY_SP), rest are random walkers
        int numGhosts = level;
        ghosts = new MyGhost[numGhosts];
        for (int i = 0; i < numGhosts; i++) {
            Index2D ghostStart = findGhostStartPos(i);
            if (i == 3) {
                // Forth ghost is smart
                ghosts[i] = new MyGhost(GhostCL.GREEDY_SP, ghostStart, seed + i);
            } else {
                // Other ghosts are random walkers
                ghosts[i] = new MyGhost(GhostCL.RANDOM_WALK1, ghostStart, seed + i);
            }
        }

        // Initialize renderer
        renderer = new GameRenderer();

        this.status = PAUSE; // Start paused

        return "Game Level: " + level + ", ID: " + id + ", Cyclic: " + isCyclic +
                ", Seed: " + seed + ", DT: " + dt + ", Ghosts: " + numGhosts;
    }

    private void loadLevel(int level) {
        // All levels use the same maze, only ghost count changes
        board = createClassicMaze();
    }

    private int[][] createClassicMaze() {
        // Classic Pac-Man style maze - 28x31 (standard size)
        int width = 28;
        int height = 31;
        int[][] maze = new int[width][height];

        // Initialize all as walls
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                maze[x][y] = blueColor;
            }
        }

        // Create the classic maze pattern
        // Outer corridor
        fillRect(maze, 1, 1, 26, 29, pinkColor);

        // Top section walls
        fillRect(maze, 2, 2, 5, 4, blueColor);
        fillRect(maze, 8, 2, 11, 4, blueColor);
        fillRect(maze, 16, 2, 19, 4, blueColor);
        fillRect(maze, 21, 2, 25, 4, blueColor);

        // Second row blocks
        fillRect(maze, 2, 6, 5, 7, blueColor);
        fillRect(maze, 8, 6, 11, 10, blueColor);
        fillRect(maze, 16, 6, 19, 10, blueColor);
        fillRect(maze, 21, 6, 25, 7, blueColor);

        // Third row
        fillRect(maze, 2, 9, 5, 10, blueColor);
        fillRect(maze, 21, 9, 25, 10, blueColor);

        // Ghost house area (center)
        fillRect(maze, 10, 12, 17, 16, blackColor); // Ghost house
        fillRect(maze, 9, 13, 9, 15, blueColor); // Left wall
        fillRect(maze, 18, 13, 18, 15, blueColor); // Right wall
        fillRect(maze, 10, 12, 17, 12, blueColor); // Top wall

        // Middle section walls
        fillRect(maze, 2, 12, 7, 13, blueColor);
        fillRect(maze, 20, 12, 25, 13, blueColor);

        fillRect(maze, 2, 15, 7, 19, blueColor);
        fillRect(maze, 20, 15, 25, 19, blueColor);

        // Bottom section
        fillRect(maze, 2, 21, 5, 22, blueColor);
        fillRect(maze, 8, 18, 11, 22, blueColor);
        fillRect(maze, 16, 18, 19, 22, blueColor);
        fillRect(maze, 21, 21, 25, 22, blueColor);

        // Bottom blocks
        fillRect(maze, 2, 24, 5, 25, blueColor);
        fillRect(maze, 8, 24, 19, 25, blueColor);
        fillRect(maze, 21, 24, 25, 25, blueColor);

        // Final bottom section
        fillRect(maze, 8, 27, 11, 28, blueColor);
        fillRect(maze, 16, 27, 19, 28, blueColor);

        // Cyclic walls
        fillRect(maze,0,13,0,15,blackColor);
        fillRect(maze,27,13,27,15,blackColor);

        fillRect(maze,0,1,0,3,blackColor);
        fillRect(maze,0,27,0,29,blackColor);
        fillRect(maze,27,1,27,3,blackColor);
        fillRect(maze,27,27,27,29,blackColor);

        // Add power pellets in corners
        maze[1][1] = greenColor;
        maze[26][1] = greenColor;
        maze[1][29] = greenColor;
        maze[26][29] = greenColor;

        // Add extra power pellets
        maze[1][15] = greenColor;
        maze[26][15] = greenColor;

        return maze;
    }

    // Helper method to fill a rectangle
    private void fillRect(int[][] maze, int x1, int y1, int x2, int y2, int color) {
        for (int x = x1; x <= x2 && x < maze.length; x++) {
            for (int y = y1; y <= y2 && y < maze[0].length; y++) {
                maze[x][y] = color;
            }
        }
    }

    private Index2D findStartPosition() {
        // Pac-Man starts at bottom center of maze
        int startX = board.length / 2;
        int startY = board[0].length - 5;

        // Make sure it's not a wall
        if (board[startX][startY] == blueColor) {
            // Find nearest non-wall position
            for (int y = 0; y < board[0].length; y++) {
                if (board[startX][y] != blueColor) {
                    return new Index2D(startX, y);
                }
            }
        }

        // Clear the starting position
        board[startX][startY] = blackColor;
        return new Index2D(startX, startY);
    }

    private Index2D findGhostStartPos(int ghostIndex) {
        // Ghosts start in the center "ghost house"
        int centerX = board.length / 2;
        int centerY = board[0].length / 2;

        // Offset positions for multiple ghosts
        int[][] offsets = {
                {0, 0},   // Center
                {-1, 0},  // Left
                {1, 0},   // Right
                {0, -1},  // Down
                {0, 1}    // Up
        };

        if (ghostIndex < offsets.length) {
            int x = centerX + offsets[ghostIndex][0];
            int y = centerY + offsets[ghostIndex][1];

            // Make sure it's not a wall
            if (x >= 0 && x < board.length && y >= 0 && y < board[0].length) {
                if (board[x][y] != blueColor) {
                    return new Index2D(x, y);
                }
            }
        }

        // Default to center
        return new Index2D(centerX, centerY);
    }

    @Override
    public int[][] getGame(int gameNum) {
        return board;
    }

    @Override
    public String getPos(int gameNum) {
        return pacmanPos.toString();
    }

    @Override
    public GhostCL[] getGhosts(int gameNum) {
        return ghosts;
    }

    @Override
    public String move(int direction) {
        if (status != PLAY) {
            return "Game not playing";
        }

        // Calculate new position
        Index2D newPos = calculateNewPosition(pacmanPos, direction);

        // Check if valid move
        if (isValidMove(newPos)) {
            pacmanPos = newPos;

            // Check what's at new position
            int cellColor = board[newPos.getX()][newPos.getY()];

            if (cellColor == pinkColor) {
                score += 10;
                board[newPos.getX()][newPos.getY()] = blackColor;
            } else if (cellColor == greenColor) {
                score += 50;
                activatePowerMode();
                board[newPos.getX()][newPos.getY()] = blackColor;
            }

            // Update ghosts
            updateGhosts();

            // Check collisions
            checkCollisions();

            // Check game end
            checkGameEnd();

            // Render
            if (renderer != null && status == PLAY) {
                renderer.draw(this);
            }
        }

        return "Score: " + score + ", Lives: " + lives;
    }

    private Index2D calculateNewPosition(Index2D current, int direction) {
        int newX = current.getX();
        int newY = current.getY();

        switch(direction) {
            case UP: newY++; break;      // Flipped: UP increases Y
            case DOWN: newY--; break;    // Flipped: DOWN decreases Y
            case LEFT: newX--; break;
            case RIGHT: newX++; break;
            case STAY: break;
        }

        // Handle cyclic board
        if (cyclic) {
            if (newX < 0) newX = board.length - 1;
            if (newX >= board.length) newX = 0;
            if (newY < 0) newY = board[0].length - 1;
            if (newY >= board[0].length) newY = 0;
        }

        return new Index2D(newX, newY);
    }

    private boolean isValidMove(Index2D pos) {
        // Check bounds
        if (!cyclic) {
            if (pos.getX() < 0 || pos.getX() >= board.length ||
                    pos.getY() < 0 || pos.getY() >= board[0].length) {
                return false;
            }
        }

        // Check if it's a wall
        return board[pos.getX()][pos.getY()] != blueColor;
    }

    private void activatePowerMode() {
        long currentTime = System.currentTimeMillis();
        for (MyGhost ghost : ghosts) {
            ghost.setEatable(currentTime, eatableTime);
        }
    }

    private void updateGhosts() {
        for (MyGhost ghost : ghosts) {
            ghost.update(pacmanPos, board, cyclic, blueColor);
        }
    }

    private void checkCollisions() {
        for (MyGhost ghost : ghosts) {
            if (ghost.getPosition().equals(pacmanPos)) {
                if (ghost.isEatable()) {
                    // Eat the ghost
                    score += 200;
                    ghost.respawn(findGhostStartPos(0));
                } else {
                    // Lose a life
                    lives--;
                    if (lives <= 0) {
                        status = DONE;
                    } else {
                        // Reset positions
                        pacmanPos = findStartPosition();
                        resetGhosts();
                    }
                }
            }
        }
    }

    private void resetGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].respawn(findGhostStartPos(i));
        }
    }

    private void checkGameEnd() {
        // Check if all dots eaten
        boolean hasDotsLeft = false;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == pinkColor || board[x][y] == greenColor) {
                    hasDotsLeft = true;
                    break;
                }
            }
            if (hasDotsLeft) break;
        }

        if (!hasDotsLeft) {
            status = DONE;
        }
    }

    @Override
    public void play() {
        if (status == PAUSE) {
            status = PLAY;
            System.out.println("Game started!");
        } else if (status == PLAY) {
            status = PAUSE;
            System.out.println("Game paused!");
        }
    }

    @Override
    public Character getKeyChar() {
        if (StdDraw.hasNextKeyTyped()) {
            lastKeyChar = StdDraw.nextKeyTyped();
            return lastKeyChar;
        }
        return null;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public boolean isCyclic() {
        return cyclic;
    }

    @Override
    public String getData(int gameNum) {
        return "Score: " + score + ", Lives: " + lives + ", Level: " + level;
    }

    @Override
    public String end(int gameNum) {
        status = DONE;
        String result = "Game Over! Score: " + score + ", Lives: " + lives + ", Level: " + level;
        // Print in red (as required by assignment)
        System.out.println("\u001B[31m" + result + "\u001B[0m");
        return result;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getBlueColor() {
        return blueColor;
    }

    public int getPinkColor() {
        return pinkColor;
    }

    public int getGreenColor() {
        return greenColor;
    }

    public int getBlackColor() {
        return blackColor;
    }
}