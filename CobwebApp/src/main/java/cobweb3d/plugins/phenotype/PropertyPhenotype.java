package cobweb3d.plugins.phenotype;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.params.phenotype.Phenotype;
import cobweb3d.ui.config.PropertyAccessor;
import cobwebutil.MutatableField;
import cobwebutil.io.ConfDisplayName;

public abstract class PropertyPhenotype extends Phenotype {

    private static final long serialVersionUID = 1L;
    private PropertyAccessor propertyAccessor;

    public PropertyPhenotype(PropertyAccessor propertyAccessor) {
        super();
        if (propertyAccessor != null && (
                propertyAccessor.getAnnotationSource().getAnnotation(ConfDisplayName.class) == null)) {
            throw new IllegalArgumentException("Property must be have a @ConfDisplayName");
        }

        this.propertyAccessor = propertyAccessor;
    }

    @Override
    public int hashCode() {
        return propertyAccessor.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PropertyPhenotype) {
            PropertyPhenotype o = (PropertyPhenotype) obj;
            return propertyAccessor.equals(o.propertyAccessor);
        }
        return false;
    }

    @Override
    public String getIdentifier() {
        return propertyAccessor.getIdentifier();
    }

    @Override
    public String getName() {
        return propertyAccessor.getName();
    }

    @Override
    public void modifyValue(Object cause, BaseAgent a, float m) {
        if (rootAccessor(a) == null)
            return;

        MutatableField field = (MutatableField) propertyAccessor.get(rootAccessor(a));
        field.setMultiplier(cause, m);
    }

    @Override
    public void unmodifyValue(Object cause, BaseAgent a) {
        if (rootAccessor(a) == null)
            return;

        MutatableField field = (MutatableField) propertyAccessor.get(rootAccessor(a));
        field.removeMultiplier(cause);
    }

    protected abstract Object rootAccessor(BaseAgent a);
}
