package org.example.controller.state;

import org.example.controller.action.AppAction;

import javax.swing.Action;
import java.util.LinkedList;

public class UndoMachine {
    private UndoRedoState undoRedoState;
    private Action undoAction;
    private Action redoAction;

    public UndoMachine() {
        LinkedList<AppAction> undoList = new LinkedList<>();
        LinkedList<AppAction> redoList = new LinkedList<>();
        undoRedoState = new StateDisableUndoDisableRedo(undoList, redoList);
    }

    public void executeRedo() {
        undoRedoState = undoRedoState.redo();
        updateButtons();
    }

    public void executeUndo() {
        undoRedoState = undoRedoState.undo();
        updateButtons();
    }

    public boolean isEnableUndo() {
        return !undoRedoState.getUndoActivityList().isEmpty();
    }

    public boolean isEnableRedo() {
        return !undoRedoState.getRedoActivityList().isEmpty();
    }

    public int getUndoSize() {
        return undoRedoState.getUndoActivityList().size();
    }

    public int getRedoSize() {
        return undoRedoState.getRedoActivityList().size();
    }

    public void add(AppAction action) {
        undoRedoState.clearHistory();
        undoRedoState.addAction(action);
        // После нового действия redo-история пуста, а undo пополнилась.
        undoRedoState = undoRedoState.nextUndoState();
        updateButtons();
    }

    public void reset() {
        undoRedoState = undoRedoState.reset();
        updateButtons();
    }

    public void setButtons(Action undoAction, Action redoAction) {
        this.undoAction = undoAction;
        this.redoAction = redoAction;
        updateButtons();
    }

    public void updateButtons() {
        if (undoAction != null) {
            undoAction.setEnabled(isEnableUndo());
        }
        if (redoAction != null) {
            redoAction.setEnabled(isEnableRedo());
        }
    }
}
