package de.paul2708.claim.file;

import de.paul2708.claim.file.condition.Condition;

/**
 * This class represents an element in the {@link AbstractConfiguration}.
 *
 * @author Paul2708
 * @param <T> value type
 */
public class ConfigurationElement<T> {

    private String key;
    private Condition<T> condition;
    private Class<T> clazz;

    /**
     * Create a new configuration element with key, condition and value class.
     *
     * @param key configuration key
     * @param condition condition
     * @param clazz value class
     */
    public ConfigurationElement(String key, Condition<T> condition, Class<T> clazz) {
        this.key = key;
        this.condition = condition;
        this.clazz = clazz;
    }

    /**
     * Validate the object.
     *
     * @param object tested value
     * @throws InvalidValueException if the object is not valid
     */
    public void validate(Object object) throws InvalidValueException {
        if (!clazz.isInstance(object)) {
            throw new InvalidValueException(this.key, "type of '" + object.toString() + "' is not of "
                    + clazz.getSimpleName());
        }

        if (!condition.fulfill((T) object)) {
            throw new InvalidValueException(this.key, "'" + object.toString() + "' doesn't fulfill the condition: "
                    + condition.description());
        }
    }

    /**
     * Get the element key.
     *
     * @return key
     */
    public String getKey() {
        return key;
    }
}
