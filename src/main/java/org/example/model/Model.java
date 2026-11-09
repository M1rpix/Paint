package org.example.model;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Model extends Observable {
    private MyShape draweableShape;
    private final List<MyShape> shapeList;

    public Model() {
        this.shapeList = new ArrayList<>();
    }

    public void setMyShape(MyShape myShape) {
        this.draweableShape = myShape;
    }

    public void addShape(MyShape shape){
        draweableShape = shape;
        shapeList.add(shape);
    }

    public void changeShape(Point2D x, Point2D y) {
        draweableShape.setFrame(x, y);
        this.setChanged();
        this.notifyObservers();
    }

    public void draw(Graphics2D g) {
        draweableShape.draw(g);
        for (MyShape shape : shapeList) {
            shape.draw(g);
        }
    }
}
