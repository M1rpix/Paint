package org.example.controller;

import org.example.controller.action.ActionChangeShape;
import org.example.controller.action.ActionDraw;
import org.example.controller.action.ActionSelect;
import org.example.controller.action.AppAction;
import org.example.controller.state.UndoMachine;
import org.example.model.Factory.ShapeState;
import org.example.model.Factory.ShapeType;
import org.example.model.Model;
import org.example.model.MyShape;
import org.example.view.MenuCreator;
import org.example.view.MyFrame;
import org.example.view.MyPanel;

import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private static final double ROTATION_STEP = 15.0;

    private static Controller instance;
    private final Model model;
    private final MyFrame frame;
    private final MyPanel panel;
    private final ShapeState shapeState;
    private final UndoMachine undoMachine;
    private final ArrayList<AppAction> actions;
    private AppAction action;

    private Controller() {
        model = new Model();

        panel = new MyPanel(this);
        model.addObserver(panel);

        frame = new MyFrame();
        shapeState = new ShapeState(ShapeType.RECTANGULAR_SHAPE, Color.RED, true);
        undoMachine = new UndoMachine();

        actions = new ArrayList<>();
        actions.add(new ActionDraw(model, shapeState));
        actions.add(new ActionSelect(model));
        action = actions.get(0);

        MenuCreator menuCreator = MenuCreator.getInstance();
        menuCreator.setController(this);
        menuCreator.setShapeState(shapeState);
        // Меню создаём раньше панели инструментов: в нём рождаются пункты undo/redo,
        // на которые тулбар (и UndoMachine) потом ссылается.
        frame.setJMenuBar(menuCreator.createMenu());
        JToolBar toolBar = menuCreator.createToolBar(JToolBar.VERTICAL);
        frame.add(toolBar, BorderLayout.WEST);

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

        if (action instanceof ActionDraw) {
            // Команда рисования ссылается на только что созданную фигуру.
            saveToHistory(((ActionDraw) action).takeCompletedAction());
        } else if (action instanceof ActionSelect) {
            saveToHistory(((ActionSelect) action).createChangeAction());
        }
    }

    private void saveToHistory(AppAction change) {
        if (change == null) {
            return;
        }
        undoMachine.add(change);
    }

    public void draw(Graphics2D g2) {
        model.draw(g2);
    }

    public Model getModel() {
        return model;
    }

    public UndoMachine getUndoMachine() {
        return undoMachine;
    }

    public AppAction getAction() {
        return action;
    }

    public List<AppAction> getActions() {
        return actions;
    }

    public boolean isActionSelected(AppAction candidate) {
        return candidate == action;
    }

    public void switchAction(AppAction target) {
        if (target == null || target == action) {
            return;
        }
        action.setEnabled(false);
        action = target;
        action.setEnabled(true);
        if (!(action instanceof ActionSelect)) {
            model.setSelectedShape(null);
        }
        model.fireChange();
    }

    public void rotateSelected(double degrees) {
        MyShape selected = model.getSelectedShape();
        if (selected == null) {
            return;
        }
        MyShape before = selected.cloneShape();
        model.rotateSelected(degrees);
        saveToHistory(new ActionChangeShape(model, selected, before, selected.cloneShape()));
    }

    public void rotateSelectedClockwise() {
        rotateSelected(ROTATION_STEP);
    }

    public void rotateSelectedCounterClockwise() {
        rotateSelected(-ROTATION_STEP);
    }

    public void clearAll() {
        model.clear();
        undoMachine.reset();
    }
}
