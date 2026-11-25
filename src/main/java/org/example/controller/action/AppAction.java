package org.example.controller.action;

import java.awt.Point;

public interface AppAction {

    void execute();

    void unexecute();

    AppAction cloneAction();

    default void mousePressed(Point point) {
    }

    default void mouseDragged(Point point) {
    }

    default void mouseReleased(Point point) {
    }

    default boolean isEnabled() {
        return true;
    }

    default void setEnabled(boolean enabled) {
    }

    default String getName() {
        return getClass().getSimpleName();
    }
}