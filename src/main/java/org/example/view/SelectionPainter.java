package org.example.view;

import org.example.model.MyShape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public final class SelectionPainter {

    private static final Color ACCENT = new Color(0x2F, 0x6F, 0xED);
    private static final double HANDLE_SIZE = 7.0;
    private static final double HANDLE_OFFSET = 12.0;
    private static final float[] DASH = {4f, 3f};

    private SelectionPainter() {
    }

    public static Point2D rotateHandleCenter(MyShape shape) {
        Rectangle2D bounds = shape.getBoundsShape().getBounds2D();
        return new Point2D.Double(bounds.getCenterX(), bounds.getMinY() - HANDLE_OFFSET);
    }

    public static Rectangle2D rotateHandleBounds(MyShape shape) {
        Point2D center = rotateHandleCenter(shape);
        double half = HANDLE_SIZE / 2.0;
        return new Rectangle2D.Double(
                center.getX() - half, center.getY() - half, HANDLE_SIZE, HANDLE_SIZE);
    }

    public static void paint(Graphics2D g, MyShape shape) {
        if (shape == null) {
            return;
        }
        Stroke oldStroke = g.getStroke();
        Paint oldPaint = g.getPaint();

        Rectangle2D bounds = shape.getBoundsShape().getBounds2D();
        Rectangle2D handle = rotateHandleBounds(shape);

        g.setPaint(ACCENT);
        g.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, DASH, 0f));
        g.draw(bounds);

        g.setStroke(new BasicStroke(1f));
        g.draw(new Line2D.Double(bounds.getCenterX(), bounds.getMinY(), handle.getCenterX(), handle.getCenterY()));

        g.setPaint(Color.WHITE);
        g.fill(handle);
        g.setPaint(ACCENT);
        g.draw(handle);

        g.setStroke(oldStroke);
        g.setPaint(oldPaint);
    }
}
