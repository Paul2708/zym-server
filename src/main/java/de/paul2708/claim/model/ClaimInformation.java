package de.paul2708.claim.model;

import de.paul2708.claim.util.Pair;
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

    private UUID uuid;
    private List<Pair<Integer, Integer>> chunks;

    /**
     * Create a new claim information.
     *
     * @param uuid uuid
     * @param chunks chunks
     */
    private ClaimInformation(UUID uuid, List<Pair<Integer, Integer>> chunks) {
        this.uuid = uuid;
        this.chunks = chunks;
    }

    /**
     * Update a chunk by adding or removing it.
     *
     * @param chunk chunk
     * @param add true to add it, false to remove it
     */
    public void updateChunk(Pair<Integer, Integer> chunk, boolean add) {
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
    public boolean contains(Pair<Integer, Integer> chunk) {
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

        for (Pair<Integer, Integer> chunk : chunks) {
            JsonObject chunkObject = new JsonObject();
            chunkObject.put("x", chunk.getKey());
            chunkObject.put("z", chunk.getValue());

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
    public static void create(UUID uuid, List<Pair<Integer, Integer>> chunks) {
        ClaimInformation information = new ClaimInformation(uuid, chunks);

        ClaimInformation.CACHE.put(uuid, information);
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
     * Get all claim information.
     *
     * @return collection of information
     */
    public static Collection<ClaimInformation> getAll() {
        return ClaimInformation.CACHE.values();
    }
}
