package org.example.controller.action;

import org.example.model.Model;
import org.example.model.MyShape;
import org.example.view.SelectionPainter;

import java.awt.Point;
import java.awt.geom.Point2D;

public class ActionSelect implements AppAction {

    private final Model model;
    private MyShape draggedShape;
    private Point lastPoint;
    private boolean rotating;
    private double lastAngle;

    public ActionSelect(Model model) {
        this.model = model;
    }

    @Override
    public void mousePressed(Point point) {
        lastPoint = point;
        MyShape selected = model.getSelectedShape();

        if (selected != null && SelectionPainter.rotateHandleBounds(selected).contains(point)) {
            rotating = true;
            draggedShape = selected;
            lastAngle = angleToHandle(selected, point);
            return;
        }

        rotating = false;
        draggedShape = model.findShape(point);
        model.setSelectedShape(draggedShape);
    }

    @Override
    public void mouseDragged(Point point) {
        if (draggedShape == null) {
            return;
        }
        if (rotating) {
            double angle = angleToHandle(draggedShape, point);
            draggedShape.rotate(Math.toDegrees(angle - lastAngle));
            lastAngle = angle;
            model.fireChange();
            return;
        }
        if (lastPoint != null) {
            draggedShape.translate(point.getX() - lastPoint.getX(), point.getY() - lastPoint.getY());
            lastPoint = point;
            model.fireChange();
        }
    }

    @Override
    public void mouseReleased(Point point) {
        draggedShape = null;
        lastPoint = null;
        rotating = false;
    }

    @Override
    public String getName() {
        return "Выделять";
    }

    private double angleToHandle(MyShape shape, Point2D point) {
        Point2D center = shape.getCenter();
        return Math.atan2(point.getY() - center.getY(), point.getX() - center.getX());
    }
}
