package server;

import java.util.Random;

public class MyGhost implements GhostCL {
    private int type;
    private Index2D position;
    private int status;
    private double eatableUntil;
    private Random random;

    public MyGhost(int type, Index2D startPos, long seed) {
        this.type = type;
        this.position = new Index2D(startPos);
        this.status = PLAY;
        this.eatableUntil = 0;
        this.random = new Random(seed);
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getPos(int gameNum) {
        return position.toString();
    }

    public Index2D getPosition() {
        return position;
    }

    @Override
    public double remainTimeAsEatable(int gameNum) {
        double remaining = eatableUntil - System.currentTimeMillis();
        return Math.max(0, remaining / 1000.0);
    }

    public boolean isEatable() {
        return System.currentTimeMillis() < eatableUntil;
    }

    public void setEatable(long currentTime, double duration) {
        this.eatableUntil = currentTime + (duration * 1000);
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getInfo() {
        return "Ghost at " + position.toString() + ", type: " + type +
                ", eatable: " + isEatable();
    }

    public void update(Index2D pacmanPos, int[][] board, boolean cyclic, int wallColor) {
        if (type == RANDOM_WALK0) {
            randomWalk(board, cyclic, wallColor);
        } else if (type == RANDOM_WALK1) {
            smartRandomWalk(board, cyclic, wallColor, pacmanPos);
        } else if (type == GREEDY_SP) {
            if (isEatable()) {
                fleeFromPlayer(pacmanPos, board, cyclic, wallColor);
            } else {
                chasePlayer(pacmanPos, board, cyclic, wallColor);
            }
        }
    }

    private void randomWalk(int[][] board, boolean cyclic, int wallColor) {
        int[] directions = {PacmanGame.UP, PacmanGame.DOWN,
                PacmanGame.LEFT, PacmanGame.RIGHT};

        // Try random directions
        for (int attempt = 0; attempt < 10; attempt++) {
            int dir = directions[random.nextInt(directions.length)];
            Index2D newPos = calculateNewPosition(position, dir, board, cyclic);

            if (isValidGhostMove(newPos, board, cyclic, wallColor)) {
                position = newPos;
                return;
            }
        }
    }

    private void smartRandomWalk(int[][] board, boolean cyclic, int wallColor, Index2D pacmanPos) {
        int[] directions = {PacmanGame.UP, PacmanGame.DOWN,
                PacmanGame.LEFT, PacmanGame.RIGHT};

        // Prefer moves that get closer to Pac-Man with some randomness
        Index2D bestMove = position;
        double bestScore = Double.MAX_VALUE;

        for (int dir : directions) {
            Index2D newPos = calculateNewPosition(position, dir, board, cyclic);

            if (isValidGhostMove(newPos, board, cyclic, wallColor)) {
                double distance = newPos.distance2D(pacmanPos);
                double randomFactor = random.nextDouble() * 5; // Add randomness
                double score = distance + randomFactor;

                if (score < bestScore) {
                    bestScore = score;
                    bestMove = newPos;
                }
            }
        }

        position = bestMove;
    }

    private void chasePlayer(Index2D pacmanPos, int[][] board, boolean cyclic, int wallColor) {
        // Greedy: move to minimize distance to Pac-Man
        int[] directions = {PacmanGame.UP, PacmanGame.DOWN,
                PacmanGame.LEFT, PacmanGame.RIGHT};

        double minDistance = Double.MAX_VALUE;
        Index2D bestMove = position;

        for (int dir : directions) {
            Index2D newPos = calculateNewPosition(position, dir, board, cyclic);

            if (isValidGhostMove(newPos, board, cyclic, wallColor)) {
                double distance = newPos.distance2D(pacmanPos);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestMove = newPos;
                }
            }
        }

        position = bestMove;
    }

    private void fleeFromPlayer(Index2D pacmanPos, int[][] board, boolean cyclic, int wallColor) {
        // Move away from Pac-Man
        int[] directions = {PacmanGame.UP, PacmanGame.DOWN,
                PacmanGame.LEFT, PacmanGame.RIGHT};

        double maxDistance = -1;
        Index2D bestMove = position;

        for (int dir : directions) {
            Index2D newPos = calculateNewPosition(position, dir, board, cyclic);

            if (isValidGhostMove(newPos, board, cyclic, wallColor)) {
                double distance = newPos.distance2D(pacmanPos);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    bestMove = newPos;
                }
            }
        }

        position = bestMove;
    }

    private Index2D calculateNewPosition(Index2D current, int direction,
                                         int[][] board, boolean cyclic) {
        int newX = current.getX();
        int newY = current.getY();

        switch(direction) {
            case PacmanGame.UP: newY++; break;      // Flipped: UP increases Y
            case PacmanGame.DOWN: newY--; break;    // Flipped: DOWN decreases Y
            case PacmanGame.LEFT: newX--; break;
            case PacmanGame.RIGHT: newX++; break;
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

    private boolean isValidGhostMove(Index2D pos, int[][] board, boolean cyclic, int wallColor) {
        // Check bounds
        if (!cyclic) {
            if (pos.getX() < 0 || pos.getX() >= board.length ||
                    pos.getY() < 0 || pos.getY() >= board[0].length) {
                return false;
            }
        }

        // Ghosts can't move through walls
        return board[pos.getX()][pos.getY()] != wallColor;
    }

    public void respawn(Index2D newPos) {
        this.position = new Index2D(newPos);
        this.eatableUntil = 0;
    }
}