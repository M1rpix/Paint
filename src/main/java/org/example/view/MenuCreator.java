package org.example.view;

import org.example.controller.Controller;
import org.example.model.Factory.ShapeState;
import org.example.model.Factory.ShapeType;
import org.example.view.menu.AppCommand;
import org.example.view.menu.CommandActionListener;
import org.example.view.menu.SwitchFill;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class MenuCreator {
    private static MenuCreator instance;
    private ShapeState shapeState;
    private Controller controller;

    private JRadioButtonMenuItem fillItem;
    private JRadioButtonMenuItem noFillItem;

    private MenuCreator() {

    }

    public static MenuCreator getInstance() {
        synchronized (MenuCreator.class) {
            if (instance == null) {
                instance = new MenuCreator();
            }
            return instance;
        }
    }

    public void setShapeState(ShapeState shapeState) {
        this.shapeState = shapeState;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public JMenuBar createMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu shapeMenu = createShapeMenu();
        jMenuBar.add(shapeMenu);

        JMenu colorMenu = createColorMenu();
        jMenuBar.add(colorMenu);

        JMenu fillMenu = createFillMenu();
        jMenuBar.add(fillMenu);

        return jMenuBar;
    }

    public JToolBar createToolBar(int orientation) {
        ArrayList<Action> toolBarItems = createToolBarItems();
        JToolBar jToolBar = new JToolBar(orientation);
        toolBarItems.forEach(jToolBar::add);

        return jToolBar;
    }

    private ArrayList<Action> createToolBarItems() {
        ArrayList<Action> menuItems = new ArrayList<>();

        URL fillUrl = getClass().getClassLoader().getResource("ico/fill_16x16.png");
        ImageIcon fillIco = fillUrl == null ? null : new ImageIcon(fillUrl);
        AppCommand fillCommand = new SwitchFill(true, shapeState, fillItem, noFillItem);
        menuItems.add(new CommandActionListener("С заливкой", fillIco, fillCommand));

        URL noFillUrl = getClass().getClassLoader().getResource("ico/no_fill_16x16.png");
        ImageIcon noFillIco = noFillUrl == null ? null : new ImageIcon(noFillUrl);
        AppCommand noFillCommand = new SwitchFill(false, shapeState, fillItem, noFillItem);
        menuItems.add(new CommandActionListener("Без заливки", noFillIco, noFillCommand));

        return menuItems;
    }

    private JMenu createActionMenu() {
        ButtonGroup group = new ButtonGroup();
        JMenu actionMenu = new JMenu("Действие");

        JRadioButtonMenuItem drawItem = new JRadioButtonMenuItem("Рисовать", true);
        drawItem.addActionListener(action -> controller.switchActionDraw());
        actionMenu.add(drawItem);
        group.add(drawItem);

        return actionMenu;
    }

    private JMenu createFillMenu() {
        ButtonGroup group = new ButtonGroup();
        JMenu fillMenu = new JMenu("Заливка");

        fillItem = new JRadioButtonMenuItem("С заливкой", true);
        noFillItem = new JRadioButtonMenuItem("Без заливки", false);
        fillItem.addActionListener(new CommandActionListener(new SwitchFill(true, shapeState, fillItem, noFillItem)));
        noFillItem.addActionListener(new CommandActionListener(new SwitchFill(false, shapeState, fillItem, noFillItem)));
        fillMenu.add(fillItem);
        group.add(fillItem);

        fillMenu.add(noFillItem);
        group.add(noFillItem);

        return fillMenu;
    }

    private JMenu createColorMenu() {
        ButtonGroup group = new ButtonGroup();
        JMenu shapeMenu = new JMenu("Цвет");
        JRadioButtonMenuItem redItem = new JRadioButtonMenuItem("Красный", true);
        redItem.addActionListener(action -> shapeState.setColor(Color.RED));
        shapeMenu.add(redItem);
        group.add(redItem);

        JRadioButtonMenuItem blueItem = new JRadioButtonMenuItem("Синий", false);
        blueItem.addActionListener(action -> shapeState.setColor(Color.BLUE));
        shapeMenu.add(blueItem);
        group.add(blueItem);

        JRadioButtonMenuItem rgbItem = new JRadioButtonMenuItem("Выбор цвета");
        rgbItem.addActionListener(action -> {
            Color color = JColorChooser.showDialog(null, "Выбор цвета", shapeState.getColor());
            if (color != null) {          // пользователь мог нажать "Отмена"
                shapeState.setColor(color);
            }
        });
        shapeMenu.add(rgbItem);
        group.add(rgbItem);

        return shapeMenu;
    }

    private JMenu createShapeMenu() {
        ButtonGroup group = new ButtonGroup();
        JMenu shapeMenu = new JMenu("Фигура");
        JRadioButtonMenuItem squareItem = new JRadioButtonMenuItem("Прямоугольник", true);
        squareItem.addActionListener(action -> shapeState.setShapeType(ShapeType.RECTANGULAR_SHAPE));
        shapeMenu.add(squareItem);
        group.add(squareItem);

        return shapeMenu;
    }
}
