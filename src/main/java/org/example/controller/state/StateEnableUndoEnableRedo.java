package org.example.controller.state;

import org.example.controller.action.AppAction;

import java.util.LinkedList;

public class StateEnableUndoEnableRedo extends UndoRedoState {

    protected StateEnableUndoEnableRedo(LinkedList<AppAction> undoActivityList, LinkedList<AppAction> redoActivityList) {
        super(undoActivityList, redoActivityList);
    }

    public static StateEnableUndoEnableRedo create(UndoRedoState state) {
        return new StateEnableUndoEnableRedo(
                state.getUndoActivityList(), state.getRedoActivityList());
    }

    @Override
    public UndoRedoState undo() {
        undoStep();
        return nextUndoState();
    }

    @Override
    public UndoRedoState redo() {
        redoStep();
        return nextRedoState();
    }
}
