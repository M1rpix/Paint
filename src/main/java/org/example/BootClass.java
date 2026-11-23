package org.example;

import org.example.controller.Controller;

import javax.swing.SwingUtilities;

public class BootClass {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Controller::getInstance);
    }
}