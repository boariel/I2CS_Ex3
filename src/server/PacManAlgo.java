package server;
/**
 * Interface for Pac-Man control algorithms.
 */
public interface PacManAlgo {
    String getInfo();
    int move(PacmanGame game);
}