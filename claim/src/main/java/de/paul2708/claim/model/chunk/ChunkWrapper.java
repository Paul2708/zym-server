package de.paul2708.claim.model.chunk;

import org.bukkit.Chunk;

/**
 * This class wraps a Bukkit chunk and reduce it to the simplest attributes.
 *
 * @author Paul2708
 */
public class ChunkWrapper {

    private final String world;
    private final int x;
    private final int z;

    /**
     * Create a new chunk wrapper.
     *
     * @param chunk chunk
     */
    public ChunkWrapper(Chunk chunk) {
        this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    /**
     * Create a new chunk wrapper.
     *
     * @param world world name
     * @param x x coordinate
     * @param z y coordinate
     */
    public ChunkWrapper(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    /**
     * Get the world name.
     *
     * @return world
     */
    public String getWorld() {
        return world;
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
     * Check if two chunk data are equal. They are equal, if the coordinates and the world are the same.
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

        ChunkWrapper chunkData = (ChunkWrapper) o;

        return world.equalsIgnoreCase(chunkData.world) && x == chunkData.x && z == chunkData.z;
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
