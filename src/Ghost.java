
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacmanGame;

import java.io.Serializable;

public class Ghost implements GhostCL, Serializable {
    private Index2D _pos;
    private int _type;
    private int _status;

    public Ghost(int type, Index2D startPos) {
        this._type = type;
        this._pos = new Index2D(startPos);
        this._status = GhostCL.PLAY;
    }

    public Index2D getRealPos() { return _pos; }

    @Override
    public int getType() { return _type; }

    @Override
    public String getPos(int var1) { return _pos.toString(); }

    @Override
    public String getInfo() { return "Type:" + _type; }

    @Override
    public double remainTimeAsEatable(int var1) { return 0; }

    @Override
    public int getStatus() { return _status; }

    /**
     * Ghost movement logic called every game tick.
     */
    public void move(PacmanGame game, int dummy) {
        if (_type == GhostCL.RANDOM_WALK0 || _type == GhostCL.RANDOM_WALK1) {
            moveRandom(game);
        } else if (_type == GhostCL.GREEDY_SP) {
            moveGreedy(game);
        }
    }

    private void moveRandom(PacmanGame game) {
        int[] dirs = {PacmanGame.UP, PacmanGame.DOWN, PacmanGame.LEFT, PacmanGame.RIGHT};
        int dir = dirs[(int)(Math.random() * 4)];
        Index2D next = getNextPos(dir);
        // Only move if not a wall (assuming 1 is wall)
        if (game.getGame(0)[next.getX()][next.getY()] != 1) {
            _pos = next;
        }
    }

    private void moveGreedy(PacmanGame game) {
        // Simple Greedy: find which adjacent tile is physically closer to Pacman
        String[] pPosStr = game.getPos(0).split(","); // Assuming "x,y" format
        int px = Integer.parseInt(pPosStr[0]);
        int py = Integer.parseInt(pPosStr[1]);

        Index2D bestPos = _pos;
        double minDist = Double.MAX_VALUE;

        int[] dirs = {PacmanGame.UP, PacmanGame.DOWN, PacmanGame.LEFT, PacmanGame.RIGHT};
        for (int dir : dirs) {
            Index2D next = getNextPos(dir);
            if (game.getGame(0)[next.getX()][next.getX()] != 1) {
                double dist = Math.sqrt(Math.pow(next.getX() - px, 2) + Math.pow(next.getY() - py, 2));
                if (dist < minDist) {
                    minDist = dist;
                    bestPos = next;
                }
            }
        }
        _pos = bestPos;
    }

    private Index2D getNextPos(int dir) {
        int x = _pos.getX(), y = _pos.getY();
        if (dir == PacmanGame.UP) y++;
        else if (dir == PacmanGame.DOWN) y--;
        else if (dir == PacmanGame.LEFT) x--;
        else if (dir == PacmanGame.RIGHT) x++;
        return new Index2D(x, y);
    }
}