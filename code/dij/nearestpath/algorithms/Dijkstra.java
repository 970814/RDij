package dij.nearestpath.algorithms;

import dij.datastruct.BinaryHeap;
import dij.nearestpath.algorithms.state.State;
import dij.nearestpath.algorithms.state.StateListener;
import dij.nearestpath.data.Map;
import dij.nearestpath.data.StaticThing;

import java.util.ArrayList;

/**
 * Created by computer on 2017/5/22.
 */
@SuppressWarnings("Duplicates")
public class Dijkstra {
    public static State search(Map map, int src, int dest) {
        return search(map, src, dest, new State());
    }

    public static State search(Map map, int src, int dest, State state) {
        int[][] cost = map.getCost();
        BinaryHeap<Integer> heap = new BinaryHeap<>((o1, o2) -> cost[src][o1] - cost[src][o2]);
        int[] parent = new int[map.size];
        boolean[] added = new boolean[map.size];
        boolean[][] nei = map.neighbor;
        added[src] = true;
        if (state == null) state = new State(parent, added, cost, heap, src, dest);
        else state.setAll(parent, added, cost, heap, src, dest);
        for (int i = 0; i < nei[src].length; i++)
            if (nei[src][i]) {
                parent[i] = src;
                heap.insert(i);
                added[i] = true;
            }
        boolean find = false;
        while (!heap.isEmpty()) {
            int min = heap.extractTheMostValue();
            for (int i = 0; i < nei[min].length; i++)
                if (nei[min][i]) {
                    int d = cost[src][min] + cost[min][i];
                    if (d < cost[src][i]) {
                        cost[i][src] = cost[src][i] = d;
                        int index = heap.find(i);
                        if (index > -1) heap.insert(heap.delete(index));
                        parent[i] = min;
                    }
                    if (!added[i]) {
                        heap.insert(i);
                        added[i] = true;
                    }
                    if (i == dest) find = true;
                }
        }
        return find ? state : null;
    }
    public static State search(Map map, int src, int dest, State state, StateListener listener) {
        int[][] cost = map.getCost();
        BinaryHeap<Integer> heap = new BinaryHeap<>((o1, o2) -> cost[src][o1] - cost[src][o2]);
        int[] parent = new int[map.size];
        boolean[] added = new boolean[map.size];
        boolean[][] nei = map.neighbor;
        if (state == null) state = new State(parent, added, cost, heap, src, dest);
        else state.setAll(parent, added, cost, heap, src, dest);
        added[src] = true;
        action(listener);
        ArrayList<Integer> incoming = new ArrayList<>();
        boolean find = false;
        for (int i = 0; i < nei[src].length; i++)
            if (nei[src][i]) {
                if (i == dest){
                    find = true;
                    incoming.add(i);
                } else {
                    parent[i] = src;
                    heap.insert(i);
                    added[i] = true;
                    incoming.add(i);
                }
            }
        listener.onFlow(src, incoming);
        action(listener);
        while (!heap.isEmpty()) {
            int min = heap.extractTheMostValue();
            action(listener);
            for (int i = 0; i < nei[min].length; i++)
                if (nei[min][i]) {
                    int d = cost[src][min] + cost[min][i];
                    int[] oldPath = null;
                    int[] nearerPath = null;
                    if (d < cost[src][i]) {
                        boolean replace = cost[src][i] != StaticThing.INF;
                        if (replace)
                            oldPath = makePath(parent, src, i);
                        cost[i][src] = cost[src][i] = d;
                        int index = heap.find(i);
                        if (index > -1) heap.insert(heap.delete(index));
                        parent[i] = min;
                        if (replace)
                            nearerPath = makePath(parent, src, i);
                        if (replace)
                            listener.replaced(oldPath, nearerPath);
                    } else {
                        if (!added[i]) {//不然会发生死循环
                            oldPath = makePath(parent, src, i);
                            int save = parent[i];
                            parent[i] = min;
                            nearerPath = makePath(parent, src, i);
                            parent[i] = save;
                            listener.replaced(oldPath, nearerPath);
                        }
                    }
                    if (i == dest) {
                        find = true;
                        incoming.add(i);
                    } else if (!added[i]) {
                        heap.insert(i);
                        added[i] = true;
                        incoming.add(i);
                    }
                }
            listener.onFlow(min, incoming);
        }
        return find ? state : null;
    }

    //计算出当前点到点的路径
    public static int[] makePath(int[] parent, int src, int dest) {
        int length = 2;
        int x = dest;
        while ((x = parent[x]) != src) length++;
        int[] path = new int[length];
        x = dest;
        for (int i = path.length - 1; i > -1; i--) {
            path[i] = x;
            x = parent[x];
        }
        return path;
    }

    private static void action(StateListener listener) {
        if (listener != null) listener.onStateChanged();
    }
}
