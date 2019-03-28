package de.paul2708.claim.model;

import de.paul2708.claim.model.chunk.ChunkData;
import de.paul2708.claim.model.identifier.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class holds information about a player and its claimed chunks.
 *
 * @author Paul2708
 */
public class ClaimProfile extends Identifier {

    private UUID uuid;

    private int claimer;

    private List<ChunkData> claimedChunks;
    private List<ChunkData> access;

    /**
     * Create a new claim profile with all required information.
     *
     * @param uuid player uuid
     * @param claimer amount of bought claimers
     */
    public ClaimProfile(UUID uuid, int claimer) {
        this.uuid = uuid;
        this.claimer = claimer;
        this.claimedChunks = new ArrayList<>();
        this.access = new ArrayList<>();
    }

    /**
     * Add a claimed chunk to the list.
     *
     * @param chunk chunk
     */
    public void addClaimedChunk(ChunkData chunk) {
        this.claimedChunks.add(chunk);
    }

    /**
     * Remove a claimed chunk from the list.
     *
     * @param chunk chunk
     */
    public void removeClaimedChunk(ChunkData chunk) {
        this.claimedChunks.remove(chunk);
    }

    /**
     * Add an accessible chunk to the list.
     *
     * @param chunk chunk
     */
    public void addAccess(ChunkData chunk) {
        this.access.add(chunk);
    }

    /**
     * Remove an accessible chunk from the list.
     *
     * @param chunk chunk
     */
    public void removeAccess(ChunkData chunk) {
        this.access.remove(chunk);
    }

    /**
     * Set the amount of bought claimers.
     *
     * @param claimer new amount of claimer
     */
    public void setClaimer(int claimer) {
        this.claimer = claimer;
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
     * Get the amount of bought claimers.
     *
     * @return claimers
     */
    public int getClaimer() {
        return claimer;
    }

    /**
     * Get a list of all claimed chunks.
     *
     * @return list of claimed chunks
     */
    public List<ChunkData> getClaimedChunks() {
        return claimedChunks;
    }

    /**
     * Get a list of all accessible chunks.
     *
     * @return list of accessible chunks
     */
    public List<ChunkData> getAccess() {
        return access;
    }
}
