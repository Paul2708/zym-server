package de.paul2708.claim.model;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
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

    private static final Map<ChunkData, UUID> CHUNK_CACHE = new HashMap<>();

    private UUID uuid;
    private List<ChunkData> chunks;
    private int buyLevel;

    /**
     * Create a new claim information.
     *
     * @param uuid uuid
     * @param chunks chunks
     * @param level buy level
     */
    private ClaimInformation(UUID uuid, List<ChunkData> chunks, int level) {
        this.uuid = uuid;
        this.chunks = chunks;
        this.buyLevel = level;
    }

    /**
     * Update a chunk by adding or removing it.
     *
     * @param chunk chunk
     * @param add true to add it, false to remove it
     */
    public void updateChunk(ChunkData chunk, boolean add) {
        if (add) {
            this.chunks.add(chunk);

            ClaimInformation.CHUNK_CACHE.put(chunk, uuid);
        } else {
            this.chunks.remove(chunk);

            ClaimInformation.CHUNK_CACHE.remove(chunk);
        }
    }

    /**
     * Add a level to the current level.
     */
    public void addLevel() {
        this.buyLevel++;
    }

    /**
     * Check if the chunk is claimed.
     *
     * @param chunk chunk
     * @return true if the chunk is claimed, otherwise false
     */
    public boolean contains(ChunkData chunk) {
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

        for (ChunkData chunk : chunks) {
            JsonObject chunkObject = new JsonObject();
            chunkObject.put("x", chunk.getX());
            chunkObject.put("z", chunk.getZ());

            array.add(chunkObject);
        }

        object.put("chunks", array);
        object.put("level", buyLevel);

        return object;
    }

    /**
     * Get the uuid.
     *
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Get the buy level.
     *
     * @return buy level
     */
    public int getBuyLevel() {
        return buyLevel;
    }

    /**
     * Create a new claim information.
     *
     * @param uuid player uuid
     * @param chunks claimed chunks
     * @param level level
     */
    public static void create(UUID uuid, List<ChunkData> chunks, int level) {
        ClaimInformation information = new ClaimInformation(uuid, chunks, level);

        ClaimInformation.CACHE.put(uuid, information);

        for (ChunkData chunk : chunks) {
            ClaimInformation.CHUNK_CACHE.put(chunk, uuid);
        }
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
     * Check if two chunks are owned by the same player.
     *
     * @param firstChunk first chunk
     * @param secondChunk second chunk
     * @return true if the same player owns the two chunks, otherwise false
     */
    public static boolean hasSameOwner(Chunk firstChunk, Chunk secondChunk) {
        ChunkData firstData = new ChunkData(firstChunk);
        ChunkData secondData = new ChunkData(secondChunk);

        UUID firstOwner = ClaimInformation.CHUNK_CACHE.get(firstData);
        UUID secondOwner = ClaimInformation.CHUNK_CACHE.get(secondData);

        if (firstOwner != null && secondOwner != null) {
            return firstOwner.equals(secondOwner);
        }

        return false;
    }

    /**
     * Check if the player owns the chunk.
     *
     * @param player player
     * @param chunk chunk
     * @return true if the player owns the chunk, otherwise false
     */
    public static boolean owns(Player player, Chunk chunk) {
        ClaimInformation information = ClaimInformation.get(player.getUniqueId());

        return information.contains(new ChunkData(chunk));
    }

    /**
     * Check if the chunk is claimed by another player.
     *
     * @param player player
     * @param chunk chunk
     * @return true if the chunk is claimed by another player, otherwise false
     */
    public static boolean isClaimedByOthers(Player player, Chunk chunk) {
        UUID uuid = ClaimInformation.CHUNK_CACHE.get(new ChunkData(chunk));

        return uuid != null && !uuid.equals(player.getUniqueId());
    }

    /**
     * Clear the cache.
     */
    public static void clear() {
        ClaimInformation.CACHE.clear();
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
