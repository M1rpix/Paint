package org.example.model.geometry;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public final class ShapeGeometry {

    public static final AffineTransform IDENTITY = new AffineTransform();

    private ShapeGeometry() {
    }

    public static Point2D unitCenter(Shape unitShape) {
        Rectangle2D bounds = unitShape.getBounds2D();
        return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }

    public static Shape transform(Shape unitShape, AffineTransform transform) {
        return transform.createTransformedShape(unitShape);
    }

    public static Point2D inversePoint(Point2D point, AffineTransform transform) {
        try {
            return transform.createInverse().transform(point, null);
        } catch (java.awt.geom.NoninvertibleTransformException e) {
            throw new IllegalStateException(e);
        }
    }

    public static AffineTransform rotation(Point2D pivot, double degrees) {
        AffineTransform transform = AffineTransform.getTranslateInstance(pivot.getX(), pivot.getY());
        transform.rotate(Math.toRadians(degrees));
        transform.translate(-pivot.getX(), -pivot.getY());
        return transform;
    }
}
