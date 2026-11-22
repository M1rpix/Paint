package org.example;

import org.example.controller.Controller;

import javax.swing.SwingUtilities;

public class BootClass {
    public static void main(String[] args) {
        // Swing не потокобезопасен: GUI создаём в потоке диспетчеризации событий.
        SwingUtilities.invokeLater(Controller::getInstance);
    }
}