package de.paul2708.claim.file.condition;

/**
 * This interface represents a condition, that has to be fulfilled.
 *
 * @author Paul2708
 * @param <T> type
 */
public interface Condition<T> {

    /**
     * Setup the condition.
     *
     * @param object object
     * @return true, if the condition is fulfilled, otherwise false
     */
    boolean fulfill(T object);

    /**
     * Get a description of the condition.
     *
     * @return description
     */
    String description();
}
