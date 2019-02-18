package de.paul2708.claim.model;

import org.bukkit.Chunk;

/**
 * This class holds data about a chunk.
 *
 * @author Paul2708
 */
public class ChunkData {

    private final String world;
    private final int x;
    private final int z;

    /**
     * Create a new chunk data.
     *
     * @param chunk chunk
     */
    public ChunkData(Chunk chunk) {
        this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    /**
     * Create a new chunk data.
     *
     * @param world world name
     * @param x x coordinate
     * @param z y coordinate
     */
    public ChunkData(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    /**
     * Get the x coordinate.
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get the z coordinate
     *
     * @return z coordinate
     */
    public int getZ() {
        return z;
    }

    /**
     * Get the world name.
     *
     * @return world name
     */
    public String getWorld() {
        return world;
    }

    /**
     * Check if two chunk data are equal. They are equal, if the coordinates are the same.
     *
     * @param o object
     * @return true if the objects are equal, otherwise false
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

        return world.equals(chunkData.world) && x == chunkData.x && z == chunkData.z;
    }

    /**
     * Get the hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = world != null ? world.hashCode() : 0;
        result = 31 * result + x;
        result = 31 * result + z;
        return result;
    }
}
