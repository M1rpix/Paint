package org.example.view;

import org.example.controller.Controller;
import org.example.model.Factory.ShapeState;
import org.example.model.Factory.ShapeType;
import org.example.view.menu.AppCommand;
import org.example.view.menu.CommandActionListener;
import org.example.view.menu.SwitchFill;
import org.example.view.menu.SwitchShape;

import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

public class MenuCreator {

    private static MenuCreator instance;

    private ShapeState shapeState;
    private Controller controller;
    private JRadioButtonMenuItem fillItem;
    private JRadioButtonMenuItem noFillItem;
    private JRadioButtonMenuItem drawItem;
    private JRadioButtonMenuItem selectItem;

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
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createShapeMenu());
        menuBar.add(createColorMenu());
        menuBar.add(createFillMenu());
        menuBar.add(createActionMenu());
        menuBar.add(createEditMenu());
        return menuBar;
    }

    public JToolBar createToolBar(int orientation) {
        JToolBar toolBar = new JToolBar(orientation);
        toolBar.setFloatable(false);
        ArrayList<AbstractAction> items = createToolBarItems();
        items.forEach(toolBar::add);
        return toolBar;
    }

    private ArrayList<AbstractAction> createToolBarItems() {
        ArrayList<AbstractAction> items = new ArrayList<>();

        items.add(createToolBarItem("draw", "Рисовать", "Режим рисования (F1)",
                () -> selectAction("draw")));
        items.add(createToolBarItem("select", "Выделять", "Выделение и перетаскивание (F2)",
                () -> selectAction("select")));
        items.add(createToolBarItem("rotate_left", "Повернуть влево", "Повернуть выбранную фигуру на 15° влево (Ctrl+[)",
                controller::rotateSelectedCounterClockwise));
        items.add(createToolBarItem("rotate_right", "Повернуть вправо", "Повернуть выбранную фигуру на 15° вправо (Ctrl+])",
                controller::rotateSelectedClockwise));
        items.add(createToolBarItem("fill", "С заливкой", "Фигуры с заливкой",
                () -> new SwitchFill(true, shapeState, fillItem, noFillItem).execute()));
        items.add(createToolBarItem("no_fill", "Без заливки", "Фигуры только контуром",
                () -> new SwitchFill(false, shapeState, fillItem, noFillItem).execute()));
        items.add(createToolBarItem("clear", "Очистить", "Очистить холст",
                controller::clearAll));

        return items;
    }

    private AbstractAction createToolBarItem(String key, String text, String toolTip, AppCommand command) {
        ImageIcon icon = null;
        if ("rotate_left".equals(key)) {
            icon = mirroredIcon("ico/rotate_16x16.png");
        } else if ("rotate_right".equals(key)) {
            icon = loadIcon("ico/rotate_16x16.png");
        } else {
            icon = loadIcon("ico/" + key + "_16x16.png");
        }
        CommandActionListener action = new CommandActionListener(command);
        action.putValue(Action.NAME, text);
        action.putValue(Action.SHORT_DESCRIPTION, toolTip);
        if (icon != null) {
            action.putValue(Action.SMALL_ICON, icon);
        }
        return action;
    }

    private void selectAction(String actionKey) {
        if ("draw".equals(actionKey)) {
            controller.switchActionDraw();
            if (drawItem != null) {
                drawItem.setSelected(true);
            }
        } else {
            controller.switchActionSelect();
            if (selectItem != null) {
                selectItem.setSelected(true);
            }
        }
    }

    private JMenu createActionMenu() {
        JMenu actionMenu = new JMenu("Действие");

        drawItem = new JRadioButtonMenuItem("Рисовать", true);
        drawItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        drawItem.addActionListener(event -> selectAction("draw"));
        actionMenu.add(drawItem);

        selectItem = new JRadioButtonMenuItem("Выделять", false);
        selectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        selectItem.addActionListener(event -> selectAction("select"));
        actionMenu.add(selectItem);

        ButtonGroup group = new ButtonGroup();
        group.add(drawItem);
        group.add(selectItem);

        return actionMenu;
    }

    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Правка");

        JRadioButtonMenuItem rotateLeft = new JRadioButtonMenuItem("Повернуть влево на 15°");
        rotateLeft.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, KeyEvent.CTRL_DOWN_MASK));
        rotateLeft.addActionListener(event -> controller.rotateSelectedCounterClockwise());
        editMenu.add(rotateLeft);

        JRadioButtonMenuItem rotateRight = new JRadioButtonMenuItem("Повернуть вправо на 15°");
        rotateRight.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, KeyEvent.CTRL_DOWN_MASK));
        rotateRight.addActionListener(event -> controller.rotateSelectedClockwise());
        editMenu.add(rotateRight);

        editMenu.addSeparator();

        JRadioButtonMenuItem clear = new JRadioButtonMenuItem("Очистить холст");
        clear.addActionListener(event -> controller.clearAll());
        editMenu.add(clear);

        return editMenu;
    }

    private JMenu createFillMenu() {
        JMenu fillMenu = new JMenu("Заливка");

        fillItem = new JRadioButtonMenuItem("С заливкой", true);
        noFillItem = new JRadioButtonMenuItem("Без заливки", false);
        fillItem.addActionListener(new CommandActionListener(
                new SwitchFill(true, shapeState, fillItem, noFillItem)));
        noFillItem.addActionListener(new CommandActionListener(
                new SwitchFill(false, shapeState, fillItem, noFillItem)));
        fillMenu.add(fillItem);
        fillMenu.add(noFillItem);

        ButtonGroup group = new ButtonGroup();
        group.add(fillItem);
        group.add(noFillItem);

        return fillMenu;
    }

    private JMenu createColorMenu() {
        JMenu colorMenu = new JMenu("Цвет");

        JRadioButtonMenuItem redItem = new JRadioButtonMenuItem("Красный", true);
        redItem.addActionListener(event -> shapeState.setColor(Color.RED));
        colorMenu.add(redItem);

        JRadioButtonMenuItem blueItem = new JRadioButtonMenuItem("Синий", false);
        blueItem.addActionListener(event -> shapeState.setColor(Color.BLUE));
        colorMenu.add(blueItem);

        JRadioButtonMenuItem rgbItem = new JRadioButtonMenuItem("Выбор цвета");
        rgbItem.addActionListener(event -> {
            Color color = JColorChooser.showDialog(null, "Выбор цвета", shapeState.getColor());
            if (color != null) {
                shapeState.setColor(color);
            }
        });
        colorMenu.add(rgbItem);

        ButtonGroup group = new ButtonGroup();
        group.add(redItem);
        group.add(blueItem);
        group.add(rgbItem);

        return colorMenu;
    }

    private JMenu createShapeMenu() {
        JMenu shapeMenu = new JMenu("Фигура");

        JRadioButtonMenuItem rectangleItem = new JRadioButtonMenuItem("Прямоугольник", true);
        rectangleItem.addActionListener(new CommandActionListener(
                new SwitchShape(shapeState, ShapeType.RECTANGULAR_SHAPE, rectangleItem)));
        shapeMenu.add(rectangleItem);

        JRadioButtonMenuItem ellipseItem = new JRadioButtonMenuItem("Эллипс", false);
        ellipseItem.addActionListener(new CommandActionListener(
                new SwitchShape(shapeState, ShapeType.ELLIPSE_SHAPE, ellipseItem)));
        shapeMenu.add(ellipseItem);

        ButtonGroup group = new ButtonGroup();
        group.add(rectangleItem);
        group.add(ellipseItem);

        return shapeMenu;
    }

    private ImageIcon loadIcon(String resourcePath) {
        URL url = getClass().getClassLoader().getResource(resourcePath);
        return url == null ? null : new ImageIcon(url);
    }

    private ImageIcon mirroredIcon(String resourcePath) {
        ImageIcon icon = loadIcon(resourcePath);
        if (icon == null) {
            return null;
        }
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = flipped.createGraphics();
        g.drawImage(icon.getImage(), width, 0, -width, height, null);
        g.dispose();
        return new ImageIcon(flipped);
    }
}
