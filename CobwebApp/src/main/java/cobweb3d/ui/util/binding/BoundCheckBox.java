package cobweb3d.ui.util.binding;

import cobweb3d.ui.config.PropertyAccessor;

import javax.swing.*;

public class BoundCheckBox extends JCheckBox implements FieldBoundComponent {

    private static final long serialVersionUID = -4621056922233460755L;
    private final Object obj;
    private final PropertyAccessor field;
    private final String label;

    public BoundCheckBox(Object obj, PropertyAccessor accessor) {
        this.obj = obj;
        this.field = accessor;
        if (!field.getType().equals(boolean.class) && !field.getType().equals(Boolean.class))
            throw new IllegalArgumentException("Accessor type must be boolean");

        setModel(new BoundButtonModel());
        this.label = accessor.getName();
    }

    @Override
    public String getLabelText() {
        return label;
    }

    private class BoundButtonModel extends ToggleButtonModel {
        private static final long serialVersionUID = -5478297230476196970L;

        @Override
        public boolean isSelected() {
            return (boolean) field.get(obj);
        }

        @Override
        public void setSelected(boolean b) {
            field.set(obj, b);
        }
    }
}
