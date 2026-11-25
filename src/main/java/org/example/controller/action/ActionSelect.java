package org.example.controller.action;

import org.example.model.Model;
import org.example.model.MyShape;
import org.example.view.SelectionPainter;

import java.awt.Point;
import java.awt.geom.Point2D;

public class ActionSelect implements AppAction {

    private final Model model;
    private MyShape draggedShape;
    private MyShape beforeSnapshot;
    private AppAction pendingChange;
    private Point lastPoint;
    private boolean rotating;
    private double rotationOffset;
    private boolean enabled = true;

    public ActionSelect(Model model) {
        this.model = model;
    }

    @Override
    public void mousePressed(Point point) {
        if (!enabled) {
            return;
        }
        lastPoint = point;
        MyShape selected = model.getSelectedShape();

        if (selected != null && SelectionPainter.rotateHandleBounds(selected).contains(point)) {
            rotating = true;
            draggedShape = selected;
            beforeSnapshot = selected.cloneShape();
            rotationOffset = angleToHandle(selected, point)
                    - angleToHandle(selected, SelectionPainter.rotateHandleCenter(selected));
            return;
        }

        rotating = false;
        draggedShape = model.findShape(point);
        beforeSnapshot = draggedShape == null ? null : draggedShape.cloneShape();
        model.setSelectedShape(draggedShape);
    }

    @Override
    public void mouseDragged(Point point) {
        if (draggedShape == null) {
            return;
        }
        if (rotating) {
            applyRotation(draggedShape, point);
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
        if (draggedShape == null || beforeSnapshot == null || draggedShape.sameState(beforeSnapshot)) {
            pendingChange = null;
        } else {
            pendingChange = new ActionChangeShape(model, draggedShape, beforeSnapshot, draggedShape.cloneShape());
        }
        draggedShape = null;
        beforeSnapshot = null;
        lastPoint = null;
        rotating = false;
    }

    /**
     * Возвращает команду изменения фигуры, чтобы её можно было положить в историю undo.
     * Забирает команду один раз — повторный вызов вернёт null.
     */
    public AppAction createChangeAction() {
        AppAction change = pendingChange;
        pendingChange = null;
        return change;
    }

    @Override
    public void execute() {
    }

    @Override
    public void unexecute() {
    }

    @Override
    public AppAction cloneAction() {
        ActionSelect action = new ActionSelect(model);
        action.draggedShape = draggedShape;
        action.beforeSnapshot = beforeSnapshot;
        action.rotating = rotating;
        action.rotationOffset = rotationOffset;
        action.enabled = enabled;
        return action;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            draggedShape = null;
            beforeSnapshot = null;
            pendingChange = null;
            lastPoint = null;
            rotating = false;
        }
    }

    @Override
    public String getName() {
        return "Выделять";
    }

    private double angleToHandle(MyShape shape, Point2D point) {
        Point2D center = shape.getCenter();
        return Math.atan2(point.getY() - center.getY(), point.getX() - center.getX());
    }

    private void applyRotation(MyShape shape, Point2D point) {
        if (beforeSnapshot == null) {
            return;
        }
        Point2D center = beforeSnapshot.getCenter();
        Point2D pivot = SelectionPainter.rotateHandleCenter(beforeSnapshot);
        double pointerAngle = Math.atan2(point.getY() - center.getY(), point.getX() - center.getX());
        double pivotAngle = Math.atan2(pivot.getY() - center.getY(), pivot.getX() - center.getX());
        shape.copyStateFrom(beforeSnapshot);
        shape.rotate(Math.toDegrees(pointerAngle - rotationOffset - pivotAngle));
    }
}
