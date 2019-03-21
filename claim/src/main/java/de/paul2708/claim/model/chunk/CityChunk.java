package de.paul2708.claim.model.chunk;

import de.paul2708.claim.model.identifier.Identifier;

import java.util.UUID;

/**
 * This class is a city chunk and holds the information, if non-op players can interact on it.
 *
 * @author Paul2708
 */
public class CityChunk extends Identifier {

    /**
     * Owner uuid of city chunks.
     */
    public static final UUID OWNER = new UUID(0, 0);

    private ChunkData chunkData;
    private boolean interactable;

    /**
     * Create a new city chunk.
     *
     * @param chunkData chunk data
     * @param interactable indicates weather the chunk is interactable or not
     */
    public CityChunk(ChunkData chunkData, boolean interactable) {
        this.chunkData = chunkData;
        this.interactable = interactable;
    }

    /**
     * Get chunk data.
     *
     * @return chunk data
     */
    public ChunkData getChunkData() {
        return chunkData;
    }

    /**
     * Check if the chunk is interactable for non-op players.
     *
     * @return true if the chunk is interactable, otherwise false
     */
    public boolean isInteractable() {
        return interactable;
    }

    /**
     * Check if two city chunks are equal.
     *
     * @param o object
     * @return true if the object is equal to the city chunk, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CityChunk cityChunk = (CityChunk) o;

        if (interactable != cityChunk.interactable) {
            return false;
        }

        return chunkData != null ? chunkData.equals(cityChunk.chunkData) : cityChunk.chunkData == null;
    }

    /**
     * Get the hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = chunkData != null ? chunkData.hashCode() : 0;
        result = 31 * result + (interactable ? 1 : 0);
        return result;
    }
}
