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

public class MyShape {
    private final Color color;
    private final FillBehavior fillBehavior;
    private final Stroke stroke;
    private final AffineTransform rotation;
    private Shape unitShape;
    private AffineTransform transform;

    public MyShape(Shape unitShape) {
        this(unitShape, Color.GRAY, new Fill());
    }

    public MyShape(Shape unitShape, Color color, FillBehavior fillBehavior) {
        this.unitShape = unitShape;
        this.color = color;
        this.fillBehavior = fillBehavior;
        this.stroke = new BasicStroke(1.5f);
        this.rotation = new AffineTransform();
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

    public AffineTransform getRotation() {
        return rotation;
    }

    public Point2D getCenter() {
        Rectangle2D bounds = getShape().getBounds2D();
        return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }

    public Shape getShape() {
        AffineTransform full = new AffineTransform(transform);
        full.concatenate(rotation);
        return ShapeGeometry.transform(unitShape, full);
    }

    public Shape getBoundsShape() {
        Shape shape = getShape();
        if (rotation.isIdentity()) {
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
        rotation.setToIdentity();
        AffineTransform moved = AffineTransform.getTranslateInstance(dx, dy);
        moved.concatenate(transform);
        transform = moved;
    }

    public void setFrame(Point2D first, Point2D second) {
        double width = Math.abs(second.getX() - first.getX());
        double height = Math.abs(second.getY() - first.getY());
        AffineTransform affine = new AffineTransform();
        affine.translate(Math.min(first.getX(), second.getX()), Math.min(first.getY(), second.getY()));
        affine.scale(width, height);
        transform = affine;
        rotation.setToIdentity();
    }

    public void rotate(double degrees) {
        Point2D pivot = getCenter();
        AffineTransform delta = ShapeGeometry.rotation(pivot, degrees);
        AffineTransform combined = new AffineTransform(delta);
        combined.concatenate(rotation);
        combined.concatenate(transform);
        Rectangle2D bounds = ShapeGeometry.transform(unitShape, combined).getBounds2D();
        combined.preConcatenate(AffineTransform.getTranslateInstance(
                Math.rint(bounds.getX()) - bounds.getX(),
                Math.rint(bounds.getY()) - bounds.getY()));
        transform = combined;
        rotation.setToIdentity();
    }

    public boolean contains(Point2D point) {
        return getShape().contains(point);
    }

    public void draw(Graphics2D g) {
        Stroke oldStroke = g.getStroke();
        g.setStroke(stroke);
        fillBehavior.draw(g, getShape(), color);
        g.setStroke(oldStroke);
    }
}
