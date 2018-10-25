package de.paul2708.claim.database;

/**
 * This interface represents a database result for asynchronous calls.
 *
 * @param <T> result type
 * @author Paul2708
 */
public interface DatabaseResult<T> {

    /**
     * Successful received the result.
     *
     * @param result result
     */
    void success(T result);

    /**
     * Handle a database exception, if one occurred.
     *
     * @param exception database exception
     */
    void exception(DatabaseException exception);
}
