package dij.nearestpath.graphics.path;

import java.util.ArrayList;

/**
 * Created by E on 2017/5/24.
 */
public class Path {
    public ArrayList<Integer> unchecked;
    public int pId;

    public Path(int pId) {
        this.unchecked = new ArrayList<>();
        this.pId = pId;
    }
}
