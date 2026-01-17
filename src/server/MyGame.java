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
        int numGhosts = 3 + level;
        ghosts = new MyGhost[numGhosts];
        for (int i = 0; i < numGhosts; i++) {
            Index2D ghostStart = findGhostStartPos(i);
            ghosts[i] = new MyGhost(GhostCL.GREEDY_SP, ghostStart, seed + i);
        }

        // Initialize renderer
        renderer = new GameRenderer();

        this.status = PAUSE; // Start paused

        return "Game Level: " + level + ", ID: " + id + ", Cyclic: " + isCyclic +
                ", Seed: " + seed + ", DT: " + dt + ", Ghosts: " + numGhosts;
    }

    private void loadLevel(int level) {
        // Create different boards for different levels
        switch(level) {
            case 0:
                board = createLevel0();
                break;
            case 1:
                board = createLevel1();
                break;
            case 2:
                board = createLevel2();
                break;
            case 3:
                board = createLevel3();
                break;
            case 4:
                board = createLevel4();
                break;
            default:
                board = createLevel0();
        }
    }

    private int[][] createLevel0() {
        // Simple 10x10 level
        int[][] level = new int[10][10];

        // Fill with walls on borders
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (x == 0 || x == 9 || y == 0 || y == 9) {
                    level[x][y] = blueColor; // walls
                } else {
                    level[x][y] = pinkColor; // dots
                }
            }
        }

        // Add some internal walls
        level[2][2] = blueColor;
        level[2][3] = blueColor;
        level[7][2] = blueColor;
        level[7][3] = blueColor;
        level[2][7] = blueColor;
        level[2][6] = blueColor;
        level[7][7] = blueColor;
        level[7][6] = blueColor;

        // Add power pellets in corners
        level[1][1] = greenColor;
        level[1][8] = greenColor;
        level[8][1] = greenColor;
        level[8][8] = greenColor;

        // Clear starting positions
        level[5][5] = blackColor; // Pac-Man start
        level[1][1] = greenColor;

        return level;
    }

    private int[][] createLevel1() {
        // Medium 15x15 level
        int[][] level = new int[15][15];

        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (x == 0 || x == 14 || y == 0 || y == 14) {
                    level[x][y] = blueColor;
                } else {
                    level[x][y] = pinkColor;
                }
            }
        }

        // Create a cross pattern of walls
        for (int i = 4; i < 11; i++) {
            level[7][i] = blueColor;
            level[i][7] = blueColor;
        }

        // Power pellets
        level[2][2] = greenColor;
        level[2][12] = greenColor;
        level[12][2] = greenColor;
        level[12][12] = greenColor;

        // Clear center
        level[7][7] = blackColor;

        return level;
    }

    private int[][] createLevel2() {
        // Larger 20x20 level
        int[][] level = new int[20][20];

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if (x == 0 || x == 19 || y == 0 || y == 19) {
                    level[x][y] = blueColor;
                } else {
                    level[x][y] = pinkColor;
                }
            }
        }

        // Create maze-like walls
        for (int i = 2; i < 18; i += 3) {
            for (int j = 2; j < 10; j++) {
                level[i][j] = blueColor;
            }
        }

        // Power pellets
        level[3][3] = greenColor;
        level[3][16] = greenColor;
        level[16][3] = greenColor;
        level[16][16] = greenColor;

        level[10][10] = blackColor;

        return level;
    }

    private int[][] createLevel3() {
        // Complex 25x25 level
        int[][] level = new int[25][25];

        for (int x = 0; x < 25; x++) {
            for (int y = 0; y < 25; y++) {
                if (x == 0 || x == 24 || y == 0 || y == 24) {
                    level[x][y] = blueColor;
                } else {
                    level[x][y] = pinkColor;
                }
            }
        }

        // Create complex maze
        for (int i = 5; i < 20; i += 5) {
            for (int j = 5; j < 20; j++) {
                if (j % 2 == 0) {
                    level[i][j] = blueColor;
                }
            }
        }

        // Power pellets in corners
        level[2][2] = greenColor;
        level[2][22] = greenColor;
        level[22][2] = greenColor;
        level[22][22] = greenColor;
        level[12][12] = greenColor; // Extra in center

        level[12][12] = blackColor;

        return level;
    }

    private int[][] createLevel4() {
        // Expert 30x30 level
        int[][] level = new int[30][30];

        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 30; y++) {
                if (x == 0 || x == 29 || y == 0 || y == 29) {
                    level[x][y] = blueColor;
                } else {
                    level[x][y] = pinkColor;
                }
            }
        }

        // Create dense maze
        for (int i = 3; i < 27; i += 4) {
            for (int j = 3; j < 27; j++) {
                if ((i + j) % 3 != 0) {
                    level[i][j] = blueColor;
                }
            }
        }

        // Many power pellets for difficult level
        level[2][2] = greenColor;
        level[2][27] = greenColor;
        level[27][2] = greenColor;
        level[27][27] = greenColor;
        level[15][2] = greenColor;
        level[15][27] = greenColor;
        level[2][15] = greenColor;
        level[27][15] = greenColor;

        level[15][15] = blackColor;

        return level;
    }

    private Index2D findStartPosition() {
        // Find black (empty) cell or center of board
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == blackColor) {
                    return new Index2D(x, y);
                }
            }
        }
        // Default to center
        return new Index2D(board.length / 2, board[0].length / 2);
    }

    private Index2D findGhostStartPos(int ghostIndex) {
        // Place ghosts in different corners
        int w = board.length;
        int h = board[0].length;

        Index2D[] positions = {
                new Index2D(1, 1),
                new Index2D(w - 2, 1),
                new Index2D(1, h - 2),
                new Index2D(w - 2, h - 2),
                new Index2D(w / 2, 1),
                new Index2D(w / 2, h - 2),
                new Index2D(1, h / 2),
                new Index2D(w - 2, h / 2)
        };

        if (ghostIndex < positions.length) {
            return positions[ghostIndex];
        }

        // Random position for extra ghosts
        return new Index2D((ghostIndex % (w - 2)) + 1, (ghostIndex / (w - 2)) + 1);
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
            case UP: newY--; break;
            case DOWN: newY++; break;
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