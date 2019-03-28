package de.paul2708.claim.database;

import de.paul2708.claim.model.chunk.ChunkWrapper;

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
     * Resolve all relevant information from the database.
     *
     * @throws DatabaseException if an exception is thrown
     */
    void resolve() throws DatabaseException;

    /**
     * Create a new entry for the uuid.
     *
     * @param uuid player uuid
     * @param result database result (inserted player id)
     */
    void create(UUID uuid, DatabaseResult<Integer> result);

    /**
     * Add a claimed chunk.
     *
     * @param playerId player id
     * @param chunk claimed chunk
     * @param groupChunk true if the chunk is a group chunk, otherwise false
     * @param result database result (inserted chunk id)
     */
    void addClaimedChunk(int playerId, ChunkWrapper chunk, boolean groupChunk, DatabaseResult<Integer> result);

    /**
     * Remove a claimed chunk by its id.
     *
     * @param id chunk id
     * @param result database result
     */
    void removeChunk(int id, DatabaseResult<Void> result);

    /**
     * Set the amount of bought claimers.
     *
     * @param playerId player uuid
     * @param amount new amount of bought claimers
     * @param result database result
     */
    void setClaimer(int playerId, int amount, DatabaseResult<Void> result);

    /**
     * Add a city chunk.
     *
     * @param playerId player id
     * @param chunk updated chunk
     * @param interactable true if all players will be able to interact in it, otherwise false
     * @param result database result (inserted city chunk id)
     */
    void addCityChunk(int playerId, ChunkWrapper chunk, boolean interactable, DatabaseResult<Integer> result);

    /**
     * Update a city chunk.
     *
     * @param id city chunk id
     * @param interactable true if all players will be able to interact in it, otherwise false
     * @param result database result
     */
    void updateCityChunk(int id, boolean interactable, DatabaseResult<Void> result);

    /**
     * Permit a target to interact on your group chunk.
     *
     * @param playerId target id
     * @param chunkId chunk id
     * @param result database result
     */
    void addAccess(int playerId, int chunkId, DatabaseResult<Integer> result);

    /**
     * Remove an access.
     *
     * @param playerId player id
     * @param chunkId chunk id
     * @param result database result
     */
    void removeAccess(int playerId, int chunkId, DatabaseResult<Void> result);

    /**
     * Disconnect from the database.
     *
     * @throws DatabaseException if an exception is thrown
     */
    void disconnect() throws DatabaseException;
}
