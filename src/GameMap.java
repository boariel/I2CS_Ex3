
import java.io.Serializable;

public class GameMap implements Serializable {
    private int[][] _map;
    private boolean _isCyclic;

    public GameMap(int width, int height, int defaultColor) {
        this._map = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                _map[x][y] = defaultColor;
            }
        }
    }

    public int getWidth(int dummy) { return _map.length; }
    public int getHeight(int dummy) { return _map[0].length; }

    public int getPixel(int x, int y, int dummy) {
        if (x >= 0 && x < _map.length && y >= 0 && y < _map[0].length) {
            return _map[x][y];
        }
        return -1; // Represents a wall/out of bounds
    }

    public void setPixel(int x, int y, int color, int dummy) {
        if (x >= 0 && x < _map.length && y >= 0 && y < _map[0].length) {
            _map[x][y] = color;
        }
    }

    public boolean isCyclic(int dummy) { return _isCyclic; }
    public void setCyclic(boolean cyclic, int dummy) { this._isCyclic = cyclic; }

    public int[][] getMap(int dummy) { return _map; }
}