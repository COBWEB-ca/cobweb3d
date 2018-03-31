package cobwebutil.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SimpleFocusAdapter extends FocusAdapter {

    private UpdateStrategy updateMethod;

    public SimpleFocusAdapter(UpdateStrategy updateMethod) {
        this.updateMethod = updateMethod;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (updateMethod != null) updateMethod.update();
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (updateMethod != null) updateMethod.update();
    }

    public interface UpdateStrategy {
        /**
         * Called when the focus is changed in any way.
         */
        void update();
    }
}
