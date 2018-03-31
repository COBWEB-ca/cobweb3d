package cobweb3d.ui.config;

import cobwebutil.MutatableFloat;
import cobwebutil.MutatableInt;


public class MutatablePropertyAccessor extends PropertyAccessorBase {

    private Class<?> type;

    public MutatablePropertyAccessor(PropertyAccessor container) {
        super(container);

        Class<?> containerType = container.getType();
        if (MutatableInt.class.isAssignableFrom(containerType)) {
            this.type = int.class;
        } else if (MutatableFloat.class.isAssignableFrom(containerType)) {
            this.type = float.class;
        } else {
            throw new IllegalArgumentException("MutatablePropertyAccessor can only access MutatableInt or MutatableFloat values");
        }
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    protected int thisHashCode() {
        return 1;
    }

    @Override
    public String getName() {
        return parent.getName();
    }

    @Override
    protected String thisGetName() {
        return "";
    }

    @Override
    protected String thisToString() {
        return "";
    }

    @Override
    protected Object thisGetValue(Object object) {
        if (type.equals(int.class)) {
            return ((MutatableInt) object).getRawValue();
        } else {
            return ((MutatableFloat) object).getRawValue();
        }
    }

    @Override
    protected void thisSetValue(Object object, Object value) {
        if (type.equals(int.class)) {
            ((MutatableInt) object).setValue((int) value);
        } else {
            ((MutatableFloat) object).setValue((float) value);
        }
    }

}
