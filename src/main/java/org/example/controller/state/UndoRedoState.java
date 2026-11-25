package org.example.controller.state;

import org.example.controller.action.AppAction;

import java.util.LinkedList;

public abstract class UndoRedoState {
    private static final int MAX_UNDO = 50;

    private final LinkedList<AppAction> undoActivityList;
    private final LinkedList<AppAction> redoActivityList;

    protected UndoRedoState(LinkedList<AppAction> undoActivityList, LinkedList<AppAction> redoActivityList) {
        this.undoActivityList = undoActivityList;
        this.redoActivityList = redoActivityList;
    }

    public LinkedList<AppAction> getUndoActivityList() {
        return undoActivityList;
    }

    public LinkedList<AppAction> getRedoActivityList() {
        return redoActivityList;
    }

    public abstract UndoRedoState undo();

    public abstract UndoRedoState redo();

    public void clearHistory() {
        redoActivityList.clear();
    }

    public void addAction(AppAction action) {
        if (undoActivityList.size() >= MAX_UNDO) {
            undoActivityList.pollFirst();
        }
        undoActivityList.addLast(action);
    }

    public UndoRedoState reset() {
        undoActivityList.clear();
        redoActivityList.clear();
        return StateDisableUndoDisableRedo.create(this);
    }

    protected void undoStep() {
        AppAction action = undoActivityList.pollLast();
        if (action != null) {
            redoActivityList.addLast(action);
            action.unexecute();
        }
    }

    protected void redoStep() {
        AppAction action = redoActivityList.pollLast();
        if (action != null) {
            undoActivityList.addLast(action);
            action.execute();
        }
    }

    protected UndoRedoState nextUndoState() {
        return resolve();
    }

    protected UndoRedoState nextRedoState() {
        return resolve();
    }

    /**
     * Состояние выбирается по наполненности истории: может ли пользователь
     * отменить и повторить действие прямо сейчас.
     */
    private UndoRedoState resolve() {
        boolean canUndo = !undoActivityList.isEmpty();
        boolean canRedo = !redoActivityList.isEmpty();
        if (canUndo && canRedo) {
            return StateEnableUndoEnableRedo.create(this);
        }
        if (canUndo) {
            return StateEnableUndoDisableRedo.create(this);
        }
        if (canRedo) {
            return StateDisableUndoEnableRedo.create(this);
        }
        return StateDisableUndoDisableRedo.create(this);
    }
}
