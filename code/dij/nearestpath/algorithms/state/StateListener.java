package dij.nearestpath.algorithms.state;

import java.util.ArrayList;

/**
 * Created by E on 2017/5/23.
 */
public interface StateListener {
    void onStateChanged();

    void onFlow(int pId, ArrayList<Integer> incoming);

    void replaced(int[] oldPath, int[] nearerPath);
}
