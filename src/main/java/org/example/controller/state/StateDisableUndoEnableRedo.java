package org.example.controller.state;

import org.example.controller.action.AppAction;

import java.util.LinkedList;

public class StateDisableUndoEnableRedo extends UndoRedoState {

    protected StateDisableUndoEnableRedo(LinkedList<AppAction> undoActivityList, LinkedList<AppAction> redoActivityList) {
        super(undoActivityList, redoActivityList);
    }

    public static StateDisableUndoEnableRedo create(UndoRedoState state) {
        return new StateDisableUndoEnableRedo(
                state.getUndoActivityList(), state.getRedoActivityList());
    }

    @Override
    public UndoRedoState undo() {
        return this;
    }

    @Override
    public UndoRedoState redo() {
        redoStep();
        return nextRedoState();
    }
}
