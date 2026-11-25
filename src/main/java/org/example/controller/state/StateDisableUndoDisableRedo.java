package org.example.controller.state;

import org.example.controller.action.AppAction;

import java.util.LinkedList;

public class StateDisableUndoDisableRedo extends UndoRedoState {

    protected StateDisableUndoDisableRedo(LinkedList<AppAction> undoActivityList, LinkedList<AppAction> redoActivityList) {
        super(undoActivityList, redoActivityList);
    }

    public static StateDisableUndoDisableRedo create(UndoRedoState state) {
        return new StateDisableUndoDisableRedo(
                state.getUndoActivityList(), state.getRedoActivityList());
    }

    @Override
    public UndoRedoState undo() {
        return this;
    }

    @Override
    public UndoRedoState redo() {
        return this;
    }
}
