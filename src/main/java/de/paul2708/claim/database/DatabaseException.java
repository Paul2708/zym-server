package de.paul2708.claim.database;

import java.io.IOException;

/**
 * This input/output exception is thrown if the database throws any exceptions.
 *
 * @author Paul2708
 */
public class DatabaseException extends IOException {

    /**
     * Create a new database exception with message.
     *
     * @param message exception message
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Create a new database exception with message and cause.
     *
     * @param message exception message
     * @param cause exception throwable
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
