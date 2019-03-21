package de.paul2708.claim.model;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import de.paul2708.claim.model.chunk.ChunkData;
import de.paul2708.claim.model.chunk.CityChunk;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This singleton holds information about every claimed chunk and its owner.
 *
 * @author Paul2708
 */
public final class ProfileManager {

    private static ProfileManager instance;

    private Set<ClaimProfile> profiles;
    private Set<CityChunk> cityChunks;

    private Map<UUID, ClaimProfile> profileMap;
    private Map<ChunkData, ClaimProfile> chunkMap;
    private Map<Chunk, ChunkData> chunkDataMap;

    /**
     * Create a new profile manager with empty sets.
     */
    private ProfileManager() {
        this.profiles = new HashSet<>();
        this.cityChunks = new HashSet<>();

        this.profileMap = new HashMap<>();
        this.chunkMap = new HashMap<>();
        this.chunkDataMap = new HashMap<>();
    }

    /**
     * Add a profile to the set.
     *
     * @param profile claim profile
     */
    public void addProfile(ClaimProfile profile) {
        profiles.add(profile);

        this.profileMap.put(profile.getUuid(), profile);
    }

    /**
     * Update the claim profile by updating hash maps.
     *
     * @param profile profile to update
     */
    public void update(ClaimProfile profile) {
        for (ChunkData claimedChunk : profile.getClaimedChunks()) {
            this.chunkMap.put(claimedChunk, profile);

            Chunk chunk = Bukkit.getWorld(claimedChunk.getWorld()).getChunkAt(claimedChunk.getX(), claimedChunk.getZ());
            this.chunkDataMap.put(chunk, claimedChunk);
        }

        this.profileMap.put(profile.getUuid(), profile);
    }

    /**
     * Add a city chunk to the set.
     *
     * @param cityChunk city chunk
     */
    public void addCityChunk(CityChunk cityChunk) {
        cityChunks.add(cityChunk);
    }

    /**
     * Remove a city chunk.
     *
     * @param chunkId chunk id
     */
    public void removeCityChunk(int chunkId) {
        CityChunk marked = null;
        for (CityChunk cityChunk : cityChunks) {
            if (cityChunk.getChunkData().getId() == chunkId) {
                marked = cityChunk;
                break;
            }
        }

        if (marked != null) {
            cityChunks.remove(marked);
        }
    }

    /**
     * Check if the player has access on the given chunk.
     * The player has access on it, if:<br>
     *     - bypass is enabled<br>
     *     - he owns it<br>
     *     - he has access (group chunk)<br>
     *     - it's a city chunk and it's interactable<br>
     *     - the chunk is unclaimed
     *
     * @param player interacted player
     * @param chunk interacted chunk
     * @return true if the player has access, otherwise false
     */
    public boolean hasAccess(Player player, Chunk chunk) {
        // Check bypass
        if (player.hasMetadata("bypass")) {
            return true;
        }

        ChunkData data = new ChunkData(chunk);

        // Check claim profiles
        for (ClaimProfile profile : profiles) {
            if (profile.getClaimedChunks().contains(data)) {
                return profile.getUuid().equals(player.getUniqueId());
            }
        }
        // Has access
        if (getProfile(player).getAccess().contains(data)) {
            return true;
        }

        // Check city chunk
        for (CityChunk cityChunk : cityChunks) {
            if (cityChunk.getChunkData().equals(data)) {
                return cityChunk.isInteractable();
            }
        }

        return true;
    }

    /**
     * Check if a player can claim a chunk.<br>
     * A chunk can be claimed, if the chunk isn't claimed yet, if the chunks next to it is not claimed and there is
     * no region.
     *
     * @param player player
     * @param chunkData chunk to claim
     * @return claim response
     */
    public ClaimResponse canClaim(Player player, ChunkData chunkData) {
        // Check chunk
        for (ClaimProfile profile : profiles) {
            if (profile.getClaimedChunks().contains(chunkData)) {
                return ClaimResponse.ALREADY_CLAIMED;
            }
        }
        if (cityChunks.contains(chunkData)) {
            return ClaimResponse.ALREADY_CLAIMED;
        }

        // Check other chunks
        for (ClaimProfile profile : profiles) {
            if (profile.getUuid().equals(player.getUniqueId())) {
                continue;
            }

            for (ChunkData chunk : profile.getClaimedChunks()) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        ChunkData nextChunk = new ChunkData(chunk.getWorld(), chunk.getX() + x, chunk.getZ() + z);

                        if (chunkData.equals(nextChunk) && !hasChunkNextTo(player, chunkData)) {
                            return ClaimResponse.BORDER;
                        }
                    }
                }
            }
        }

        // Check existing regions
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(
                BukkitAdapter.adapt(player.getWorld()));

        Chunk chunk = player.getLocation().getChunk();
        int bx = chunk.getX() << 4;
        int bz = chunk.getZ() << 4;
        BlockVector pt1 = new BlockVector(bx, 0, bz);
        BlockVector pt2 = new BlockVector(bx + 15, 256, bz + 15);

        ProtectedCuboidRegion region = new ProtectedCuboidRegion("ThisIsAnId", pt1, pt2);
        ApplicableRegionSet regions = regionManager.getApplicableRegions(region);

        if (regions.size() > 0) {
            return ClaimResponse.REGION;
        }

        return ClaimResponse.CLAIMABLE;
    }

    /**
     * Check if a player has the chunk next to it.
     *
     * @param player player
     * @param chunkData chunk to check
     * @return true if player owns it, otherwise false
     */
    private boolean hasChunkNextTo(Player player, ChunkData chunkData) {
        for (ChunkData chunk : getProfile(player).getClaimedChunks()) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    ChunkData nextChunk = new ChunkData(chunk.getWorld(), chunk.getX() + x, chunk.getZ() + z);

                    if (chunkData.equals(nextChunk)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check if the two chunks have the same owner - either player or city.
     *
     * @param first first chunk
     * @param second second chunk
     * @return true if the chunks have the same owners, otherwise false
     */
    public boolean hasSameOwner(Chunk first, Chunk second) {
        ChunkData firstChunkData = new ChunkData(first);
        ChunkData secondChunkData = new ChunkData(second);

        for (ClaimProfile profile : profiles) {
            if (profile.getClaimedChunks().contains(firstChunkData) && profile.getClaimedChunks().contains(secondChunkData)) {
                return true;
            }
        }
        if (cityChunks.contains(firstChunkData) && cityChunks.contains(secondChunkData)) {
            return true;
        }

        return false;
    }

    /**
     * Get the chunk data by chunk.
     *
     * @param chunk chunk
     * @return chunk data
     */
    public ChunkData getChunkData(Chunk chunk) {
        return chunkDataMap.get(chunk);
    }

    /**
     * Check if a chunk is claimed - weather by player or city.
     *
     * @param chunk chunk
     * @return true if the chunk is claimed, otherwise false
     */
    public boolean isClaimed(Chunk chunk) {
        ChunkData chunkData = new ChunkData(chunk);

        return chunkMap.containsKey(chunkData);
    }

    /**
     * Get the owner as uuid from a chunk.
     *
     * @param chunk chunk
     * @return owner uuid or null if there isn't an owner
     */
    public UUID getOwner(Chunk chunk) {
        ChunkData chunkData = new ChunkData(chunk);

        if (chunkMap.containsKey(chunkData)) {
            return chunkMap.get(chunkData).getUuid();
        } else if (cityChunks.contains(chunkData)) {
            return CityChunk.OWNER;
        }

        return null;
    }

    /**
     * Check if a profile exists.
     *
     * @param uuid uuid
     * @return true if a profile exists, otherwise false
     */
    public boolean doesProfileExist(UUID uuid) {
        return getProfile(uuid) != null;
    }

    /**
     * Get the city chunk data by chunk.
     *
     * @param chunk chunk
     * @return chunk data
     */
    public CityChunk getCityChunk(Chunk chunk) {
        for (CityChunk cityChunk : cityChunks) {
            if (cityChunk.getChunkData().equals(new ChunkData(chunk))) {
                return cityChunk;
            }
        }

        return null;
    }

    /**
     * Get the claim profile by chunk.
     *
     * @param chunk chunk
     * @return claim profile or null if none was found
     */
    public ClaimProfile getProfile(Chunk chunk) {
        return chunkMap.get(new ChunkData(chunk));
    }

    /**
     * Get the claim profile by player.
     *
     * @param player player
     * @return claim profile or null if none was found
     */
    public ClaimProfile getProfile(Player player) {
        return this.getProfile(player.getUniqueId());
    }

    /**
     * Get the claim profile by uuid.
     *
     * @param uuid uuid
     * @return claim profile or null if none was found
     */
    public ClaimProfile getProfile(UUID uuid) {
        return profileMap.getOrDefault(uuid, null);
    }

    /**
     * Get a set of all claim profiles.
     *
     * @return set of profiles
     */
    public Set<ClaimProfile> getProfiles() {
        return profiles;
    }

    /**
     * Get the profile manager instance.
     *
     * @return profile manager
     */
    public static ProfileManager getInstance() {
        if (ProfileManager.instance == null) {
            ProfileManager.instance = new ProfileManager();
        }

        return instance;
    }
}
