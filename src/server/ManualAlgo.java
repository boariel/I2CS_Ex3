package server;

public class ManualAlgo implements PacManAlgo{

    @Override
    public String getInfo() {
        return "Manual control: w=up, a=left, s=down, d=right";
    }

    @Override
    public int move(PacmanGame game) {
        Character cmd = ServerMain.getCMD();

        if (cmd != null) {
            if (cmd == 'w' || cmd == 'W') return Game.UP;
            if (cmd == 's' || cmd == 'S') return Game.DOWN;
            if (cmd == 'a' || cmd == 'A') return Game.LEFT;
            if (cmd == 'd' || cmd == 'D') return Game.RIGHT;
        }

        return Game.STAY;
    }
}
