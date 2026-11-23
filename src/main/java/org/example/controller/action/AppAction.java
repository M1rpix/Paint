package org.example.controller.action;

import java.awt.Point;

public interface AppAction {

    default void mousePressed(Point point) {
    }

    default void mouseDragged(Point point) {
    }

    default void mouseReleased(Point point) {
    }

    default String getName() {
        return getClass().getSimpleName();
    }
}
