package de.paul2708.claim.database.impl.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This callback acts like a consumer but with an exception throwing.
 *
 * @author Paul2708
 */
public interface Callback {

    /**
     * Called, if the result set came in.
     *
     * @param result result
     * @throws SQLException if the result set throws any exceptions
     */
    void call(ResultSet result) throws SQLException;
}
