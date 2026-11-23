package org.example.model;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public class Model extends Observable {

    private final List<MyShape> shapeList;
    private MyShape drawableShape;
    private MyShape selectedShape;

    public Model() {
        this.shapeList = new ArrayList<>();
    }

    public List<MyShape> getShapes() {
        return Collections.unmodifiableList(shapeList);
    }

    public void setShapes(List<MyShape> shapes) {
        shapeList.clear();
        shapeList.addAll(shapes);
        selectedShape = null;
        fireChange();
    }

    public void addShape(MyShape shape) {
        shapeList.add(shape);
        fireChange();
    }

    public void clear() {
        shapeList.clear();
        drawableShape = null;
        selectedShape = null;
        fireChange();
    }

    public MyShape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(MyShape shape) {
        if (selectedShape == shape) {
            return;
        }
        selectedShape = shape;
        fireChange();
    }

    public MyShape findShape(Point2D point) {
        for (int i = shapeList.size() - 1; i >= 0; i--) {
            if (shapeList.get(i).contains(point)) {
                return shapeList.get(i);
            }
        }
        return null;
    }

    public void setDrawableShape(MyShape shape) {
        drawableShape = shape;
    }

    public void changeShape(Point2D first, Point2D second) {
        if (drawableShape == null) {
            return;
        }
        drawableShape.setFrame(first, second);
        fireChange();
    }

    public void rotateSelected(double degrees) {
        if (selectedShape == null) {
            return;
        }
        selectedShape.rotate(degrees);
        fireChange();
    }

    public void draw(Graphics2D g) {
        for (MyShape shape : shapeList) {
            shape.draw(g);
        }
    }

    public void fireChange() {
        this.setChanged();
        this.notifyObservers();
    }
}
