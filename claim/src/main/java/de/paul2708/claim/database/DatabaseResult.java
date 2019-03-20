package de.paul2708.claim.database;

/**
 * This interface represents a connection result.
 *
 * @param <R> result type
 */
public interface DatabaseResult<R> {

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
    void exception(DatabaseException exception);
}