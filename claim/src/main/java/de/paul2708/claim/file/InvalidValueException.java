package de.paul2708.claim.file;

/**
 * This exception will be thrown if an invalid value was found.
 *
 * @author Paul2708
 */
public class InvalidValueException extends IllegalArgumentException {

    /**
     * Create a new invalid value exception with key and description.
     * 
     * @param key properties key
     * @param description description why the value is invalid
     */
    public InvalidValueException(String key, String description) {
        super(String.format("invalid value for %s: %s", key, description));
    }
}
