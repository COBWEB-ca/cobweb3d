package cobweb3d.ui.util.binding;

import cobweb3d.ui.config.PropertyAccessor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BoundJSpinner extends JSpinner implements FieldBoundComponent, ChangeListener {

    private static final long serialVersionUID = 6928975337583519338L;

    private final Object obj;
    private final PropertyAccessor field;

    public BoundJSpinner(Object obj, PropertyAccessor accessor, SpinnerNumberModel spinnerNumberModel) {
        super(spinnerNumberModel);
        this.obj = obj;
        this.field = accessor;

        // Set current value
        this.setValue(field.get(this.obj));
        //this.addPropertyChangeListener("value", this);
        addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        field.set(obj, FieldBoundComponent.validate(field, ((JSpinner) e.getSource()).getValue()));
    }

    @Override
    public String getLabelText() {
        return field.getName();
    }
}