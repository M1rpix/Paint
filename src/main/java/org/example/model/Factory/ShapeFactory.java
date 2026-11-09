package org.example.model.Factory;

import org.example.model.MyShape;
import org.example.model.fill.Fill;
import org.example.model.fill.FillBehavior;
import org.example.model.fill.NoFill;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ShapeFactory {
    public MyShape createShape(ShapeType shapeType, Color color, boolean infill){
        FillBehavior fillBehavior = infill ? new Fill() : new NoFill();
        return shapeType.createShape(color, fillBehavior);
    }
}
