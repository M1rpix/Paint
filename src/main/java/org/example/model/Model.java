package org.example.model;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Модель рисунка: хранит фигуры и уведомляет наблюдателей (панель) об изменениях.
 * Внимание: java.util.Observable объявлен устаревшим (deprecated) начиная с Java 9.
 */
public class Model extends Observable {

    private MyShape drawableShape;
    private List<MyShape> shapeList;

    public Model() {
        this.shapeList = new ArrayList<>();
    }

    public void setShapeList(List<MyShape> shapeList) {
        this.shapeList = shapeList;
    }

    public void addShape(MyShape shape) {
        shapeList.add(shape);
    }

    public void setDrawableShape(MyShape shape) {
        drawableShape = shape;
    }

    public void changeShape(Point2D x, Point2D y) {
        if (drawableShape == null) {
            return;
        }
        drawableShape.setFrame(x, y);
        this.setChanged();
        this.notifyObservers();
    }

    public void draw(Graphics2D g) {
        for (MyShape shape : shapeList) {
            shape.draw(g);
        }
    }
}
