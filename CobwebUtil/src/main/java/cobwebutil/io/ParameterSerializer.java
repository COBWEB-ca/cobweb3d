package cobwebutil.io;

import cobwebutil.MutatableFloat;
import cobwebutil.MutatableInt;
import cobwebutil.ReflectionUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class ParameterSerializer {

    private ChoiceCatalog parts;

    public ParameterSerializer(ChoiceCatalog parts) {
        this.parts = parts;
    }

    /**
     * Allows the extraction of data from a configuration file for any Cobweb parameters.  The data is
     * passed in as the root node of a tree containing the data.
     *
     * @param obj  The type of object parameters.
     * @param root The root node of the tree.
     */
    public ParameterSerializable load(ParameterSerializable obj, Node root) {
        Class<?> T = obj.getClass();

        Map<String, Method> setters = new LinkedHashMap<>();
        for (Method m : T.getMethods()) {
            ConfXMLTag tagname = m.getAnnotation(ConfXMLTag.class);
            if (tagname != null) {
                setters.put(tagname.value(), m);
            }
        }

        Map<String, Field> fields = new LinkedHashMap<String, Field>();
        List<Field> squishFields = new ArrayList<Field>();
        for (Field f : T.getFields()) {
            ConfXMLTag tagname = f.getAnnotation(ConfXMLTag.class);
            if (f.isAnnotationPresent(ConfSquishParent.class)) {
                squishFields.add(f);
            } else if (tagname != null) {
                fields.put(tagname.value(), f);
            }
        }

        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);

            Method setter = setters.get(n.getNodeName());
            if (setter != null) {
                try {
                    Object newValue = loadObject(setter.getParameterTypes()[0], setter, null, n);
                    setter.invoke(obj, newValue);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException("Could not invoke config setter: " + setter.getName(), ex);
                }
            }

            Field f = fields.get(n.getNodeName());
            if (f == null) {
                continue;
            }
            try {

                Object currentValue = f.get(obj);

                Object newValue = loadObject(f.getType(), f, currentValue, n);

                f.set(obj, newValue);

            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new IllegalArgumentException("Cannot load configuration field: " + f.getName(), ex);
            }
        }

        for (Field f : squishFields) {
            try {
                Object currentValue = f.get(obj);
                Object newValue = loadObject(f.getType(), f, currentValue, root);
                f.set(obj, newValue);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new IllegalArgumentException("Cannot load configuration field: " + f.getName(), ex);
            }
        }

        return obj;
    }

    /**
     * Saves data fields from any object implementing the CobwebParam interface to a data file, doc.
     *
     * @param obj    Cobweb parameter object.
     * @param config Initial node to add data fields to.
     * @param doc    Data file fields are saved to.
     */
    public void save(ParameterSerializable obj, Element config, Document doc) {
        Class<?> T = obj.getClass();

        for (Method m : T.getMethods()) {
            ConfXMLTag tagname = m.getAnnotation(ConfXMLTag.class);
            if (tagname == null) {
                continue;
            }

            try {
                Method getter = T.getMethod(m.getName().replaceFirst("^set", "get"));
                Object value = getter.invoke(obj);
                Element tag = doc.createElement(tagname.value());
                saveObject(value.getClass(), m, value, tag, doc);
                config.appendChild(tag);

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException("Could not invoke config getter: " + m.getName(), ex);
            }


        }

        for (Field f : T.getFields()) {

            boolean squish = f.isAnnotationPresent(ConfSquishParent.class);
            ConfXMLTag tagname = f.getAnnotation(ConfXMLTag.class);
            // if (tagname.value().equals(""))
            Element tag;
            if (squish) // store config in current node, don't create a child
            {
                tag = config;
            } else if (tagname != null) // store config in child node
            {
                tag = doc.createElement(tagname.value());
            } else // not a field we care about
            {
                continue;
            }

            try {
                Class<?> t = f.getType();
                Object value = f.get(obj);
                saveObject(t, f, value, tag, doc);

            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new IllegalArgumentException("Cannot save configuration field: " + f.getName(), ex);
            }

            if (!squish) {
                config.appendChild(tag);
            }
        }
    }


    private Object loadObject(Class<?> type, AnnotatedElement annotationSource, Object currentValue,
                              Node objectNode) throws IllegalArgumentException, IllegalAccessException {
        Object newValue = currentValue;

        if (ReflectionUtil.isPrimitive(type)) {
            String strVal = objectNode.getFirstChild().getNodeValue();
            newValue = ReflectionUtil.stringToBoxed(type, strVal);

        } else if (MutatableFloat.class.isAssignableFrom(type)) {
            String strVal = objectNode.getFirstChild().getNodeValue();
            float value = (float) ReflectionUtil.stringToBoxed(float.class, strVal);
            newValue = new MutatableFloat(value);

        } else if (MutatableInt.class.isAssignableFrom(type)) {
            String strVal = objectNode.getFirstChild().getNodeValue();
            int value = (int) ReflectionUtil.stringToBoxed(int.class, strVal);
            newValue = new MutatableInt(value);

        } else if (type.isEnum()) {
            newValue = loadEnum(type, objectNode.getFirstChild().getNodeValue());

        } else if (ParameterChoice.class.isAssignableFrom(type)) {
            @SuppressWarnings("unchecked")
            Class<? extends ParameterChoice> dynamicType = (Class<? extends ParameterChoice>) type;
            newValue = loadChoice(dynamicType, objectNode);

        } else if (ParameterSerializable.class.isAssignableFrom(type)) {
            ParameterSerializable inner = (ParameterSerializable) currentValue;
            try {
                Class<?> instanceClass = type;
                Node instanceClassName = objectNode.getAttributes().getNamedItem("class");
                if (instanceClassName != null) {
                    instanceClass = Class.forName(instanceClassName.getTextContent());
                }
                if (inner == null || !instanceClass.isAssignableFrom(inner.getClass())) {
                    inner = (ParameterSerializable) instanceClass.newInstance();
                }
            } catch (InstantiationException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            newValue = load(inner, objectNode);

        } else if (type.isArray()) {
            newValue = loadArray(type, annotationSource, currentValue, objectNode);

        } else if (List.class.isAssignableFrom(type)) {
            newValue = loadList(annotationSource, (List<?>) currentValue, objectNode);

        } else if (Map.class.isAssignableFrom(type)) {
            newValue = loadMap(annotationSource, currentValue, objectNode);

        } else {
            throw new IllegalArgumentException("Unknown field type");
        }
        return newValue;
    }

    private void saveObject(Class<?> type, AnnotatedElement annotationSource, Object value,
                            Element tag, Document doc) {
        if (ReflectionUtil.isPrimitive(type) || type.isEnum()) {
            tag.setTextContent(value.toString());

        } else if (MutatableFloat.class.isAssignableFrom(type)) {
            tag.setTextContent(Float.toString(((MutatableFloat) value).getRawValue()));

        } else if (MutatableInt.class.isAssignableFrom(type)) {
            tag.setTextContent(Integer.toString(((MutatableInt) value).getRawValue()));

        } else if (ParameterChoice.class.isAssignableFrom(type)) {
            ParameterChoice inner = (ParameterChoice) value;
            saveChoice(inner, tag);

        } else if (ParameterSerializable.class.isAssignableFrom(type)) {
            ParameterSerializable inner = (ParameterSerializable) value;
            if (annotationSource.isAnnotationPresent(ConfSaveInstanceClass.class)) {
                tag.setAttribute("class", value.getClass().getName());
            }
            save(inner, tag, doc);

        } else if (type.isArray()) {
            saveArray(type, annotationSource, value, tag, doc);

        } else if (List.class.isAssignableFrom(type)) {
            saveList(annotationSource, (List<?>) value, tag, doc);

        } else if (Map.class.isAssignableFrom(type)) {
            saveMap(annotationSource, value, tag, doc);

        } else {
            throw new IllegalArgumentException("Unknown field type");
        }
    }

    private Object loadArray(Class<?> arrayType, AnnotatedElement arrayAnnotations,
                             Object currentArray, Node arrayNode)
            throws IllegalArgumentException, IllegalAccessException {
        return loadArray(arrayType, arrayAnnotations, currentArray, 0, arrayNode);
    }

    private Object loadArray(Class<?> arrayType, AnnotatedElement arrayAnnotations,
                             Object currentArray, int depth, Node arrayNode)
            throws IllegalArgumentException, IllegalAccessException {

        Class<?> componentType;
        if (currentArray != null) {
            // This is required when serializing generic T[] arrays
            // The T type is erased and ends up being Object or whatever T extends
            // If there is an existing default array, it is using the correct type
            componentType = currentArray.getClass().getComponentType();
        } else {
            componentType = arrayType.getComponentType();
        }
        if (!canSerializeArray(componentType)) {
            throw new IllegalArgumentException("Unknown array type: " + componentType);
        }

        ConfList listOptions = arrayAnnotations.getAnnotation(ConfList.class);
        if (listOptions == null) {
            throw new IllegalArgumentException("Config lists must be tagged @ConfList");
        }

        List<Object> result = new ArrayList<Object>();

        NodeList children = arrayNode.getChildNodes();
        int arrayIndex = 0;
        for (int i = 0; i < children.getLength(); i++) {
            Node itemNode = children.item(i);

            if (!itemNode.getNodeName().startsWith(listOptions.indexName()[depth])) {
                continue;
            }

            Object currentItem = null;
            if (currentArray != null && arrayIndex < Array.getLength(currentArray)) {
                currentItem = Array.get(currentArray, arrayIndex);
            }

            Object newItem = currentItem;
            if (componentType.isArray()) {
                newItem = loadArray(componentType, arrayAnnotations, currentItem, depth + 1, itemNode);
            } else {
                newItem = loadObject(componentType, arrayAnnotations, currentItem, itemNode);
            }

            result.add(newItem);
            arrayIndex++;
        }

        Object newArray = Array.newInstance(componentType, result.size());
        for (int i = 0; i < result.size(); i++) {
            Array.set(newArray, i, result.get(i));
        }

        return newArray;
    }

    private void saveArray(Class<?> arrayType, AnnotatedElement arrayAnnotations, Object array,
                           Element tag, Document doc) {
        saveArray(arrayType, arrayAnnotations, array, 0, tag, doc);
    }

    private void saveArray(Class<?> arrayType, AnnotatedElement arrayAnnotations, Object array,
                           int depth,
                           Element tag, Document doc) {

        Class<?> componentType = arrayType.getComponentType();
        if (!canSerializeArray(componentType)) {
            throw new IllegalArgumentException("Unknown array type");
        }

        ConfList listOptions = arrayAnnotations.getAnnotation(ConfList.class);
        if (listOptions == null) {
            throw new IllegalArgumentException("Config lists must be tagged @ConfList");
        }

        for (int i = 0; i < Array.getLength(array); i++) {
            int outputIndex = listOptions.startAtOne() ? i + 1 : i;
            Element itemTag = doc.createElement(listOptions.indexName()[depth]);
            itemTag.setAttribute("id", Integer.toString(outputIndex));

            Object item = Array.get(array, i);

            if (componentType.isArray()) {
                saveArray(componentType, arrayAnnotations, item, depth + 1, itemTag, doc);
            } else {
                saveObject(componentType, arrayAnnotations, item, itemTag, doc);
            }

            tag.appendChild(itemTag);
        }
    }

    protected boolean canSerializeArray(Class<?> componentType) {
        for (Class<?> ct = componentType; ; ct = ct.getComponentType()) {
            if (canSerialize(ct)) {
                return true;
            } else if (!ct.isArray()) {
                return false;
            }
        }
    }

    private List<?> loadList(AnnotatedElement listAnnotations,
                             List<?> currentList, Node listNode)
            throws IllegalArgumentException, IllegalAccessException {
        Class<?> componentType = getListComponentType(listAnnotations);
        ConfList listOptions = listAnnotations.getAnnotation(ConfList.class);

        List<Object> result = new ArrayList<>();

        NodeList children = listNode.getChildNodes();
        int arrayIndex = 0;
        for (int i = 0; i < children.getLength(); i++) {
            Node itemNode = children.item(i);

            if (!itemNode.getNodeName().startsWith(listOptions.indexName()[0])) {
                continue;
            }

            Object currentItem = null;
            if (arrayIndex < currentList.size()) {
                currentItem = currentList.get(arrayIndex);
            }

            Object newItem = currentItem;
            newItem = loadObject(componentType, listAnnotations, currentItem, itemNode);

            result.add(newItem);
            arrayIndex++;
        }

        return result;
    }

    private void saveList(AnnotatedElement listAnnotations, List<?> list,
                          Element tag, Document doc) {
        Class<?> componentType = getListComponentType(listAnnotations);
        ConfList listOptions = listAnnotations.getAnnotation(ConfList.class);

        for (int i = 0; i < list.size(); i++) {
            int outputIndex = listOptions.startAtOne() ? i + 1 : i;
            Element itemTag = doc.createElement(listOptions.indexName()[0]);
            itemTag.setAttribute("id", Integer.toString(outputIndex));

            Object item = list.get(i);

            saveObject(componentType, listAnnotations, item, itemTag, doc);

            tag.appendChild(itemTag);
        }
    }

    protected Class<?> getListComponentType(AnnotatedElement listAnnotations)
            throws IllegalArgumentException {

        ConfListType listTypeAnnotation = listAnnotations.getAnnotation(ConfListType.class);
        if (listTypeAnnotation == null) {
            throw new IllegalArgumentException("List not tagged with @ConfListType");
        }

        Class<?> componentType = listTypeAnnotation.value();

        if (!canSerializeArray(componentType)) {
            throw new IllegalArgumentException("Unknown list component type");
        }

        if (!listAnnotations.isAnnotationPresent(ConfList.class)) {
            throw new IllegalArgumentException("Config lists must be tagged @ConfList");
        }

        return componentType;
    }

    private Object loadMap(AnnotatedElement mapAnnotations, Object currentMap, Node mapNode)
            throws IllegalArgumentException, IllegalAccessException {

        ConfMap mapOptions = mapAnnotations.getAnnotation(ConfMap.class);
        if (mapOptions == null) {
            throw new IllegalArgumentException("Config maps must be tagged @ConfList");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) currentMap;

        if (result == null) {
            result = new LinkedHashMap<String, Object>();
        }

        NodeList children = mapNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node itemNode = children.item(i);

            if (!itemNode.getNodeName().equals(mapOptions.entryName())) {
                continue;
            }

            String key = itemNode.getAttributes().getNamedItem(mapOptions.keyName()).getTextContent();

            Object currentItem = result.get(key);

            Object newItem = loadObject(mapOptions.valueClass(), mapAnnotations, currentItem, itemNode);

            result.put(key, newItem);
        }

        return result;
    }

    private void saveMap(AnnotatedElement mapAnnotations, Object currentMap, Element tag,
                         Document doc) {

        ConfMap mapOptions = mapAnnotations.getAnnotation(ConfMap.class);
        if (mapOptions == null) {
            throw new IllegalArgumentException("Config maps must be tagged @ConfList");
        }

        @SuppressWarnings("unchecked")
        Map<Object, Object> result = (Map<Object, Object>) currentMap;

        for (Entry<Object, Object> i : result.entrySet()) {
            Element itemTag = doc.createElement(mapOptions.entryName());
            itemTag.setAttribute(mapOptions.keyName(), i.getKey().toString());

            saveObject(mapOptions.valueClass(), mapAnnotations, i.getValue(), itemTag, doc);

            tag.appendChild(itemTag);
        }
    }


    protected boolean canSerialize(Class<?> T) {
        return ReflectionUtil.isPrimitive(T) ||
                ParameterSerializable.class.isAssignableFrom(T) ||
                ParameterChoice.class.isAssignableFrom(T);
    }

    protected Object loadEnum(Class<?> type, String text) {
        try {
            // Simple enums
            @SuppressWarnings({"unchecked", "rawtypes"})
            Enum<?> newValue = Enum.valueOf((Class<? extends Enum>) type, text);
            return newValue;
        } catch (IllegalArgumentException ex) {
            // nothing
        }

        try {
            // Enums with custom names, requires defining static fromString method
            Object newValue = type
                    .getMethod("fromString", String.class)
                    .invoke(null, text);
            return newValue;
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected ParameterChoice loadChoice(Class<? extends ParameterChoice> type, Node node) {
        String identifier = node.getTextContent();
        if (parts != null) {
            if (parts.getChoices(type) != null) {
                for (ParameterChoice x : parts.getChoices(type)) {
                    if (identifier == null && x.getIdentifier() == null) {
                        return x;
                    } else if (identifier != null && identifier.equals(x.getIdentifier())) {
                        return x;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Could not load ParameterChoice" + node.getNodeName());
    }


    protected void saveChoice(ParameterChoice obj, Element node) {
        node.setTextContent(obj.getIdentifier());
    }
}
