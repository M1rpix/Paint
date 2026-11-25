package org.example.controller.action;

import org.example.model.Model;
import org.example.model.MyShape;

/**
 * Команда изменения уже нарисованной фигуры: перемещение или поворот.
 * Хранит два снимка геометрии — «до» и «после», поэтому одинаково хорошо
 * выполняется и отменяется.
 */
public class ActionChangeShape implements AppAction {

    private final Model model;
    private final MyShape shape;
    private final MyShape before;
    private final MyShape after;

    public ActionChangeShape(Model model, MyShape shape, MyShape before, MyShape after) {
        this.model = model;
        this.shape = shape;
        this.before = before;
        this.after = after;
    }

    @Override
    public void execute() {
        apply(after);
    }

    @Override
    public void unexecute() {
        apply(before);
    }

    @Override
    public AppAction cloneAction() {
        return new ActionChangeShape(model, shape, before, after);
    }

    @Override
    public String getName() {
        return "Изменение фигуры";
    }

    private void apply(MyShape snapshot) {
        shape.copyStateFrom(snapshot);
        model.setSelectedShape(shape);
        model.fireChange();
    }
}
