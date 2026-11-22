package org.example.controller;

import org.example.controller.action.ActionDraw;
import org.example.controller.action.AppAction;
import org.example.model.Model;
import org.example.model.Factory.ShapeState;
import org.example.model.Factory.ShapeType;
import org.example.view.MenuCreator;
import org.example.view.MyFrame;
import org.example.view.MyPanel;

import javax.swing.*;
import java.awt.*;

public class Controller {

    private static Controller instance;
    private final Model model;
    private final MyFrame frame;
    private final MyPanel panel;
    private AppAction action;
    private final ShapeState shapeState;

    private Controller() {
        model = new Model();

        panel = new MyPanel(this);
        model.addObserver(panel);

        frame = new MyFrame();
        shapeState = new ShapeState(ShapeType.RECTANGULAR_SHAPE, Color.RED, true);
        action = new ActionDraw(model, shapeState);

        MenuCreator menuCreator = MenuCreator.getInstance();
        menuCreator.setController(this);
        menuCreator.setShapeState(shapeState);
        frame.setJMenuBar(menuCreator.createMenu());
        frame.add(menuCreator.createToolBar(JToolBar.VERTICAL), BorderLayout.EAST);

        // Панель добавляется ровно один раз, и только после этого окно показывается.
        frame.setPanel(panel);
        frame.setVisible(true);
    }

    public static synchronized Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void mousePressed(Point point) {
        action.mousePressed(point);
    }

    public void mouseDragged(Point point) {
        action.mouseDragged(point);
    }

    public void draw(Graphics2D g2) {
        model.draw(g2);
    }

    public void switchActionDraw() {
        action = new ActionDraw(model, shapeState);
    }
}
