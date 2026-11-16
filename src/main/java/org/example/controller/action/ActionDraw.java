package org.example.controller.action;

import org.example.model.Factory.ShapeState;
import org.example.model.Model;
import org.example.model.MyShape;
import org.example.model.Factory.ShapeFactory;

import java.awt.*;

public class ActionDraw implements AppAction {
    private Point firstPoint;
    private Point secondPoint;
    private Model model;
    private ShapeFactory shapeFactory;

    private ShapeState state;

    public ActionDraw(Model model) {
        this.model = model;
        shapeFactory = ShapeFactory.getInstanse();
        model.setMyShape(shapeFactory.createDefaultShape());
    }

    @Override
    public void mousePressed(Point point) {
        firstPoint = point;
        MyShape shape = shapeFactory.createDefaultShape();
        model.setDrawableShape(shape);
        model.addShape(shape);
    }

    @Override
    public void mouseDragged(Point point) {
        secondPoint = point;
        model.changeShape(firstPoint, secondPoint);
    }
}