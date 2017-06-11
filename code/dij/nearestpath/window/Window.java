package dij.nearestpath.window;

import dij.nearestpath.data.Map;
import dij.nearestpath.graphics.Painter;

import javax.swing.*;

import java.awt.*;

import static dij.nearestpath.data.StaticThing.H;
import static dij.nearestpath.data.StaticThing.W;

/**
 * Created by E on 2017/5/23.
 */
public class Window extends JFrame {
    private Map map;
    private final Painter painter;
    public Window(Map map) throws HeadlessException {
        this.map = map;
        setContentPane(painter = new Painter(this, map));
        initMenu();
    }



    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(W + 17, H + 40 + 23);
        setLocationRelativeTo(null);
    }

    private void initMenu() {
        setJMenuBar(new JMenuBar(){
            {
                add(new JMenu("Operation") {
                    {
                        {
                            setMnemonic('O');
                        }
                        add(new JMenuItem("Run") {
                            {
                                setMnemonic('R');
                                setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
                                addActionListener(e -> painter.run());
                            }
                        });
                        add(new JMenuItem("Insert Edge") {
                            {
                                setMnemonic('I');
                                setAccelerator(KeyStroke.getKeyStroke("ctrl I"));
                                addActionListener(e -> map.randomNewEdge());
                            }
                        });
                    }
                });
            }
        });
    }
    {
        new Thread(){
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        sleep(36);
                        repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


}
