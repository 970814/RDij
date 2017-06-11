package dij.nearestpath.test;

import dij.nearestpath.algorithms.Dijkstra;
import dij.nearestpath.algorithms.state.State;
import dij.nearestpath.data.Map;

import java.util.Stack;

/**
 * Created by E on 2017/5/22.
 */
public class Test {
    public static void main(String[] args) {
        Map map = new Map(100);
//        map.show();
        State state = Dijkstra.search(map, 0, 3);
        Stack<Integer> path = state.calPath();
        if (path != null) {
            int pre = path.pop();
            System.out.print(pre);
            while (!path.isEmpty()) {
                int now = path.pop();
                System.out.print(" -> (cost " + state.cost[pre][now] + ") -> " + now);
            }
            System.out.println();
        } else System.out.println("No path");
    }
}
