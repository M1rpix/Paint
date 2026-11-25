package org.example.model;

import org.example.model.fill.Fill;
import org.example.model.fill.FillBehavior;
import org.example.model.geometry.ShapeGeometry;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

public class MyShape {
    private final Color color;
    private final FillBehavior fillBehavior;
    private final Stroke stroke;
    private Shape unitShape;
    private AffineTransform transform;
    private double rotationRadians;

    public MyShape(Shape unitShape) {
        this(unitShape, Color.GRAY, new Fill());
    }

    public MyShape(Shape unitShape, Color color, FillBehavior fillBehavior) {
        this.unitShape = unitShape;
        this.color = color;
        this.fillBehavior = fillBehavior;
        this.stroke = new BasicStroke(1.5f);
        this.transform = new AffineTransform();
    }

    public Color getColor() {
        return color;
    }

    public FillBehavior getFillBehavior() {
        return fillBehavior;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public double getRotationDegrees() {
        return Math.toDegrees(rotationRadians);
    }

    public Point2D getCenter() {
        Rectangle2D bounds = getShape().getBounds2D();
        return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }

    public Shape getShape() {
        return ShapeGeometry.transform(unitShape, fullTransform());
    }

    public Shape getFrameShape() {
        return ShapeGeometry.transform(unitShape, transform);
    }

    public Shape getBoundsShape() {
        Shape shape = getShape();
        if (rotationRadians == 0) {
            return shape;
        }
        Rectangle2D bounds = shape.getBounds2D();
        return new Rectangle2D.Double(
                bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    public void setUnitShape(Shape unitShape) {
        this.unitShape = unitShape;
    }

    public void translate(double dx, double dy) {
        transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
    }

    public void setOffset(double x, double y) {
        transform.setToTranslation(x, y);
    }

    public void setFrame(Point2D first, Point2D second) {
        double width = Math.abs(second.getX() - first.getX());
        double height = Math.abs(second.getY() - first.getY());
        AffineTransform affine = new AffineTransform();
        affine.translate(Math.min(first.getX(), second.getX()), Math.min(first.getY(), second.getY()));
        affine.scale(width, height);
        transform = affine;
        rotationRadians = 0;
    }

    public void rotate(double degrees) {
        rotationRadians = normalizeRadians(rotationRadians + Math.toRadians(degrees));
    }

    public boolean contains(Point2D point) {
        return getShape().contains(point);
    }

    public MyShape cloneShape() {
        MyShape copy = new MyShape(unitShape, color, fillBehavior);
        copy.transform = new AffineTransform(transform);
        copy.rotationRadians = rotationRadians;
        return copy;
    }

    public void copyStateFrom(MyShape snapshot) {
        this.unitShape = snapshot.unitShape;
        this.transform = new AffineTransform(snapshot.transform);
        this.rotationRadians = snapshot.rotationRadians;
    }

    public boolean sameState(MyShape other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (Math.abs(rotationRadians - other.rotationRadians) > 1e-9) {
            return false;
        }
        double[] m1 = new double[6];
        double[] m2 = new double[6];
        transform.getMatrix(m1);
        other.transform.getMatrix(m2);
        return Arrays.equals(m1, m2);
    }

    public void draw(Graphics2D g) {
        Stroke oldStroke = g.getStroke();
        g.setStroke(stroke);
        fillBehavior.draw(g, getShape(), color);
        g.setStroke(oldStroke);
    }

    private AffineTransform fullTransform() {
        if (rotationRadians == 0) {
            return new AffineTransform(transform);
        }
        Rectangle2D frame = ShapeGeometry.transform(unitShape, transform).getBounds2D();
        AffineTransform rotation = ShapeGeometry.rotation(
                new Point2D.Double(frame.getCenterX(), frame.getCenterY()), getRotationDegrees());
        rotation.concatenate(transform);
        return rotation;
    }

    private static double normalizeRadians(double radians) {
        double fullTurn = 2 * Math.PI;
        double normalized = radians % fullTurn;
        if (normalized > Math.PI) {
            normalized -= fullTurn;
        } else if (normalized <= -Math.PI) {
            normalized += fullTurn;
        }
        return normalized;
    }
}