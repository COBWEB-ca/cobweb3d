package cobweb3d.ui.config;

import java.lang.reflect.AnnotatedElement;

/**
 * Allows a property of an object to be read and written.
 */
public interface PropertyAccessor {

    /**
     * Name for this property
     *
     * @return Name for this property
     */
    String getName();

    String getIdentifier();

    /**
     * Gets value of property for given object
     *
     * @param object object to get value from
     * @return value of property for object
     */
    Object get(Object object);


    float getAsFloat(Object object);

    /**
     * Sets value of property for given object
     *
     * @param object object to set value on
     * @param value  value to set
     */
    void set(Object object, Object value);

    void setAsFloat(Object object, float value);

    /**
     * Class of the property
     *
     * @return class of the property
     */
    Class<?> getType();

    AnnotatedElement getAnnotationSource();
}
