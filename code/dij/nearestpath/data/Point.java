package dij.nearestpath.data;

/**
 * Created by E on 2017/5/23.
 */
public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static double distance(Point a, Point b) {
        return distance(a.x, a.y, b.x, b.y);
    }
    public static double distance(double x1, double y1,
                                  double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }
}
