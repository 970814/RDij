package dij.nearestpath.graphics;

import dij.nearestpath.algorithms.Dijkstra;
import dij.nearestpath.algorithms.state.State;
import dij.nearestpath.algorithms.state.StateListener;
import dij.nearestpath.data.*;
import dij.nearestpath.data.Point;
import dij.nearestpath.graphics.path.Path;
import dij.nearestpath.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import static dij.nearestpath.data.StaticThing.*;

/**
 * Created by E on 2017/5/23.
 */
public class Painter extends JComponent implements StateListener {
    private Painter this0 = this;
    private Window window;
    private Map map;
    public Painter(Window window, Map map) {
        this.window = window;
        this.map = map;
//        addMouseMotionListener();
    }

    private void addMouseMotionListener() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println(e.getPoint());
            }
        });
    }

    private State state;
    private boolean runFlag = false;
    int[] path = null;
    int srcId = -1;
    int destId = -1;

    public void run() {
        if (keyFlag) return;
        keyFlag = true;
        new Thread() {
            @Override
            public void run() {
                checked.clear();
                path = null;
                if (srcId == -1 || destId == -1){
                    keyFlag = false;
                    return;
                }
                state = new dij.nearestpath.algorithms.state.State();
                Dijkstra.search(map, srcId, destId, state, this0);
                path = Dijkstra.makePath(state.parent, srcId, destId);
                keyFlag = false;
            }
        }.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D d = (Graphics2D) g;
        StaticThing.drawBorder(d);
        drawSrcAndDest(d);
        drawId(d);
        drawEdge(d);
        if (!keyFlag || state == null || !state.isInited()) drawPoint(d);
        else drawProcess(d);
        drawSrcDestPath(d);
        drawRect(d);
        drawSelected(d);
    }

    private void drawSelected(Graphics2D d) {
        d.setColor(Color.BLUE);
        d.setStroke(fattestStroke);
        for (Integer id : movePoints) {
            Point p = map.findPointById(id);
            d.drawLine(p.x, p.y, p.x, p.y);
        }
    }

    private void drawSrcDestPath(Graphics2D d) {
        if (path != null)
            drawPath(path, d, Color.ORANGE, StaticThing.fatterStroke);
    }

    private void drawProcess(Graphics2D d) {
        d.setStroke(StaticThing.fattestStroke);
        boolean[] inHeap = new boolean[map.size];
        state.heap.forEach(id -> {
            inHeap[id] = true;
            Point p = map.findPointById(id);
            d.setColor(StaticThing.HeapColor);
            d.drawLine(p.x, p.y, p.x, p.y);
        });
        for (int id = 0; id < inHeap.length; id++)
            if (!inHeap[id]) {
                Point p = map.findPointById(id);
                if (state.added[id]) d.setColor(StaticThing.ExploredColor);
                else d.setColor(StaticThing.UnexploredColor);
                d.drawLine(p.x, p.y, p.x, p.y);
            }

        d.setStroke(StaticThing.fatterStroke);
        d.setColor(StaticThing.water);
        if (unchecked != null) {
            Point U = map.findPointById(unchecked.pId);
            for (int id : unchecked.unchecked) {
                Point V = map.findPointById(id);
                double k = (U.y - V.y) / (double) (U.x - V.x);
                double b = U.y - k * U.x;
                double L = length[id];
                double K = k * k;
                float x = (float) (L / Math.sqrt(1 + K));
                if (V.x >= U.x) x += U.x;
                else x = U.x - x;
                float y = (float) (k * x + b);
                line.setLine(U.x, U.y, x, y);
                d.draw(line);
            }
        }
        d.setStroke(StaticThing.thinStroke);
        if (checked != null) {
            synchronized (lockChecked) {
                for (Path path : checked) {
                    Point U = map.findPointById(path.pId);
                    for (int id : path.unchecked) {
                        Point V = map.findPointById(id);
                        line.setLine(U.x, U.y, V.x, V.y);
                        d.draw(line);
                    }
                }
            }
        }
        replacing(d);
    }

    private final Object lockChecked = new Object();

    private void drawSrcAndDest(Graphics2D d) {
        //绘制终点和起点
        if (srcId != -1) {
            Point src = map.findPointById(srcId);
            Banner.drawBanner(d, Banner.RED, src.x, src.y);
        }
        if (destId != -1) {
            Point dest = map.findPointById(destId);
            Banner.drawBanner(d, Banner.ORANGE, dest.x, dest.y);
        }
    }

    private void replacing(Graphics2D d) {
        int[] oldPath = this.oldPath;
        int[] nearerPath = this.nearerPath;
        if (oldPath != null && nearerPath != null) {
            drawPath(nearerPath, d, StaticThing.NearerColor, StaticThing.fatStroke);
            drawPath(oldPath, d, StaticThing.OldColor, StaticThing.fatStroke);
        }
    }

    private void drawPath(int[] path, Graphics2D d, Color color, Stroke stroke) {
        d.setColor(color);
        Point U = map.findPointById(path[0]);
        for (int i = 1; i < path.length; i++) {
            Point V = map.findPointById(path[i]);
            if (i < path.length - 1)
                Banner.drawBanner(d, Banner.BLACK, V.x, V.y);
            d.setStroke(StaticThing.fattestStroke);
            d.drawLine(V.x, V.y, V.x, V.y);
            d.setStroke(stroke);
            d.drawLine(U.x, U.y, V.x, V.y);
            U = V;
        }
    }

    private void drawPoint(Graphics2D d) {
        if (!showLocation) return;
        Point[] ps = map.points;
        d.setStroke(StaticThing.fattestStroke);
        d.setColor(ExploredColor);
        for (Point p : ps) d.drawLine(p.x, p.y, p.x, p.y);
    }

    private void drawId(Graphics2D d) {
        if (!showId) return;
        d.setColor(StaticThing.IdColor);
        Point[] ps = map.points;
        for (int i = 0; i < ps.length; i++) {
            Point p = ps[i];
            d.drawString(String.valueOf(i), p.x - 4, p.y + 14);
        }
    }

    private Line2D line = new Line2D.Float();

    private void drawEdge(Graphics2D d) {
        if (!showLinkedLines) return;
        d.setColor(StaticThing.ExploredColor);
        d.setStroke(StaticThing.thinnerStroke);
        boolean[][] neighbor = map.neighbor;
        Point[] ps = map.points;
        for (int i = 0; i < neighbor.length; i++)
            for (int j = 0; j < neighbor[i].length; j++)
                if (neighbor[i][j]) {
                    Point p = ps[i];
                    Point q = ps[j];
                    line.setLine(p.x, p.y, q.x, q.y);
                    d.draw(line);
                }
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStateChanged() {
        sleep(40);
    }
    private Path unchecked;
    private ArrayList<Path> checked = new ArrayList<>();
    private int[] length;
    @Override
    public void onFlow(int pId, ArrayList<Integer> incoming) {
        if (incoming != null) {
            unchecked = new Path(pId);
            unchecked.unchecked.addAll(incoming);
            length = new int[map.size];
            while (!incoming.isEmpty()) {
                Iterator<Integer> each = incoming.iterator();
                while (each.hasNext()) {
                    int id = each.next();
                    if (++length[id] >= map.getCost(pId, id)) each.remove();
                }
                sleep(1);
            }
            synchronized (lockChecked) {
                checked.add(unchecked);
            }
            unchecked = null;
        }
    }

    private int[] oldPath;
    private int[] nearerPath;
    @Override
    public void replaced(int[] oldPath, int[] nearerPath) {
        this.oldPath = oldPath;
        this.nearerPath = nearerPath;
        sleep(800);
        this.oldPath = null;
        this.nearerPath = null;
    }

    ///////////////////////////////////////////////////////////////

    private void drawRect(Graphics2D d) {
        d.setStroke(new BasicStroke(1.0f));
        if (rectangle != null) {
            if (removeMode) d.setColor(Color.BLACK);
            else d.setColor(Color.BLUE);
            d.draw(rectangle);
        }
    }

    private java.awt.Point point = new java.awt.Point(0, 0);
    private JPopupMenu popupMenu = new JPopupMenu();
    private JCheckBoxMenuItem[] items = new JCheckBoxMenuItem[CheckBoxStr.length];
    private boolean showId = false;
    private boolean showLocation = false;
    private boolean showLinkedLines = false;
    private JRadioButtonMenuItem[] radioItems = new JRadioButtonMenuItem[RadioStr.length];
    private boolean addMode = false;
    private boolean removeMode = false;
    private boolean normalMode = true;
    private boolean selectMode = false;
    private boolean movedMode = false;
    private boolean mklMode = false;
    private boolean rmlMode = false;
    private boolean ssMode = false;
    private boolean sdMode = false;
    private Rectangle rectangle = null;
    private Stack<Integer> movePoints = new Stack<>();
    private java.awt.Point start = null;
    private boolean keyFlag = false;
    {
        for (int i = 0; i < items.length; i++) {
            items[i] = new JCheckBoxMenuItem(CheckBoxStr[i]);
            popupMenu.add(items[i]);
        }
        items[0].addActionListener((l) -> showId = !showId);
        items[1].addActionListener((l) -> showLocation = !showLocation);
        items[2].addActionListener((l) -> showLinkedLines = !showLinkedLines);
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i <radioItems.length; i++) {
            radioItems[i] = new JRadioButtonMenuItem(RadioStr[i]);
            popupMenu.add(radioItems[i]);
            group.add(radioItems[i]);
        }
        radioItems[0].setSelected(true);
        radioItems[0].addActionListener((l) ->{
            normalMode = true;
            addMode = false;
            removeMode = false;
            selectMode = false;
            movedMode = false;
            mklMode = false;
            rmlMode = false;
            ssMode = false;
            sdMode = false;
            movePoints.clear();
        });
        radioItems[1].addActionListener((l) ->{
            normalMode = false;
            addMode = true;
            removeMode = false;
            selectMode = false;
            movedMode = false;
            mklMode = false;
            rmlMode = false;
            ssMode = false;
            sdMode = false;
            movePoints.clear();
        });
        radioItems[2].addActionListener((l) -> {
            normalMode = false;
            addMode = false;
            removeMode = true;
            movedMode = false;
            selectMode = false;
            mklMode = false;
            rmlMode = false;
            ssMode = false;
            sdMode = false;
            resetSrcDest();
            movePoints.clear();
        });
        radioItems[3].addActionListener((l) -> {
            normalMode = false;
            addMode = false;
            removeMode = false;
            selectMode = true;
            movedMode = false;
            mklMode = false;
            rmlMode = false;
            ssMode = false;
            sdMode = false;
        });
        radioItems[4].addActionListener((l) -> {
            normalMode = false;
            addMode = false;
            removeMode = false;
            selectMode = false;
            movedMode = true;
            mklMode = false;
            rmlMode = false;
            ssMode = false;
            sdMode = false;
        });
        radioItems[5].addActionListener((l) -> {
            normalMode = false;
            addMode = false;
            removeMode = false;
            selectMode = false;
            movedMode = false;
            mklMode = true;
            rmlMode = false;
            ssMode = false;
            sdMode = false;
        });
        radioItems[6].addActionListener((l) -> {
            normalMode = false;
            addMode = false;
            removeMode = false;
            selectMode = false;
            movedMode = false;
            mklMode = false;
            rmlMode = true;
            ssMode = false;
            sdMode = false;
        });
        radioItems[7].addActionListener((l) -> {
            normalMode = false;
            addMode = false;
            removeMode = false;
            selectMode = false;
            movedMode = false;
            mklMode = false;
            rmlMode = false;
            ssMode = true;
            sdMode = false;
            movePoints.clear();
        });
        radioItems[8].addActionListener((l) -> {
            normalMode = false;
            addMode = false;
            removeMode = false;
            selectMode = false;
            movedMode = false;
            mklMode = false;
            rmlMode = false;
            ssMode = false;
            sdMode = true;
            movePoints.clear();
        });
    }

    private void resetSrcDest() {
        state = null;
        srcId = destId = -1;
        path = null;

    }

    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if ((removeMode || selectMode || movedMode || ssMode || sdMode) && e.getButton() == MouseEvent.BUTTON1) {
                    start = e.getPoint();
                } else {
                    start = null;
                }
            }
            public void mouseReleased(MouseEvent event) {
                if (event.isPopupTrigger()) {
                    popupMenu.show(event.getComponent(), event.getX(), event.getY());
                } else if (normalMode) {
                    return;
                } else if (addMode) {
                    // maker.addNewPoint(event.getPoint());
                    map.addNewPoint(event.getPoint());
                } else if (removeMode) {
                    if (keyFlag) {
                        rectangle = null;
                        return;
                    }
                    if (rectangle == null) return;
                    java.awt.Point location = rectangle.getLocation();
                    Dimension size = rectangle.getSize();
                    for (int i = 0; i < map.size; i++) {
                        Point p = map.points[i];
                        if (withinBounds(p, location.x, size.width + location.x, location.y, size.height + location.y)) {
                            map.remove(i);
                            i--;
                        }
                    }
                    rectangle = null;
                } else if (selectMode) {
                    if (keyFlag) {
                        rectangle = null;
                        return;
                    }
                    if (rectangle == null) return;
                    java.awt.Point location = rectangle.getLocation();
                    Dimension size = rectangle.getSize();

                    for (int i = 0; i < map.size; i++) {
                        Point p = map.points[i];
                        if (withinBounds(p, location.x, size.width + location.x, location.y, size.height + location.y))
                            if (!movePoints.contains(i)) movePoints.add(i);
                    }
                    rectangle = null;
                } else if (mklMode) map.makeEdges(movePoints);
                else if (rmlMode) map.removeEdges(movePoints);
                else if (ssMode || sdMode) {
                    if (keyFlag) {
                        rectangle = null;
                        return;
                    }
                    if (rectangle == null) return;
                    java.awt.Point location = rectangle.getLocation();
                    Dimension size = rectangle.getSize();
                    for (int i = 0; i < map.size; i++) {
                        Point p = map.points[i];
                        if (withinBounds(p, location.x, size.width + location.x, location.y, size.height + location.y))
                            selectSrcOrDest(i);
                    }
                    rectangle = null;
                }
            }

            private void selectSrcOrDest(int id) {
                if (ssMode) srcId = id;
                else destId = id;
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                point = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                point = e.getPoint();
                if ((removeMode || selectMode || ssMode || sdMode) && start != null) {
                    java.awt.Point end = e.getPoint();
                    rectangle = new Rectangle(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.abs(end.x - start.x), Math.abs(end.y - start.y));
                } else if (movedMode && start != null) {
                    java.awt.Point end = e.getPoint();
                    int x = end.x - start.x;
                    int y = end.y - start.y;
                    start = end;
                    for (Integer movePoint : movePoints) {
                        Point p = map.findPointById(movePoint);
                        p.x += x;
                        p.y += y;
                        map.update(movePoint);

                    }
                }
            }
        });
    }
    private boolean withinBounds(Point p, int x1, int x2, int y1, int y2) {
        return (x1 <= p.x && p.x <= x2) && (y1 <= p.y && p.y <= y2);
    }
}
