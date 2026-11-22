package org.example.view.menu;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;

/**
 * Адаптер между Swing-действием (Action) и командой приложения (AppCommand).
 * Его можно добавлять и в JMenu, и в JToolBar.
 */
public class CommandActionListener extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private final AppCommand command;

    public CommandActionListener(AppCommand command) {
        this.command = command;
    }

    public CommandActionListener(String name, Icon icon, AppCommand command) {
        super(name, icon);
        this.command = command;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (command != null) {
            command.execute();
        }
    }
}
