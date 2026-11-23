package org.example.controller;

import org.example.controller.action.ActionDraw;
import org.example.controller.action.ActionSelect;
import org.example.controller.action.AppAction;
import org.example.model.Factory.ShapeState;
import org.example.model.Factory.ShapeType;
import org.example.model.Model;
import org.example.view.MenuCreator;
import org.example.view.MyFrame;
import org.example.view.MyPanel;

import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;

public class Controller {

    private static final double ROTATION_STEP = 15.0;

    private static Controller instance;
    private final Model model;
    private final MyFrame frame;
    private final MyPanel panel;
    private final ShapeState shapeState;
    private final Map<String, AppAction> actions;
    private AppAction action;

    private Controller() {
        model = new Model();

        panel = new MyPanel(this);
        model.addObserver(panel);

        frame = new MyFrame();
        shapeState = new ShapeState(ShapeType.RECTANGULAR_SHAPE, Color.RED, true);

        actions = new LinkedHashMap<>();
        actions.put("draw", new ActionDraw(model, shapeState));
        actions.put("select", new ActionSelect(model));
        action = actions.get("draw");

        MenuCreator menuCreator = MenuCreator.getInstance();
        menuCreator.setController(this);
        menuCreator.setShapeState(shapeState);
        frame.setJMenuBar(menuCreator.createMenu());
        JToolBar toolBar = menuCreator.createToolBar(JToolBar.VERTICAL);
        frame.add(toolBar, BorderLayout.EAST);

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

    public void mouseReleased(Point point) {
        action.mouseReleased(point);
    }

    public void draw(java.awt.Graphics2D g2) {
        model.draw(g2);
    }

    public Model getModel() {
        return model;
    }

    public AppAction getAction() {
        return action;
    }

    public boolean isActionSelected(String actionKey) {
        return actions.get(actionKey) == action;
    }

    public void switchAction(String actionKey) {
        AppAction target = actions.get(actionKey);
        if (target == null || target == action) {
            return;
        }
        model.setDrawableShape(null);
        action = target;
        model.fireChange();
    }

    public void switchActionDraw() {
        switchAction("draw");
    }

    public void switchActionSelect() {
        if (action instanceof ActionSelect) {
            return;
        }
        model.setSelectedShape(null);
        switchAction("select");
        model.fireChange();
    }

    public void rotateSelected(double degrees) {
        model.rotateSelected(degrees);
    }

    public void rotateSelectedClockwise() {
        rotateSelected(ROTATION_STEP);
    }

    public void rotateSelectedCounterClockwise() {
        rotateSelected(-ROTATION_STEP);
    }

    public void clearAll() {
        model.clear();
    }
}
