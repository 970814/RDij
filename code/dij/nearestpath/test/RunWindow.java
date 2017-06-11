package dij.nearestpath.test;

import dij.nearestpath.data.Map;
        import dij.nearestpath.window.Window;

        import javax.swing.*;

/**
 * Created by E on 2017/5/23.
 */
public class RunWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Window(new Map(8)).setVisible(true));
    }
}
