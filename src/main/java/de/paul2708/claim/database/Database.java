package de.paul2708.claim.database;

import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Pair;

import java.util.List;
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
     * Get a list of all claim information.
     *
     * @return list of claim information
     * @throws DatabaseException if an exception is thrown
     */
    List<ClaimInformation> resolveClaimInformation() throws DatabaseException;

    /**
     * Update the claim information for a player.
     *
     * @param uuid player uuid
     * @param chunk updated chunk
     * @param add true if the chunk will be added, otherwise false to remove it
     * @throws DatabaseException if an exception is thrown
     */
    void updateClaimInformation(UUID uuid, Pair<Integer, Integer> chunk, boolean add) throws DatabaseException;

    /**
     * Check if a chunk is already claimed.
     *
     * @param chunk chunk
     * @throws DatabaseException if an exception is thrown
     */
    boolean isClaimed(Pair<Integer, Integer> chunk) throws DatabaseException;

    /**
     * Disconnect from the database.
     *
     * @throws DatabaseException if an exception is thrown
     */
    void disconnect() throws DatabaseException;
}