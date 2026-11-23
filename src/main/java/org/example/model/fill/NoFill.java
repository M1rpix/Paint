package org.example.model.fill;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

public class NoFill implements FillBehavior {

    @Override
    public void draw(Graphics2D g, Shape shape, Color color) {
        Paint paint = g.getPaint();
        g.setPaint(color);
        g.draw(shape);
        g.setPaint(paint);
    }
}
