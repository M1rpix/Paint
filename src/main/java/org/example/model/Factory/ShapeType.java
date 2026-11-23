package org.example.model.Factory;

import org.example.model.MyShape;
import org.example.model.fill.FillBehavior;

import java.awt.Color;
import java.awt.Shape;

public enum ShapeType {
    RECTANGULAR_SHAPE {
        @Override
        public Shape createUnitShape() {
            return new java.awt.geom.Rectangle2D.Double(0, 0, 1, 1);
        }
    },
    ELLIPSE_SHAPE {
        @Override
        public Shape createUnitShape() {
            return new java.awt.geom.Ellipse2D.Double(0, 0, 1, 1);
        }
    };

    public abstract Shape createUnitShape();

    public MyShape createShape(FillBehavior fillBehavior, Color color) {
        return new MyShape(createUnitShape(), color, fillBehavior);
    }
}
