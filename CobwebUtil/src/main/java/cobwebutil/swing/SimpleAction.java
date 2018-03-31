package cobwebutil.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SimpleAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private ActionStrategy actionStrategy;

    public SimpleAction(String name, ActionStrategy actionStrategy) {
        super(name);
        this.actionStrategy = actionStrategy;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (actionStrategy != null) actionStrategy.actionPerformed(e);
    }

    public interface ActionStrategy {
        /**
         * Called when the focus is changed in any way.
         */
        void actionPerformed(ActionEvent e);
    }
}
