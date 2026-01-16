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
	public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
        int dir = Game.UP;
		//if(_count==0 || _count==300) {
			int code = 0;
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

            //to Map object
            Map map = new Map(board);
            //getting pac and ghost pos as Index2D
            String[] cord = pos.split(",");
            int pacposx = Integer.parseInt(cord[0].trim());
            int pacposy = Integer.parseInt(cord[1].trim());
            Index2D pacin = new Index2D(pacposx,pacposy);
            Index2D[] gin = new Index2D[ghosts.length];
            for (int i = 0; i < ghosts.length; i++) {
                String gpos = ghosts[i].getPos(code);
                cord = pos.split(",");
                int gposx = Integer.parseInt(cord[0].trim());
                int gposy = Integer.parseInt(cord[1].trim());
                gin[i] = new Index2D(gposx,gposy);
            }
            //Mapping distances
            Map dist = (Map) map.allDistance(pacin,blue);
            int closestghostdist = Integer.MAX_VALUE;
            for (int i = 0; i < gin.length; i++) {
                closestghostdist = Math.min(dist.getPixel(gin[i]),closestghostdist);
            }

            if (closestghostdist<0){
                Map[] ghostdist = new Map[gin.length];
            }
            else {
                int closestpink = 0;
                Index2D pinktogo = new Index2D(0,0);
                for (int i = 0; i < map.getWidth(); i++) {
                    for (int j = 0; j < map.getHeight(); j++) {
                        if (map.getPixel(i,j)==pink){
                            if(closestpink>dist.getPixel(i,j)){
                                closestpink = dist.getPixel(i,j);
                                pinktogo = new Index2D(i,j);
                            }
                        }
                    }
                }
                Pixel2D[] pinkpath = map.shortestPath(pacin,pinktogo,black);
                dir = getMoveByPix(pacin,pinkpath[1]);
            }
		//}
		_count++;
		return dir;
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
    private static int getMoveByPix(Pixel2D pos,Pixel2D next){
        int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
        if (pos.getX()!= next.getX()){
            if (pos.getX()<next.getX())
                return right;
            else
                return left;
        }
        else{
            if (pos.getY()<next.getY())
                return up;
            else
                return down;
        }
    }
}