package org.example.controller.action;

import org.example.controller.state.UndoMachine;
import org.example.model.Factory.ShapeState;
import org.example.model.Factory.ShapeType;
import org.example.model.Model;
import org.example.model.MyShape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionDrawTest {

    private Model model;
    private ShapeState shapeState;
    private ActionDraw draw;
    private UndoMachine undoMachine;

    @BeforeEach
    void setUp() {
        model = new Model();
        shapeState = new ShapeState(ShapeType.RECTANGULAR_SHAPE, Color.RED, true);
        draw = new ActionDraw(model, shapeState);
        undoMachine = new UndoMachine();
    }

    private void dragShape(int x1, int y1, int x2, int y2) {
        draw.mousePressed(new Point(x1, y1));
        draw.mouseDragged(new Point(x2, y2));
        draw.mouseReleased(new Point(x2, y2));
    }

    @Test
    void dragCreatesShapeAndReturnsCommand() {
        dragShape(10, 10, 60, 50);

        assertEquals(1, model.getShapes().size());
        AppAction command = draw.takeCompletedAction();
        assertNotNull(command, "Протяжка должна дать команду для истории undo");
    }

    @Test
    void clickWithoutDragLeavesNoShape() {
        draw.mousePressed(new Point(20, 20));
        draw.mouseReleased(new Point(20, 20));

        assertTrue(model.getShapes().isEmpty(), "Фигура нулевого размера не должна остаться на холсте");
        assertNull(draw.takeCompletedAction());
    }

    @Test
    void undoRemovesShapeAndRedoReturnsIt() {
        dragShape(10, 10, 60, 50);
        MyShape drawn = model.getLastShape();
        undoMachine.add(draw.takeCompletedAction());

        undoMachine.executeUndo();
        assertTrue(model.getShapes().isEmpty());

        undoMachine.executeRedo();
        assertEquals(1, model.getShapes().size());
        assertTrue(model.getShapes().contains(drawn), "Redo должен вернуть ту же самую фигуру");
    }

    @Test
    void redoDoesNotDuplicateShape() {
        dragShape(10, 10, 60, 50);
        undoMachine.add(draw.takeCompletedAction());

        undoMachine.executeUndo();
        undoMachine.executeRedo();
        undoMachine.executeRedo();

        assertEquals(1, model.getShapes().size());
    }

    @Test
    void severalShapesAreUndoneAndRedoneInOrder() {
        dragShape(10, 10, 40, 40);
        MyShape first = model.getLastShape();
        undoMachine.add(draw.takeCompletedAction());

        dragShape(50, 50, 90, 90);
        MyShape second = model.getLastShape();
        undoMachine.add(draw.takeCompletedAction());

        undoMachine.executeUndo();
        assertEquals(1, model.getShapes().size());
        assertFalse(model.getShapes().contains(second));

        undoMachine.executeUndo();
        assertTrue(model.getShapes().isEmpty());

        undoMachine.executeRedo();
        assertEquals(1, model.getShapes().size());
        assertEquals(first, model.getShapes().get(0));

        undoMachine.executeRedo();
        assertEquals(2, model.getShapes().size());
        assertEquals(second, model.getShapes().get(1));
    }

    @Test
    void toolCanDrawAgainAfterUndoEntryWasTaken() {
        dragShape(10, 10, 40, 40);
        undoMachine.add(draw.takeCompletedAction());
        dragShape(50, 50, 80, 80);

        assertEquals(2, model.getShapes().size());
        assertNotNull(draw.takeCompletedAction());
    }

    @Test
    void changingShapeStateIsUndoable() {
        dragShape(10, 10, 60, 50);
        MyShape shape = model.getLastShape();
        double beforeX = shape.getFrameShape().getBounds2D().getX();

        MyShape snapshot = shape.cloneShape();
        shape.translate(30, 0);
        AppAction change = new ActionChangeShape(model, shape, snapshot, shape.cloneShape());

        change.unexecute();
        assertEquals(beforeX, shape.getFrameShape().getBounds2D().getX(), 0.001);

        change.execute();
        assertEquals(beforeX + 30, shape.getFrameShape().getBounds2D().getX(), 0.001);
    }
}
