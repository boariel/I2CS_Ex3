package server;

public class Index2D implements Pixel2D {
    private int width, height;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {
        width =x;
        height =y;}
    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}
    @Override
    public int getX() {
        return width;
    }
    @Override
    public int getY() {
        return height;
    }
    public double distance2D(Pixel2D p2) throws RuntimeException{
        if (p2==null)
            throw new RuntimeException("Cannot compare to null");
        int dx = p2.getX()-this.width;
        int dy = p2.getY()-this.height;
        double dis = Math.sqrt(dx*dx+dy*dy);
        return dis;
    }
    @Override
    public String toString() {
        return getX()+","+getY();
    }
    @Override
    public boolean equals(Object t) {
        boolean ans = false;
       /////// you do NOT need to add your code below ///////
        if(t instanceof Pixel2D) {
            Pixel2D p = (Pixel2D) t;
            ans = (this.distance2D(p)==0);
        }
       ///////////////////////////////////
        return ans;
    }
}
