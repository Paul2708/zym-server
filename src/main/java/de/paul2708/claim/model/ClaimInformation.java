package de.paul2708.claim.model;

import org.json.simple.JsonArray;
import org.json.simple.JsonObject;

import java.util.*;

/**
 * This class holds information about a claim.
 *
 * @author Paul2708
 */
public final class ClaimInformation {

    private static final Map<UUID, ClaimInformation> CACHE = new HashMap<>();
    private static final Map<ChunkInformation, UUID> CHUNK_CACHE = new HashMap<>();

    private UUID uuid;
    private List<ChunkInformation> chunks;

    /**
     * Create a new claim information.
     *
     * @param uuid uuid
     * @param chunks chunks
     */
    private ClaimInformation(UUID uuid, List<ChunkInformation> chunks) {
        this.uuid = uuid;
        this.chunks = chunks;
    }

    /**
     * Update a chunk by adding or removing it.
     *
     * @param chunk chunk
     * @param add true to add it, false to remove it
     */
    public void updateChunk(ChunkInformation chunk, boolean add) {
        if (add) {
            this.chunks.add(chunk);
        } else {
            this.chunks.remove(chunk);
        }
    }

    /**
     * Check if the chunk is claimed.
     *
     * @param chunk chunk
     * @return true if the chunk is claimed, otherwise false
     */
    public boolean contains(ChunkInformation chunk) {
        return this.chunks.contains(chunk);
    }

    /**
     * Get the json object of the information.
     *
     * @return json object
     */
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.put("uuid", uuid.toString());

        JsonArray array = new JsonArray();

        for (ChunkInformation chunk : chunks) {
            JsonObject chunkObject = new JsonObject();
            chunkObject.put("x", chunk.getX());
            chunkObject.put("z", chunk.getZ());

            array.add(chunkObject);
        }

        object.put("chunks", array);

        return object;
    }

    /**
     * Create a new claim information.
     *
     * @param uuid player uuid
     * @param chunks claimed chunks
     */
    public static void create(UUID uuid, List<ChunkInformation> chunks) {
        ClaimInformation information = new ClaimInformation(uuid, chunks);

        ClaimInformation.CACHE.put(uuid, information);

        for (ChunkInformation chunk : chunks) {
            ClaimInformation.CHUNK_CACHE.put(chunk, uuid);
        }
    }

    /**
     * Clear the cache.
     */
    public static void clear() {
        ClaimInformation.CACHE.clear();
    }

    /**
     * Get the claim information for a player.
     *
     * @param uuid player uuid
     * @return the players claim information
     */
    public static ClaimInformation get(UUID uuid) {
        return ClaimInformation.CACHE.get(uuid);
    }

    /**
     * Get the player uuid of a claimed chunk.
     *
     * @param chunk chunk
     * @return the players uuid
     */
    public static UUID get(ChunkInformation chunk) {
        return ClaimInformation.CHUNK_CACHE.get(chunk);
    }

    /**
     * Get all claim information.
     *
     * @return collection of information
     */
    public static Collection<ClaimInformation> getAll() {
        return ClaimInformation.CACHE.values();
    }
}
