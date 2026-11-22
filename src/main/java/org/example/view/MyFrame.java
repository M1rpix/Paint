package org.example.view;

import javax.swing.JFrame;

public class MyFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private MyPanel panel;

    public MyFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        // setVisible(true) отсюда убран: окно показывается в Controller
        // уже после того, как добавлены панель, меню и тулбар.
    }

    public void setPanel(MyPanel panel) {
        this.panel = panel;
        add(panel);
    }
}
