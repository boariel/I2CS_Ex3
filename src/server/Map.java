package server;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] map;
	private boolean _cyclicFlag = true;
	
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
    public Map(int w, int h, int v) {init(w, h, v);}

    /**
     * Constructs a square map (size*size).
     * @param size width and height of map
     */
    public Map(int size) {this(size,size, 0);}

    /**
     * Constructs a map from a given 2D array.
     * @param data copied array
     */
    public Map(int[][] data) {
        init(data);
    }

    @Override
    public void init(int w, int h, int v) {
        map = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                map[i][j] = v;
            }
        }
    }
    @Override
    public void init(int[][] arr) throws RuntimeException{
        if (arr==null)
            throw new RuntimeException("Cannot copy null array.");
        if (arr.length==0)
            throw new RuntimeException("The array cannot be empty.");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length!=arr[0].length)
                throw new RuntimeException("The cannot be ragged.");
        }
        int h = arr.length;
        int w = arr[0].length;
        map = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                map[i][j] = arr[i][j];
            }
        }
    }
    @Override
    public int[][] getMap() {
        int[][] ans = null;
        ans = new int[this.map.length][this.map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < this.map[0].length; j++) {
                ans[i][j] = this.map[i][j];
            }
        }
        return ans;
    }
    @Override
    public int getWidth() {
        int ans = -1;
        ans = this.map[0].length;
        return ans;
    }
    @Override
    public int getHeight() {
        int ans = -1;
        ans = this.map.length;
        return ans;
    }

    /**
     * This function returns the color of a pixel in the coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the value of the entry at the coordinate [x][y]
     */
    @Override
    public int getPixel(int x, int y) {
        int ans = -1;
        ans = this.map[y][x];
        return ans;
    }
    /**
     * This function returns the color of a pixel in the coordinates.
     * @param p the coordinates in Pixel2D
     * @return the value of the entry at the coordinate [x][y]
     */
    @Override
    public int getPixel(Pixel2D p) {
        int ans = -1;
        int x = p.getX();
        int y = p.getY();
        ans = this.map[y][x];
        return ans;
    }

    /**
     * This function changes the color of a pixel in the coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param v the value that the entry at the coordinate [x][y] is set to.
     */
    @Override
    public void setPixel(int x, int y, int v) {
        this.map[y][x] = v;
    }

    /**
     * This function changes the color of a pixel in the coordinates.
     * @param p the coordinate in the map.
     * @param v the value that the entry at the coordinate [p.x][p.y] is set to.
     */
    @Override
    public void setPixel(Pixel2D p, int v) {
        int x = p.getX();
        int y = p.getY();
        this.map[y][x] = v;
    }
    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof Map2D) {
                Map2D m=(Map2D) o;
                if (this.getWidth()==m.getWidth()&&this.getHeight()==m.getHeight()){
                    for (int x = 0; x < this.getWidth(); x++) {
                        for (int y = 0; y < this.getHeight(); y++) {
                            if (this.getPixel(x, y) != ((Map2D)o).getPixel(x, y))
                                return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * This function fill a new color in a group of pixels of the same color
     * starting from a given pixel xy.
     * The function does this by using flood fills presented in:
     * https://en.wikipedia.org/wiki/Flood_fill
     * @param xy the pixel to start from.
     * @param new_v - the new "color" to be filled in p's connected component.
     * @return amount of pixels recolored
     */
    @Override
    public int fill(Pixel2D xy, int new_v) {
        boolean cyclic = this._cyclicFlag;
        int oldColor = getPixel(xy);
        if (oldColor == new_v) return 0;
        int count = 0;
        boolean[][] visited = new boolean[getHeight()][getWidth()];
        ArrayDeque<Pixel2D> q = new ArrayDeque<>();
        q.add(xy);
        visited[xy.getY()][xy.getX()] = true;
        setPixel(xy, new_v);
        count++;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        while (!q.isEmpty()) {
            Pixel2D p = q.poll();
            int x = p.getX();
            int y = p.getY();
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (cyclic) {
                    nx = (nx + getWidth()) % getWidth();
                    ny = (ny + getHeight()) % getHeight();
                }
                if (nx < 0 || ny < 0 || nx >= getWidth() || ny >= getHeight())
                    continue;
                if (!visited[ny][nx] && getPixel(nx, ny) == oldColor) {
                    visited[ny][nx] = true;
                    setPixel(nx, ny, new_v);
                    q.add(new Index2D(nx, ny));
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * This function returns the shortest path between two coordinates.
     * The function finds the shortest path by using BDF algorithm as presented in:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     * @param p1 first coordinate (start point).
     * @param p2 second coordinate (end point).
     * @param obsColor the color which is addressed as an obstacle.
     * @return the shortest path as array of pixels
     */
    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        boolean cyclic = this._cyclicFlag;
        if (p1.equals(p2)) return new Pixel2D[]{p1};
        boolean[][] visited = new boolean[getHeight()][getWidth()];
        Pixel2D[][] parent = new Pixel2D[getHeight()][getWidth()];
        ArrayDeque<Pixel2D> q = new ArrayDeque<>();
        visited[p1.getY()][p1.getX()] = true;
        q.add(p1);
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        while (!q.isEmpty()) {
            Pixel2D v = q.poll();
            int x = v.getX();
            int y = v.getY();
            if (v.equals(p2))
                break;
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (cyclic) {
                    nx = (nx + getWidth()) % getWidth();
                    ny = (ny + getHeight()) % getHeight();
                }
                if (nx < 0 || nx >= getWidth() || ny < 0 || ny >= getHeight())
                    continue;
                if (visited[ny][nx])
                    continue;
                if (getPixel(nx, ny) == obsColor)
                    continue;
                visited[ny][nx] = true;
                parent[ny][nx] = v;
                q.add(new Index2D(nx, ny));
            }
        }
        if (!visited[p2.getY()][p2.getX()]) return null;
        LinkedList<Pixel2D> path = new LinkedList<>();
        Pixel2D cur = p2;
        while (!cur.equals(p1)) {
            path.addFirst(cur);
            cur = parent[cur.getY()][cur.getX()];
        }
        path.addFirst(p1);
        return path.toArray(new Pixel2D[0]);
    }
    /**
     * This function returns true if the coordinates are in it and false otherwise.
     * @param p the 2D coordinate.
     * @return is p in the map
     */
    @Override
    public boolean isInside(Pixel2D p) {
        boolean ans = true;
        int x = p.getX();
        int y = p.getY();
        if (x>=this.getWidth())
            ans = false;
        if (y>=this.getHeight())
            ans = false;
        return ans;
    }

	@Override
	/////// add your code below ///////
	public boolean isCyclic() {
		return this._cyclicFlag;
	}
	@Override
	/////// add your code below ///////
	public void setCyclic(boolean cy) {this._cyclicFlag = cy;}
    /**
     * This function return a map that represents the length the shortest path from every
     * index to a starting coordinate.
     * @param start the source (starting) point
     * @param obsColor the color representing obstacles
     * @return the map with the shortest path length as values
     */
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        Map2D ans = new Map(this.getWidth(), this.getHeight(), -1);

        boolean cyclic = this._cyclicFlag;

        int[][] dist = new int[getHeight()][getWidth()];
        for (int y = 0; y < getHeight(); y++) {
            Arrays.fill(dist[y], -1);
        }

        ArrayDeque<Pixel2D> q = new ArrayDeque<>();

        // If start is an obstacle, return empty map
        if (getPixel(start.getX(), start.getY()) == obsColor) {
            return ans;
        }

        dist[start.getY()][start.getX()] = 0;
        q.add(start);

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        while (!q.isEmpty()) {
            Pixel2D v = q.poll();
            int x = v.getX();
            int y = v.getY();

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (cyclic) {
                    nx = (nx + getWidth()) % getWidth();
                    ny = (ny + getHeight()) % getHeight();
                }

                if (nx < 0 || nx >= getWidth() || ny < 0 || ny >= getHeight())
                    continue;

                if (getPixel(nx, ny) == obsColor)
                    continue;

                if (dist[ny][nx] != -1)
                    continue;

                dist[ny][nx] = dist[y][x] + 1;
                q.add(new Index2D(nx, ny));
            }
        }

        // Copy distances into Map2D
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                ans.setPixel(x, y, dist[y][x]);
            }
        }

        return ans;
    }
}
