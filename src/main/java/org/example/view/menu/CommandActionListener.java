package org.example.view.menu;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;

public class CommandActionListener extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private final transient AppCommand command;

    public CommandActionListener(AppCommand command) {
        this.command = command;
    }

    public CommandActionListener(String name, Icon icon, AppCommand command) {
        super(name, icon);
        this.command = command;
    }

    public CommandActionListener(String name, Icon icon, String toolTip, AppCommand command) {
        super(name, icon);
        this.command = command;
        if (toolTip != null) {
            putValue(SHORT_DESCRIPTION, toolTip);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (command != null) {
            command.execute();
        }
    }
}
