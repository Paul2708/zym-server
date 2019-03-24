package de.paul2708.claim.model.chunk;

import de.paul2708.claim.model.identifier.Identifier;

/**
 * This class holds data about a chunk.
 *
 * @author Paul2708
 */
public class ChunkData extends Identifier {

    private ChunkWrapper wrapper;
    private boolean groupChunk;

    /**
     * Create a new chunk data based world, x, z and group chunk value.
     *
     * @param world world
     * @param x chunk x coordinate
     * @param z chunk z coordinate
     * @param groupChunk true if the chunk is a group chunk, otherwise false
     */
    public ChunkData(String world, int x, int z, boolean groupChunk) {
        this(new ChunkWrapper(world, x, z), groupChunk);
    }

    /**
     * Create a new chunk data.
     *
     * @param wrapper chunk wrapper
     * @param groupChunk true if the chunk is a group chunk, otherwise false
     */
    public ChunkData(ChunkWrapper wrapper, boolean groupChunk) {
        this.wrapper = wrapper;
        this.groupChunk = groupChunk;
    }

    /**
     * Check if the chunk is a group chunk.
     *
     * @return true if the chunk is a group chunk, otherwise false
     */
    public boolean isGroupChunk() {
        return groupChunk;
    }

    /**
     * Get the chunk wrapper.
     *
     * @return chunk wrapper
     */
    public ChunkWrapper getWrapper() {
        return wrapper;
    }

    /**
     * Two chunk data are equal, if the wrapper is the same.
     *
     * @param o object
     * @return true if they are equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChunkData chunkData = (ChunkData) o;

        return wrapper != null ? wrapper.equals(chunkData.wrapper) : chunkData.wrapper == null;
    }

    /**
     * Get the hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return wrapper != null ? wrapper.hashCode() : 0;
    }
}
