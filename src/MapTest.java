import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
class MapTest {

    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private Map2D _m0, _m1, _m3_3;
    @BeforeEach
    public void setuo() {
        _m3_3 = new Map(_map_3_3);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int [500][500];
        _m1 = new Map(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3,2);
        _m1.fill(p1,1);
    }

    @Test
    void testInit() {
        _m0=new Map(_map_3_3);
        _m1=new Map(_map_3_3);
        assertEquals(_m0, _m1);
    }
    @Test
    void testEquals() {
        _m0=new Map(_map_3_3);
        _m1=new Map(_map_3_3);
        assertEquals(_m0,_m1);
    }
    @Test
    void testGetMap(){
        _m0 = new Map(_map_3_3);
        int[][] ac = _m0.getMap();
        assertArrayEquals(_map_3_3,ac);
    }
    @Test
    void testWidth(){
        _m0 = new Map(_map_3_3);
        assertEquals(3,_m0.getWidth());
    }
    @Test
    void testHeight(){
        _m0 = new Map(_map_3_3);
        assertEquals(3,_m0.getHeight());
    }
    @Test
    void testGetPixel(){
        _m0 = new Map(_map_3_3);
        int ac = _m0.getPixel(0,0);
        Index2D p1 = new Index2D(0,0);
        assertEquals(0,ac);
        ac = _m0.getPixel(p1);
        assertEquals(0,ac);
    }
    @Test
    void testSetPixel(){
        _m0 = new Map(_map_3_3);
        _m0.setPixel(0,0,4);
        Index2D p1 = new Index2D(0,0);
        int ac = _m0.getPixel(0,0);
        assertEquals(4,ac);
        _m0.setPixel(p1,3);
        ac = _m0.getPixel(p1);
        assertEquals(3,ac);
    }
    @Test
    void testIsInside(){
        _m0 = new Map(_map_3_3);
        Index2D p1 = new Index2D(2,4);
        Index2D p2 = new Index2D(4,2);
        Index2D p3 = new Index2D(2,2);
        assertFalse(_m0.isInside(p1));
        assertFalse(_m0.isInside(p2));
        assertTrue(_m0.isInside(p3));
    }
    @Test
    void testFill() {
        Map map = new Map(3, 3, 0);
        map.setPixel(0, 2, 1);
        map.setPixel(2, 0, 1);
        int count = map.fill(new Index2D(0, 0), 2);
        assertEquals(7, count);
        assertEquals(2, map.getPixel(0, 0));
        assertEquals(2, map.getPixel(1, 0));
        assertEquals(2, map.getPixel(1, 1));
    }
    @Test
    void testShortestPathCyclic() {
        int[][] data = {
                {0, 1, 0},
                {1, 0, 1},
                {0, 1, 0}
        };
        Map map = new Map(data);
        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(2, 0);
        int obsColor = 1;
        Pixel2D[] path = map.shortestPath(start, end, obsColor);
        assertNotNull(path);
        assertEquals(start, path[0]);
        assertEquals(end, path[path.length - 1]);
        for (Pixel2D p : path) {
            assertNotEquals(obsColor, map.getPixel(p));
        }
    }
    @Test
    void testAllDistance() {
        int[][] data = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        Map map = new Map(data);
        Pixel2D start = new Index2D(0, 0);
        int obsColor = 1;
        Map2D distMap = map.allDistance(start, obsColor);
        int[][] expected = {
                {0, 1, 1},
                {1, -1, 2},
                {1, 2, 2}
        };
        for (int y = 0; y < expected.length; y++) {
            for (int x = 0; x < expected[0].length; x++) {
                assertEquals(expected[y][x], distMap.getPixel(x, y),
                        "Distance mismatch at (" + x + "," + y + ")");
            }
        }
    }
}