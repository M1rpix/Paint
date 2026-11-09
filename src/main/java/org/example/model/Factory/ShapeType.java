package org.example.model.Factory;

import org.example.model.MyShape;
import org.example.model.fill.FillBehavior;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public enum ShapeType {
    RECTANGULAR_SHAPE {
        @Override
        public MyShape createShape(Color color, FillBehavior fillBehavior) {
            return new MyShape(color, new Rectangle2D.Double(), fillBehavior);
        }
    };

    public abstract MyShape createShape(Color color, FillBehavior fillBehavior);
}
