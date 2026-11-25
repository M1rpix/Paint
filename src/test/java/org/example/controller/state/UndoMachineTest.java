package org.example.controller.state;

import org.example.controller.action.AppAction;
import org.junit.jupiter.api.Test;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UndoMachineTest {

    /** Простейшая команда: только запоминает, что её выполнили или отменили. */
    private static class RecordAction implements AppAction {
        private final String name;
        private final List<String> log;

        RecordAction(String name, List<String> log) {
            this.name = name;
            this.log = log;
        }

        @Override
        public void execute() {
            log.add("do:" + name);
        }

        @Override
        public void unexecute() {
            log.add("undo:" + name);
        }

        @Override
        public AppAction cloneAction() {
            return new RecordAction(name, log);
        }
    }

    private final List<String> log = new ArrayList<>();

    private RecordAction action(String name) {
        return new RecordAction(name, log);
    }

    @Test
    void newActionEnablesUndoOnly() {
        UndoMachine machine = new UndoMachine();
        assertFalse(machine.isEnableUndo());
        assertFalse(machine.isEnableRedo());

        machine.add(action("A"));

        assertTrue(machine.isEnableUndo());
        assertFalse(machine.isEnableRedo());
        assertEquals(1, machine.getUndoSize());
    }

    @Test
    void undoAndRedoMoveActionBetweenHistories() {
        UndoMachine machine = new UndoMachine();
        machine.add(action("A"));

        machine.executeUndo();

        assertEquals(List.of("undo:A"), log);
        assertFalse(machine.isEnableUndo());
        assertTrue(machine.isEnableRedo());
        assertEquals(0, machine.getUndoSize());
        assertEquals(1, machine.getRedoSize());

        machine.executeRedo();

        assertEquals(List.of("undo:A", "do:A"), log);
        assertTrue(machine.isEnableUndo());
        assertFalse(machine.isEnableRedo());
    }

    @Test
    void undoAndRedoKeepOrderForSeveralActions() {
        UndoMachine machine = new UndoMachine();
        machine.add(action("A"));
        machine.add(action("B"));

        machine.executeUndo();
        machine.executeUndo();
        machine.executeRedo();
        machine.executeRedo();

        assertEquals(List.of("undo:B", "undo:A", "do:A", "do:B"), log);
    }

    @Test
    void newActionDropsRedoHistory() {
        UndoMachine machine = new UndoMachine();
        machine.add(action("A"));
        machine.executeUndo();
        assertTrue(machine.isEnableRedo());

        machine.add(action("B"));

        assertFalse(machine.isEnableRedo());
        assertTrue(machine.isEnableUndo());
    }

    @Test
    void undoOnEmptyHistoryChangesNothing() {
        UndoMachine machine = new UndoMachine();
        machine.executeUndo();
        machine.executeRedo();

        assertTrue(log.isEmpty());
        assertFalse(machine.isEnableUndo());
        assertFalse(machine.isEnableRedo());
    }

    @Test
    void historyKeepsLastFiftyActions() {
        UndoMachine machine = new UndoMachine();
        for (int i = 0; i < 60; i++) {
            machine.add(action("A" + i));
        }
        assertEquals(50, machine.getUndoSize());

        machine.executeUndo();
        assertEquals(List.of("undo:A59"), log);
    }

    @Test
    void resetClearsBothHistoriesAndButtons() {
        UndoMachine machine = new UndoMachine();
        TestButton undoButton = new TestButton();
        TestButton redoButton = new TestButton();
        machine.setButtons(undoButton, redoButton);

        machine.add(action("A"));
        machine.executeUndo();
        machine.updateButtons();
        assertFalse(undoButton.isEnabled());
        assertTrue(redoButton.isEnabled());

        machine.reset();

        assertFalse(machine.isEnableUndo());
        assertFalse(machine.isEnableRedo());
        assertEquals(0, machine.getUndoSize());
        assertEquals(0, machine.getRedoSize());
    }

    @Test
    void updateButtonsFollowsHistories() {
        UndoMachine machine = new UndoMachine();
        TestButton undoButton = new TestButton();
        TestButton redoButton = new TestButton();
        machine.setButtons(undoButton, redoButton);

        assertFalse(undoButton.isEnabled());
        assertFalse(redoButton.isEnabled());

        machine.add(action("A"));
        assertTrue(undoButton.isEnabled());
        assertFalse(redoButton.isEnabled());

        machine.executeUndo();
        assertFalse(undoButton.isEnabled());
        assertTrue(redoButton.isEnabled());
    }

    private static class TestButton extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent event) {
        }
    }
}
