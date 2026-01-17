import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Index2DTest {

    @Test
    /**
     * Tests the getX method
     */
    public void getXTest(){
        Index2D ind1 = new Index2D(2,3);
        int x = ind1.getX();
        assertEquals(x,2);
    }

    @Test
    /**
     * Tests the getY method
     */
    public void getYTest(){
        Index2D ind1 = new Index2D(2,3);
        int y = ind1.getY();
        assertEquals(y,3);
    }

    @Test
    /**
     * Tests the distance2D method
     */
    public void testDistance2D() {
        Index2D ind1 = new Index2D(2,3);
        Index2D ind2 = new Index2D(5,7);
        double dis = ind1.distance2D(ind2);
        assertEquals(dis,5);
        String err ="";
        try {
            ind1.distance2D(null);
        } catch (RuntimeException e) {
            err = e.toString();
        }
        assertNotEquals(err,"");
    }


    @Test
    /**
     * Tests the equals method
     */
    public void testEquals(){
        Index2D ind1 = new Index2D(2,3);
        Index2D ind2 = new Index2D(2,3);
        Index2D ind3 = new Index2D(5,7);
        assertTrue(ind1.equals(ind2));
        assertFalse(ind1.equals(ind3));
    }
}
