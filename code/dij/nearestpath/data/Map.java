package dij.nearestpath.data;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Stack;

import static dij.nearestpath.data.StaticThing.*;

/**
 * Created by computer on 2017/5/22.
 */
public class Map {
    private int[][] cost;
    public boolean[][] neighbor;
    public int size;
    public Point[] points;

    public void randomNewEdge() {
        int x = random.nextInt(size);
        int y = random.nextInt(size);
        makeEdge(x, y);
    }

    private void makeEdge(int x, int y) {
        if (!neighbor[x][y]) {
            neighbor[x][y] = neighbor[y][x] = true;
            cost[x][y] = cost[y][x] = (int) Point.distance(points[x], points[y]);
            edgeCount++;
        }
    }
    private void removeEdge(int x, int y) {
        if (neighbor[x][y]) {
            neighbor[x][y] = neighbor[y][x] = false;
            cost[x][y] = cost[y][x] = INF;
            edgeCount--;
        }
    }

    public void addNewPoint(java.awt.Point point) {
        size++;
        int[][] newCost = new int[size][size];
        for (int i = 0; i < cost.length; i++) System.arraycopy(cost[i], 0, newCost[i], 0, cost[i].length);
        for (int i = 0; i < size; i++) newCost[i][size - 1] = newCost[size - 1][i] = INF;
        cost = newCost;
        boolean[][] newNeighbor = new boolean[size][size];
        for (int i = 0; i < neighbor.length; i++) System.arraycopy(neighbor[i], 0, newNeighbor[i], 0, neighbor[i].length);
        for (int i = 0; i < size; i++) newNeighbor[i][size - 1] = newNeighbor[size - 1][i] = false;
        neighbor = newNeighbor;
        Point[] newPoints = new Point[size];
        System.arraycopy(points, 0, newPoints, 0, points.length);
        newPoints[size - 1] = new Point(point.x, point.y);
        points = newPoints;
    }
    @SuppressWarnings("Duplicates")
    public void remove(int index) {
        size--;
        int[][] newCost = new int[size][size];
        for (int i = 0; i < newCost.length; i++) {
            int k = i;
            if (k >= index) k++;
            for (int j = 0; j < newCost[i].length; j++) {
                int v = j;
                if (v>=index) v++;
                newCost[i][j] = cost[k][v];
            }
        }
        cost = newCost;
        boolean[][] newNeighbor = new boolean[size][size];
        for (int i = 0; i < newNeighbor.length; i++) {
            int k = i;
            if (k >= index) k++;
            for (int j = 0; j < newNeighbor[i].length; j++) {
                int v = j;
                if (v>=index) v++;
                newNeighbor[i][j] = neighbor[k][v];
            }
        }
        neighbor = newNeighbor;
        Point[] newPoints = new Point[size];
        for (int i = 0; i < newPoints.length; i++) {
            int k = i;
            if (k >= index) k++;
            newPoints[i] = points[k];
        }
        points = newPoints;
    }


    public Map(int n) {
        size = n;
        cost = new int[size][];
        setInf();
        neighbor = new boolean[size][size];
        initPoint();
        randomEdge();
    }

    private void initPoint() {
        points = new Point[size];
        Arrays.setAll(points, i -> points[i] = new Point(randomPointW(), randomPointH()));
    }



    private int edgeCount;
    private void randomEdge() {
        edgeCount = 0;
        for (int i = 0; i < size * 1.5; i++) {
//            int x = random.nextInt(size);
            int x = i % size;
            int y = random.nextInt(size - i % size) + i % size;
            if (x == y) continue;
//            cost[map.y][x] = cost[x][map.y] = random.nextInt(size * size);
            makeEdge(x, y);

        }
        System.out.println("edgeCount: " + edgeCount);
    }

    public void setInf() {
        int[] array = new int[size];
        Arrays.setAll(array, i -> INF);
        Arrays.setAll(cost, i -> array.clone());
    }

    public int[][] getCost() {
        return deepClone(cost);
    }

    private int[][] deepClone(int[][] cost) {
        int[][] newClone = new int[cost.length][];
        Arrays.setAll(newClone, i -> newClone[i] = cost[i].clone());
        return newClone;
    }

    public Point findPointById(int id) {
        return points[id];
    }

    public int getCost(int pId, int id) {
        if (neighbor[pId][id])
            return cost[pId][id];
        throw new NoSuchElementException("Edge: " + pId + " to " + id);
    }


    public void makeEdges(Stack<Integer> movePoints) {
        makeEdges(movePoints, 0, movePoints.size());
    }

    private void makeEdges(Stack<Integer> movePoints, int from, int to) {
        if (to - from > 1) {
            for (int i = from + 1; i < to; i++) makeEdge(movePoints.get(from), movePoints.get(i));
            makeEdges(movePoints, from + 1, to);
        }
    }

    public void removeEdges(Stack<Integer> movePoints) {
        removeEdges(movePoints, 0, movePoints.size());
    }

    private void removeEdges(Stack<Integer> movePoints, int from, int to) {
        if (to - from > 1) {
            for (int i = from + 1; i < to; i++) removeEdge(movePoints.get(from), movePoints.get(i));
            removeEdges(movePoints, from + 1, to);
        }
    }

    public void update(int id) {
        for (int i = 0; i < neighbor[id].length; i++)
            if (neighbor[id][i]) cost[id][i] = cost[i][id] = (int) Point.distance(points[id], points[i]);
    }
}
