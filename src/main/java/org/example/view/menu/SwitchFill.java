package org.example.view.menu;

import org.example.model.Factory.ShapeState;

import javax.swing.JRadioButtonMenuItem;

/**
 * Переключает режим заливки в ShapeState и синхронизирует
 * состояние пунктов меню "С заливкой"/"Без заливки".
 */
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
        // Пункты меню могут быть ещё не созданы, если команду дёрнули из тулбара.
        if (fillItem != null) {
            fillItem.setSelected(fill);
        }
        if (noFillItem != null) {
            noFillItem.setSelected(!fill);
        }
    }
}
