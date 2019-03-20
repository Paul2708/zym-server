package de.paul2708.claim.database.mysql;

import java.sql.SQLException;

/**
 * This interface represents a connection result.
 *
 * @param <R> result type
 */
public interface ConnectionResult<R> {

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
    void exception(SQLException exception);
}