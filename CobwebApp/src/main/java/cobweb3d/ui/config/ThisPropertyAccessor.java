package cobweb3d.ui.config;


public class ThisPropertyAccessor extends PropertyAccessorBase {

    @Override
    public String getIdentifier() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String thisGetName() {
        return "this";
    }

    @Override
    protected String thisToString() {
        return "this";
    }

    @Override
    protected Object thisGetValue(Object object) {
        return object;
    }

    @Override
    protected void thisSetValue(Object object, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int thisHashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ThisPropertyAccessor) {
            ThisPropertyAccessor o = (ThisPropertyAccessor) obj;
            return super.equals(o);
        }
        return false;
    }
}
