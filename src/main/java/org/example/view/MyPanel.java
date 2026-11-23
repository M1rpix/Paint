package org.example.view;

import org.example.controller.Controller;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

public class MyPanel extends JPanel implements Observer {
    private static final long serialVersionUID = 1L;

    private final transient Controller controller;

    public MyPanel(Controller controller) {
        this.controller = controller;
        MouseAdapter handler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                controller.mousePressed(event.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                controller.mouseDragged(event.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                controller.mouseReleased(event.getPoint());
            }
        };
        addMouseListener(handler);
        addMouseMotionListener(handler);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        controller.draw(g2);
        SelectionPainter.paint(g2, controller.getModel().getSelectedShape());
    }

    @Override
    public void update(Observable observable, Object arg) {
        repaint();
    }
}
