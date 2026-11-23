package org.example.view.menu;

import org.example.model.Factory.ShapeState;
import org.example.model.Factory.ShapeType;

import javax.swing.JRadioButtonMenuItem;

public class SwitchShape implements AppCommand {
    private final ShapeState shapeState;
    private final ShapeType shapeType;
    private final JRadioButtonMenuItem radioButton;

    public SwitchShape(ShapeState shapeState, ShapeType shapeType, JRadioButtonMenuItem radioButton) {
        this.shapeState = shapeState;
        this.shapeType = shapeType;
        this.radioButton = radioButton;
    }

    @Override
    public void execute() {
        shapeState.setShapeType(shapeType);
        if (radioButton != null) {
            radioButton.setSelected(true);
        }
    }
}
