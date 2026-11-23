package org.example.controller.action;

import org.example.model.Model;
import org.example.model.MyShape;
import org.example.model.ShapeFactory;
import org.example.model.Factory.ShapeState;

import java.awt.Point;

public class ActionDraw implements AppAction {

    private final Model model;
    private final ShapeState shapeState;
    private final ShapeFactory shapeFactory;
    private Point firstPoint;

    public ActionDraw(Model model, ShapeState shapeState) {
        this.model = model;
        this.shapeState = shapeState;
        this.shapeFactory = ShapeFactory.getInstance();
    }

    @Override
    public void mousePressed(Point point) {
        firstPoint = point;
        MyShape shape = shapeFactory.createShape(shapeState);
        shape.setFrame(point, point);
        model.setDrawableShape(shape);
        model.addShape(shape);
    }

    @Override
    public void mouseDragged(Point point) {
        if (firstPoint == null) {
            return;
        }
        model.changeShape(firstPoint, point);
    }

    @Override
    public void mouseReleased(Point point) {
        if (firstPoint != null) {
            model.changeShape(firstPoint, point);
        }
        firstPoint = null;
        model.setDrawableShape(null);
    }

    @Override
    public String getName() {
        return "Рисовать";
    }
}
