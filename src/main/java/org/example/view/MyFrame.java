package org.example.view;

import javax.swing.JFrame;

public class MyFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private MyPanel panel;

    public MyFrame() {
        super("MVC Paint");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
    }

    public void setPanel(MyPanel panel) {
        this.panel = panel;
        add(panel);
    }
}
