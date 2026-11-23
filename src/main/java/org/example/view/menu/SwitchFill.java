package org.example.view.menu;

import org.example.model.Factory.ShapeState;

import javax.swing.JRadioButtonMenuItem;

public class SwitchFill implements AppCommand {
    private final boolean fill;
    private final ShapeState shapeState;
    private final JRadioButtonMenuItem fillItem;
    private final JRadioButtonMenuItem noFillItem;

    public SwitchFill(boolean fill,
                      ShapeState shapeState,
                      JRadioButtonMenuItem fillItem,
                      JRadioButtonMenuItem noFillItem) {
        this.fill = fill;
        this.shapeState = shapeState;
        this.fillItem = fillItem;
        this.noFillItem = noFillItem;
    }

    @Override
    public void execute() {
        shapeState.setFill(fill);
        if (fillItem != null) {
            fillItem.setSelected(fill);
        }
        if (noFillItem != null) {
            noFillItem.setSelected(!fill);
        }
    }
}
