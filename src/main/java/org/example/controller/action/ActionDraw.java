package org.example.controller.action;

import org.example.model.Factory.ShapeState;
import org.example.model.Model;
import org.example.model.MyShape;
import org.example.model.ShapeFactory;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * Стратегия «рисование фигуры». Один и тот же объект обслуживает много фигур подряд:
 * после отпускания кнопки мыши готовая команда забирается через {@link #takeCompletedAction()},
 * и цикл начинается заново.
 */
public class ActionDraw implements AppAction {

    private final Model model;
    private final ShapeFactory shapeFactory;
    private final ShapeState shapeState;
    private MyShape sampleShape;
    private MyShape drawableShape;
    private Point firstPoint;
    private Point secondPoint;
    private boolean mousePressed;
    private boolean hasCompletedShape;
    private boolean enabled = true;

    public ActionDraw(Model model, ShapeState shapeState) {
        this.model = model;
        this.shapeState = shapeState;
        this.shapeFactory = ShapeFactory.getInstance();
    }

    @Override
    public void mousePressed(Point point) {
        if (!enabled || mousePressed) {
            return;
        }
        secondPoint = point;
        firstPoint = point;
        sampleShape = shapeFactory.createShape(shapeState);
        drawableShape = sampleShape;
        model.setDrawableShape(sampleShape);
        model.addShape(sampleShape);
        mousePressed = true;
        model.fireChange();
    }

    @Override
    public void mouseDragged(Point point) {
        if (!enabled || !mousePressed) {
            return;
        }
        firstPoint = point;
        sampleShape.setFrame(firstPoint, secondPoint);
        model.fireChange();
    }

    @Override
    public void mouseReleased(Point point) {
        if (!mousePressed) {
            return;
        }
        sampleShape.setFrame(point, secondPoint);
        model.setDrawableShape(null);
        mousePressed = false;

        if (hasContent()) {
            hasCompletedShape = true;
        } else {
            // Клик без протяжки — фигуру нулевого размера на холсте не оставляем.
            model.removeShape(drawableShape);
            drawableShape = null;
        }
        model.fireChange();
    }

    /**
     * Забирает команду для стека undo, если последний жест действительно нарисовал фигуру.
     * После вызова действие готово к следующей фигуре.
     */
    public AppAction takeCompletedAction() {
        if (!hasCompletedShape) {
            return null;
        }
        hasCompletedShape = false;
        ActionDraw command = snapshot();
        reset();
        return command;
    }

    @Override
    public void execute() {
        if (drawableShape != null && !model.getShapes().contains(drawableShape)) {
            model.addShape(drawableShape);
        }
        model.fireChange();
    }

    @Override
    public void unexecute() {
        if (drawableShape != null) {
            model.removeShape(drawableShape);
        }
        model.fireChange();
    }

    @Override
    public AppAction cloneAction() {
        return snapshot();
    }

    private ActionDraw snapshot() {
        ActionDraw action = new ActionDraw(model, shapeState);
        action.sampleShape = sampleShape;
        action.drawableShape = drawableShape;
        action.firstPoint = firstPoint;
        action.secondPoint = secondPoint;
        action.enabled = enabled;
        return action;
    }

    private void reset() {
        sampleShape = null;
        drawableShape = null;
        firstPoint = null;
        secondPoint = null;
        mousePressed = false;
        hasCompletedShape = false;
        model.setDrawableShape(null);
    }

    private boolean hasContent() {
        if (drawableShape == null) {
            return false;
        }
        Rectangle2D bounds = drawableShape.getShape().getBounds2D();
        return bounds.getWidth() >= 1 && bounds.getHeight() >= 1;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            model.setDrawableShape(null);
            mousePressed = false;
            hasCompletedShape = false;
        }
    }

    @Override
    public String getName() {
        return "Рисовать";
    }
}
