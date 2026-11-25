package org.example.view.menu;

import javax.swing.Icon;

/**
 * Пункт меню, который показывает свою «выключенную» иконку, когда действие недоступно.
 * Нужен для кнопок «Отменить» / «Повторить»: они гаснут, когда история пуста.
 */
public class SwitchAction extends CommandActionListener {
    private static final long serialVersionUID = 1L;

    private final transient Icon enabledIcon;
    private final transient Icon disabledIcon;

    public SwitchAction(AppCommand command, Icon enabledIcon, Icon disabledIcon) {
        super(command);
        this.enabledIcon = enabledIcon;
        this.disabledIcon = disabledIcon;
        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        putValue(SMALL_ICON, enabled ? enabledIcon : disabledIcon);
    }
}
