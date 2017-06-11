package dij.nearestpath.data;

import java.awt.*;
import java.util.Random;

/**
 * Created by E on 2017/5/23.
 */
public class StaticThing {
    public static final int W = 1200;
    public static final int H = 600;
    public static final int MARGIN = 40;
    public static final int PADDING = 60;
    public static final int SUM = MARGIN + PADDING;
    public static final Rectangle BORDER = new Rectangle(MARGIN, MARGIN, W - (MARGIN << 1), H - (MARGIN << 1));
    public static final Random random = new Random();

    public static final int BW = 16;
    public static final int BH = 30;
    public static final int B_W = 4;


    public static int randomPointW() {
        return random.nextInt(W - (SUM << 1)) + SUM;
    }
    public static int randomPointH() {
        return random.nextInt(H - (SUM << 1)) + SUM;
    }

    public static void drawBorder(Graphics2D d) {
        Color tmp = d.getColor();
        d.setColor(Color.BLUE);
        d.draw(BORDER);
        d.setColor(tmp);
    }

    public static final int INF = Integer.MAX_VALUE;
    public static final Color BgColor = new Color(238, 238, 238);
    public static final Color activeColor = new Color(117, 189, 247);
    public static final Color color = new Color(0, 0, 255, 48);
    public static final Color water = new Color(55, 168, 226);
    public static final Color HeapColor = Color.blue.darker();
    public static final Color UnexploredColor = Color.BLACK.darker();
    public static final Color ExploredColor = Color.LIGHT_GRAY;
    public static final Color SrcColor = Color.RED;
    public static final Color IdColor = Color.BLACK;
    public static final Color NearerColor = Color.BLUE;
    public static final Color OldColor = Color.PINK;
    public static final BasicStroke fatStroke = new BasicStroke(2.0f);
    public static final BasicStroke thinStroke = new BasicStroke(1.0f);
    public static final BasicStroke thinnerStroke = new BasicStroke(0.125f);
    public static final BasicStroke fatterStroke = new BasicStroke(4.0f);
    public static final BasicStroke fattestStroke = new BasicStroke(8.0f);
    public static final String[] CheckBoxStr = {"show id", "show location", "show linkedLines"};
    public static final String[] RadioStr = {"normal", "add", "remove", "select", "moveTo"
            , "makeLine", "removeLine", "select src", "select dest"};
}
