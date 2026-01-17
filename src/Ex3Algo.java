import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;
    private Index2D last;
	public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return "This is an algorithm for beating the game pacman." +
                "If pacman is close to a ghost and has enough power-up time he will move towards the closest ghost." +
                "If he is close to a ghost he will go towards the places furthest away from as many ghosts as possible." +
                "If there are no ghosts nearby pacman will move towards the closest pink orb.";
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
        int dir = Game.ERR;
        int code = 0;
        boolean cyclic = GameInfo.CYCLIC_MODE;
        int[][] board = game.getGame(0);
        printBoard(board);
        int blue = Game.getIntColor(Color.BLUE, code);
        int pink = Game.getIntColor(Color.PINK, code);
        int black = Game.getIntColor(Color.BLACK, code);
        int green = Game.getIntColor(Color.GREEN, code);
        System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
        String pos = game.getPos(code).toString();
        System.out.println("Pacman coordinate: "+pos);
        GhostCL[] ghosts = game.getGhosts(code);
        printGhosts(ghosts);
        int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;

        Map map = convertBoardToMap(board);
        //printBoard(map);
        String[] cords = pos.split(",");
        Index2D ppos = new Index2D(Integer.parseInt(cords[0]),Integer.parseInt(cords[1]));
        Index2D[] ghostsp = new Index2D[ghosts.length];
        for (int i=0; i<ghostsp.length; i++){
            pos = ghosts[i].getPos(code);
            cords = pos.split(",");
            ghostsp[i] = new Index2D(Integer.parseInt(cords[0]),Integer.parseInt(cords[1]));
        }
        Map2D dist = map.allDistance(ppos,blue);
        //printBoard(dist);
        int ghostdis = Integer.MAX_VALUE;
        Index2D closestg = null;
        int closestIndex = -1;

        for (int i = 0; i < ghostsp.length; i++) {
            int d = dist.getPixel(ghostsp[i]);
            if (d >= 0 && d < ghostdis) {
                ghostdis = d;
                closestg = new Index2D(ghostsp[i]);
                closestIndex = i;
            }
        }
        _count++;
        if (ghostdis<=3&&ghosts[closestIndex].remainTimeAsEatable(code)>1)
            return eatGhost(map,ppos,closestg,blue,cyclic);
        else if (ghostdis<3)
            return escapeGhost(map,ppos,ghostsp,blue,cyclic);
        else
            return food(map,dist,ppos,blue, pink,cyclic);

	}

    private int food(Map map, Map2D dist, Index2D ppos, int blue, int pink,boolean cyclic) {
        int mindist = Integer.MAX_VALUE;
        Index2D move = new Index2D(ppos);
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int d = dist.getPixel(x,y);
                if (map.getPixel(x,y) == pink && d > 0 && d < mindist) {
                    mindist = dist.getPixel(x,y);
                    move = new Index2D(x,y);
                }
            }
        }
        System.out.println("food");
        Pixel2D[] path = map.shortestPath(ppos, move, blue);
        return dirByPix(ppos, path[1],map,cyclic);
    }

    private int escapeGhost(Map map, Index2D pac, Index2D[] ghosts, int wall, boolean cyclic) {
        // compute distance maps from each ghost
        Map2D[] ghostDist = new Map2D[ghosts.length];
        for (int i = 0; i < ghosts.length; i++) {
            ghostDist[i] = map.allDistance(ghosts[i], wall);
        }

        // look at neighbors of Pacman
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        int bestScore = Integer.MIN_VALUE;
        Index2D bestNeighbor = pac;

        for (int i = 0; i < 4; i++) {
            int nx = pac.getX() + dx[i];
            int ny = pac.getY() + dy[i];

            // handle cyclic maps
            if (cyclic) {
                nx = (nx + map.getWidth()) % map.getWidth();
                ny = (ny + map.getHeight()) % map.getHeight();
            }

            if (nx < 0 || nx >= map.getWidth() || ny < 0 || ny >= map.getHeight())
                continue;
            if (map.getPixel(nx, ny) == wall)
                continue;

            // compute distance to closest ghost
            int minDist = Integer.MAX_VALUE;
            for (Map2D gd : ghostDist) {
                int d = gd.getPixel(nx, ny);
                if (d >= 0) minDist = Math.min(minDist, d);
            }

            if (minDist > bestScore) {
                bestScore = minDist;
                bestNeighbor = new Index2D(nx, ny);
            }
        }
        System.out.println("escape");
        return dirByPix(pac, bestNeighbor, map, cyclic);
    }





    private int eatGhost(Map map, Index2D ppos, Index2D closestg, int blue, boolean cyclic) {
        Pixel2D[] path = map.shortestPath(ppos,closestg,blue);
        System.out.println("eat");
        return dirByPix(ppos,path[1],map,cyclic);
    }

    private int dirByPix(Index2D from, Pixel2D to, Map map, boolean cyclic) {
        last = from;
        int width = map.getWidth();
        int height = map.getHeight();
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        if (cyclic) {
            if (dx >  width / 2) dx -= width;
            if (dx < -width / 2) dx += width;
            if (dy >  height / 2) dy -= height;
            if (dy < -height / 2) dy += height;
        }
        if (dx == 1 && dy == 0) return Game.RIGHT;
        if (dx == -1 && dy == 0) return Game.LEFT;
        if (dx == 0 && dy == 1) return Game.UP;
        if (dx == 0 && dy == -1) return Game.DOWN;
        return Game.ERR;
    }


    private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
    private static void printBoard(Map2D b) {
        for(int y =0;y<b.getHeight();y++){
            for(int x =0;x<b.getWidth();x++){
                int v = b.getPixel(x,y);
                System.out.print(v+"\t");
            }
            System.out.println();
        }
    }
    private static Map convertBoardToMap(int[][] b){
        Map ret = new Map(b.length,b[0].length,-1);
        for(int y =0;y<b[0].length;y++){
            for(int x =0;x<b.length;x++){
                ret.setPixel(x,y,b[x][y]);
            }
        }
        return ret;
    }
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}
}