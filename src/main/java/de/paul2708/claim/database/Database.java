package de.paul2708.claim.database;

import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Pair;

import java.util.UUID;

/**
 * This interface represents a database and is used as general strategy.
 *
 * @author Paul2708
 */
public interface Database {

    /**
     * Connect to the database.
     *
     * @throws DatabaseException if an exception is thrown
     */
    void connect() throws DatabaseException;

    /**
     * Set up the database connection.
     *
     * @throws DatabaseException if an exception is thrown
     */
    void setUp() throws DatabaseException;

    /**
     * Resolve the claim information about a player.
     *
     * @param uuid player uuid
     * @param result database result
     */
    void resolveClaimInformation(UUID uuid, DatabaseResult<ClaimInformation> result);

    /**
     * Update the claim information for a player.
     *
     * @param uuid player uuid
     * @param chunk updated chunk
     * @param add true if the chunk will be added, otherwise false to remove it
     * @param result database result
     */
    void updateClaimInformation(UUID uuid, Pair<Integer, Integer> chunk, boolean add, DatabaseResult<Void> result);

    /**
     * Check if a chunk is already claimed.
     *
     * @param chunk chunk
     * @param result database result
     */
    void checkClaim(Pair<Integer, Integer> chunk, DatabaseResult<Boolean> result);

    /**
     * Disconnect from the database.
     *
     * @throws DatabaseException if an exception is thrown
     */
    void disconnect() throws DatabaseException;
}
