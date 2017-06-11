package dij.nearestpath.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;

import static dij.nearestpath.data.StaticThing.BH;
import static dij.nearestpath.data.StaticThing.BW;
import static dij.nearestpath.data.StaticThing.BgColor;

/**
 * Created by E on 2017/5/24.
 */
public class Banner {
    private static final int xPoints[] = {0, BW, 0};
    private static final int yPoints[] = {0, 0, BW};
    private static final int nPoints = yPoints.length;
    public static BufferedImage GreenBanner = draw(Color.ORANGE);
    public static BufferedImage RedBanner = draw(Color.RED);
    public static BufferedImage BlackBanner = draw(Color.BLACK);
    public static BufferedImage SmallBlackBanner;
    public static BufferedImage draw(Color color) {
        BufferedImage banner = new BufferedImage(BW, BH, BufferedImage.TYPE_INT_RGB);
        Graphics2D d = banner.createGraphics();
        d.setColor(BgColor);
        d.fillRect(0, 0, BW, BH);
        d.setColor(color);
        d.fillPolygon(xPoints, yPoints, nPoints);
        d.fillRect(0, 0, 3, BH);
        return banner;
    }

    public static final int ORANGE = 0;
    public static final int RED = ORANGE + 1;
    public static final int BLACK = RED + 1;

    public static void drawBanner(Graphics2D d, int color, int x, int y) {
        BufferedImage img;
        switch (color) {
            case ORANGE:
                img = GreenBanner;
                break;
            case RED:
                img = RedBanner;
                break;
            case BLACK:
                img = BlackBanner;
                break;
            default:
                throw new NoSuchElementException(String.valueOf(color));
        }
        d.drawImage(img, x, y - BH, null);
    }

    public static void drawSmallBlackBanner(Graphics2D d, int x, int y) {
        if (SmallBlackBanner == null) {
            int w = BW >>> 1;
            int h = BH >>> 1;
            SmallBlackBanner = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D d2 = SmallBlackBanner.createGraphics();
            d2.setColor(BgColor);
            d2.fillRect(0, 0, w, h);
            d2.setColor(Color.BLACK);
            d2.fillPolygon(new int[]{0, w, 0}, new int[]{0, 0, w}, nPoints);
            d2.fillRect(0, 0, 3, h);
        }
        d.drawImage(SmallBlackBanner, x, y - SmallBlackBanner.getHeight(), null);
    }
}
