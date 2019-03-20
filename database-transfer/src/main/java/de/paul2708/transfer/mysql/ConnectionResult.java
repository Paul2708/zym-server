package de.paul2708.transfer.mysql;

/**
 * This interface represents a connection result.
 *
 * @param <R> result type
 * @param <E> exception type
 */
public interface ConnectionResult<R, E extends Exception> {

    /**
     * Got the result successfully.
     *
     * @param result result
     */
    void success(R result);

    /**
     * Handle an occurred exception.
     *
     * @param exception exception
     */
    void exception(E exception);
}