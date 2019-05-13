package de.paul2708.claim.file.condition;

/**
 * This condition is always fulfilled, independent of the value.
 *
 * @param <T> type
 * @author Paul2708
 */
public class NoneCondition<T> implements Condition<T> {

    /**
     * Return <code>true</code>.
     *
     * @param object object
     * @return true, if the condition is fulfilled, otherwise false
     */
    @Override
    public boolean fulfill(T object) {
        return true;
    }

    /**
     * Get a description of the condition.
     *
     * @return description
     */
    @Override
    public String description() {
        return "none condition";
    }
}
