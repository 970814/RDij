package dij.nearestpath.algorithms.state;

import dij.datastruct.BinaryHeap;

import java.util.Stack;

/**
 * Created by E on 2017/5/23.
 */
public class State {
    public int[] parent;
    public boolean[] added;
    public int[][] cost;
    public  BinaryHeap<Integer> heap;
    public int src;
    public int dest;

    public State() {
    }

    public State(int[] parent, boolean[] added, int[][] cost, BinaryHeap<Integer> heap, int src, int dest) {
        setAll(parent, added, cost, heap, src, dest);
    }

    public Stack<Integer> calPath() {
        Stack<Integer> path = new Stack<>();
        parent[src] = -1;
        int x = dest;
        do {
            path.push(x);
            x = parent[x];
        } while (x != -1);
        return path;
    }

    private boolean inited = false;
    public void setAll(int[] parent, boolean[] added, int[][] cost, BinaryHeap<Integer> heap, int src, int dest) {
        this.parent = parent;
        this.added = added;
        this.cost = cost;
        this.heap = heap;
        this.src = src;
        this.dest = dest;
        inited = true;
    }
    public boolean isInited() {
        return inited;
    }
}
