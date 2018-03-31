/**
 *
 */
package cobweb3d.ui.swing.components.table;

import cobweb3d.ui.config.*;
import cobwebutil.MutatableFloat;
import cobwebutil.MutatableInt;
import cobwebutil.ReflectionUtil;
import cobwebutil.io.*;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Table model that binds to CobwebParam object exposed fields
 * For an example, see the Resources and Agents tabs
 */
public class ConfigTableModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = -8556152150949927964L;

    private final String prefix;
    public ChoiceCatalog choiceCatalog = new ChoiceCatalog();
    private ParameterSerializable[] data;
    private List<PropertyAccessor> fields = new ArrayList<>();
    private int columns;

    /**
     * Creates table model for array of CobwebParam
     *
     * @param data   CobwebParam array to display as columns
     * @param prefix Prefix for the column names
     */
    public ConfigTableModel(ParameterSerializable[] data, String prefix) {
        super();
        this.data = data;
        this.prefix = prefix;
        columns = data.length;

        if (data.length > 0) bindObject(data[0]);
    }

    public ConfigTableModel(ParameterSerializable data, String prefix) {
        this(new ParameterSerializable[]{data}, prefix);
    }

    protected void bindObject(ParameterSerializable d) {
        bindConfigObject(d, d.getClass(), null);
    }

    protected void bindConfigObject(ParameterSerializable root, Class<? extends ParameterSerializable> actualClass, PropertyAccessor parent) {
        for (Method m : actualClass.getMethods()) {
            if (!m.isAnnotationPresent(ConfDisplayName.class) &&
                    !m.isAnnotationPresent(ConfDisplayFormat.class))
                continue;

            PropertyAccessor fieldAccessor = new SetterPropertyAccessor(parent, m);
            bindItem(root, fieldAccessor);
        }

        for (Field f : actualClass.getFields()) {
            if (!f.isAnnotationPresent(ConfDisplayName.class) &&
                    !f.isAnnotationPresent(ConfDisplayFormat.class))
                continue;

            PropertyAccessor fieldAccessor = new FieldPropertyAccessor(parent, f);
            bindItem(root, fieldAccessor);
        }
    }

    protected void bindItem(ParameterSerializable root, PropertyAccessor fieldAccessor) {
        {
            try {
                if (fieldAccessor.getType().isArray()) {
                    int len = Array.getLength(fieldAccessor.get(root));
                    for (int i = 0; i < len; i++) {
                        bindItem(root, new ArrayPropertyAccessor(fieldAccessor, i));
                    }
                } else if (List.class.isAssignableFrom(fieldAccessor.getType())) {
                    List<?> list = (List<?>) fieldAccessor.get(root);
                    for (int i = 0; i < list.size(); i++) {
                        bindItem(root, new ListPropertyAccessor(fieldAccessor, i));
                    }
                } else if (Map.class.isAssignableFrom(fieldAccessor.getType())) {
                    Map<?, ?> col = (Map<?, ?>) fieldAccessor.get(root);
                    for (Object k : col.keySet()) {
                        bindItem(root, new MapPropertyAccessor(fieldAccessor, k));
                    }
                } else if (ParameterSerializable.class.isAssignableFrom(fieldAccessor.getType())) {
                    ParameterSerializable value = (ParameterSerializable) fieldAccessor.get(root);
                    Class<? extends ParameterSerializable> valueClass = value.getClass();
                    bindConfigObject(root, valueClass, fieldAccessor);
                } else if (MutatableFloat.class.isAssignableFrom(fieldAccessor.getType()) ||
                        MutatableInt.class.isAssignableFrom(fieldAccessor.getType())) {
                    fields.add(new MutatablePropertyAccessor(fieldAccessor));
                } else {
                    //DEBUG
                    //System.out.println("Binding: " + root.getClass().getSimpleName() + fieldAccessor.toString() + " as \"" + fieldAccessor.getName() +"\"");
                    fields.add(fieldAccessor);
                }
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Could not bind property " + root.getClass().getName() + fieldAccessor.toString(), ex);
            }
        }
    }

    @Override
    public int getColumnCount() {
        return columns + 1;
    }

    @Override
    public int getRowCount() {
        return fields.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0;
    }

    @Override
    public Object getValueAt(int row, int col) {
        PropertyAccessor mf = fields.get(row);
        if (col == 0)
            return mf.getName();
        else
            return mf.get(data[col - 1]);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 0)
            return;

        PropertyAccessor mf = fields.get(row);
        Class<?> declaredClass = mf.getType();

        Object typedValue;
        if (value instanceof String && !declaredClass.equals(String.class)) {
            typedValue = ReflectionUtil.stringToBoxed(declaredClass, (String) value);
        } else {
            typedValue = value;
            assert declaredClass.isAssignableFrom(value.getClass());
        }

        mf.set(data[col - 1], typedValue);

        fireTableCellUpdated(row, col);
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0)
            return "";

        if (columns > 1)
            return prefix + " " + column;

        return prefix;
    }

    @SuppressWarnings("unchecked")
    public <T extends ParameterChoice> Set<T> getRowOptions(int row) {
        if (choiceCatalog == null)
            throw new IllegalArgumentException("ConfigTableModel needs choiceCatalog for this row");

        Class<T> clazz = (Class<T>) fields.get(row).getType();
        Set<T> res = choiceCatalog.getChoices(clazz);
        return res;
    }
}
