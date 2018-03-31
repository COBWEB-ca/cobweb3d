package cobweb3d.ui.util.binding;

import cobweb3d.ui.config.PropertyAccessor;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Format;

public class BoundJFormattedTextField extends JFormattedTextField implements FieldBoundComponent, PropertyChangeListener {

    /**
     *
     */
    private static final long serialVersionUID = 6928975337583519338L;

    private final Object obj;
    private final PropertyAccessor field;

    public BoundJFormattedTextField(Object obj, PropertyAccessor accessor, Format format) {
        super(format);
        this.obj = obj;
        this.field = accessor;

        // Set current value
        this.setValue(field.get(this.obj));
        this.addPropertyChangeListener("value", this);
    }

    @Override
    @SuppressWarnings("boxing")
    public void propertyChange(PropertyChangeEvent evt) {
        field.set(obj, FieldBoundComponent.validate(field, evt.getNewValue()));
    }

    @Override
    public String getLabelText() {
        return field.getName();
    }
}
