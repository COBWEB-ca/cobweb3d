package cobwebutil.swing;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface SimpleDocumentListener extends DocumentListener {

    /**
     * Called when the document is updated in any way.
     */
    void update() throws Exception;

    default void tryUpdate() {
        try {
            update();
        } catch (Exception ex) {
            // Do Nothing!
        }
    }

    @Override
    default void insertUpdate(DocumentEvent e) {
        tryUpdate();
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        tryUpdate();
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        tryUpdate();
    }
}
