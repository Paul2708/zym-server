package de.paul2708.claim.database;

import de.paul2708.claim.model.ChunkData;

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
     * @param result database result
     */
    void create(UUID uuid, DatabaseResult<Void> result);

    /**
     * Update the claim information for a player.
     *
     * @param uuid player uuid
     * @param chunk updated chunk
     * @param add true if the chunk will be added, otherwise false to remove it
     * @param result database result
     */
    void updateClaimedChunk(UUID uuid, ChunkData chunk, boolean add, DatabaseResult<Void> result);

    /**
     * Update the amount of bought claimers.
     *
     * @param uuid player uuid
     * @param amount new amount of bought claimers
     * @param result database result
     */
    void updateClaimer(UUID uuid, int amount, DatabaseResult<Void> result);

    /**
     * Update the group chunk by either claiming or removing.
     *
     * @param uuid player uuid
     * @param chunk updated chunk
     * @param add true if the chunk will be added, otherwise false to remove it
     * @param result database result
     */
    void updateGroupChunk(UUID uuid, ChunkData chunk, boolean add, DatabaseResult<Void> result);

    /**
     * Update the city chunk by either claiming or removing.
     *
     * @param uuid player uuid
     * @param chunk updated chunk
     * @param add true if the chunk will be added, otherwise false to remove it
     * @param interactable true if all players will be able to interact in it, otherwise false
     * @param result database result
     */
    void updateCityChunk(UUID uuid, ChunkData chunk, boolean add, boolean interactable, DatabaseResult<Void> result);

    /**
     * Permit or remove permission for a target to interact on your group chunk.
     *
     * @param uuid player uuid
     * @param chunk updated chunk
     * @param target target uuid
     * @param result database result
     */
    void updateAccess(UUID uuid, ChunkData chunk, UUID target, DatabaseResult<Void> result);

    /**
     * Disconnect from the database.
     *
     * @throws DatabaseException if an exception is thrown
     */
    void disconnect() throws DatabaseException;
}
