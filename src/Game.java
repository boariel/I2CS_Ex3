
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacmanGame;
import exe.ex3.game.StdDraw;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements PacmanGame, Serializable {
    private Index2D _pos;      // Pacman position
    private GameMap _map;        // Game grid
    private int _status;         // INIT, PLAY, DONE 
    private ArrayList<Ghost> _ghosts;
    private int _score;
    private int _steps;
    private int code;
    private Index2D _ghostSP;

    public Game() {
        this._ghosts = new ArrayList<>();
        this._status = PacmanGame.INIT;
    }

    @Override
    public String init(int level, String user, boolean cyclic, long seed, double scale, int dt, int dummy) {
        this._status = PacmanGame.INIT;
        this._map = new GameMap(22, 21, 0); // Dimensions from initMap 
        this._map.setCyclic(cyclic, 0);
        this._pos = new Index2D(11, 14);

        // Add ghosts as seen in decompiled source 
        _ghosts.add(new Ghost(GhostCL.RANDOM_WALK0, this._ghostSP));
        _ghosts.add(new Ghost(GhostCL.RANDOM_WALK1, this._ghostSP));
        _ghosts.add(new Ghost(GhostCL.GREEDY_SP, this._ghostSP));

        return "Initialized Level: " + level;
    }

    @Override
    public GhostCL[] getGhosts(int var1) {
        GhostCL[] g = new GhostCL[_ghosts.size()];
        return _ghosts.toArray(g);
    }

    @Override
    public int[][] getGame(int var1) {
        return _map.getMap(0);
    }

    @Override
    public void play() {
        this._status = PacmanGame.PLAY;
    }

    @Override
    public String end(int var1) {
        this._status = PacmanGame.DONE;
        return "Game Over. Score: " + _score;
    }

    @Override
    public String getData(int var1) {
        return "S: " + _score + ", St: " + _steps + ", P: " + _pos;
    }

    @Override
    public String getPos(int var1) {
        return _pos.toString();
    }

    @Override
    public int getStatus() {
        return _status;
    }

    @Override
    public boolean isCyclic() {
        return _map.isCyclic(0);
    }

    @Override
    public Character getKeyChar() {
        return StdDraw.getKeyChar(0);
    }

    /**
     * Checks if Pacman's current position overlaps with any ghost's position.
     */
    private boolean isKill(int dummy) {
        for (GhostCL gcl : getGhosts(0)) {
            // Casting to access our concrete implementation's position if needed
            // or parsing the string if getPos() returns "x,y"
            String[] gPos = gcl.getPos(0).split(",");
            int gx = Integer.parseInt(gPos[0]);
            int gy = Integer.parseInt(gPos[1]);

            if (_pos.getX() == gx && _pos.getY() == gy) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String move(int direction) {
        if (getStatus() != PacmanGame.PLAY) return getPos(0);

        // 1. Move Pacman (with wall and cyclic check as implemented before)
        // ... (Your previous move logic here)

        // 2. Move Ghosts
        for (Ghost g : _ghosts) {
            g.move(this, 0);
        }

        // 3. Collision Check
        if (isKill(0)) {
            this.end(0); // Sets status to DONE
        }

        return getPos(0);
    }
}