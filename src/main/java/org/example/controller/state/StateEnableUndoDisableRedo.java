package org.example.controller.state;

import org.example.controller.action.AppAction;

import java.util.LinkedList;

public class StateEnableUndoDisableRedo extends UndoRedoState {

    protected StateEnableUndoDisableRedo(LinkedList<AppAction> undoActivityList, LinkedList<AppAction> redoActivityList) {
        super(undoActivityList, redoActivityList);
    }

    public static StateEnableUndoDisableRedo create(UndoRedoState state) {
        return new StateEnableUndoDisableRedo(
                state.getUndoActivityList(), state.getRedoActivityList());
    }

    @Override
    public UndoRedoState undo() {
        undoStep();
        return nextUndoState();
    }

    @Override
    public UndoRedoState redo() {
        return this;
    }
}
