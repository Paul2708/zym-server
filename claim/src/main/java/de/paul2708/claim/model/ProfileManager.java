package de.paul2708.claim.model;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import de.paul2708.claim.model.chunk.ChunkData;
import de.paul2708.claim.model.chunk.ChunkWrapper;
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

    private Map<UUID, List<ChunkWrapper>> accessMap;

    /**
     * Create a new profile manager with empty sets.
     */
    private ProfileManager() {
        this.profiles = new HashSet<>();
        this.cityChunks = new HashSet<>();

        this.accessMap = new HashMap<>();
    }

    /**
     * Add a profile to the set.
     *
     * @param profile claim profile
     */
    public void addProfile(ClaimProfile profile) {
        profiles.add(profile);
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
     * @param cityChunk city chunk
     */
    public void removeCityChunk(CityChunk cityChunk) {
        cityChunks.remove(cityChunk);
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

        ChunkWrapper chunkWrapper = new ChunkWrapper(chunk);

        // Check cache
        List<ChunkWrapper> list = accessMap.getOrDefault(player.getUniqueId(), new ArrayList<>());

        if (list.contains(chunkWrapper)) {
            return true;
        }

        // Check claim profiles
        for (ClaimProfile profile : profiles) {
            for (ChunkData claimedChunk : profile.getClaimedChunks()) {
                if (claimedChunk.getWrapper().equals(chunkWrapper)) {
                    if (profile.getUuid().equals(player.getUniqueId())) {
                        List<ChunkWrapper> wrappers = accessMap.getOrDefault(player.getUniqueId(), new ArrayList<>());
                        wrappers.add(claimedChunk.getWrapper());

                        accessMap.put(player.getUniqueId(), wrappers);
                        return true;
                    } else {
                        if (claimedChunk.isGroupChunk()) {
                            if (getProfile(player).getAccess().contains(claimedChunk)) {
                                List<ChunkWrapper> wrappers = accessMap.getOrDefault(player.getUniqueId(), new ArrayList<>());
                                wrappers.add(claimedChunk.getWrapper());

                                accessMap.put(player.getUniqueId(), wrappers);
                                return true;
                            }
                        }

                        return false;
                    }
                }
            }
        }

        // Check city chunk
        for (CityChunk cityChunk : cityChunks) {
            if (cityChunk.getChunkData().getWrapper().equals(chunkWrapper)) {
                if (cityChunk.isInteractable()) {
                    List<ChunkWrapper> wrappers = accessMap.getOrDefault(player.getUniqueId(), new ArrayList<>());
                    wrappers.add(cityChunk.getChunkData().getWrapper());

                    accessMap.put(player.getUniqueId(), wrappers);
                    return true;
                } else {
                    return false;
                }
            }
        }

        // Check access
        for (ChunkData access : getProfile(player).getAccess()) {
            if (access.getWrapper().equals(chunkWrapper)) {
                List<ChunkWrapper> wrappers = accessMap.getOrDefault(player.getUniqueId(), new ArrayList<>());
                wrappers.add(access.getWrapper());

                accessMap.put(player.getUniqueId(), wrappers);
                return true;
            }
        }

        List<ChunkWrapper> wrappers = accessMap.getOrDefault(player.getUniqueId(), new ArrayList<>());
        wrappers.add(chunkWrapper);

        accessMap.put(player.getUniqueId(), wrappers);
        return true;
    }

    /**
     * Clear access.
     *
     * @param chunk chunk
     */
    public void clearAccess(Chunk chunk) {
        ChunkWrapper wrapper = new ChunkWrapper(chunk);

        for (ClaimProfile profile : profiles) {
            List<ChunkWrapper> wrappers = accessMap.getOrDefault(profile.getUuid(), new ArrayList<>());

            if (wrappers.isEmpty()) {
                continue;
            }

            if (wrappers.contains(wrapper)) {
                wrappers.remove(wrapper);
                accessMap.put(profile.getUuid(), wrappers);
            }
        }
    }

    /**
     * Remove chunk access from a chunk.
     *
     * @param chunk chunk that has been removed
     */
    public void removeChunkAccess(Chunk chunk) {
        ChunkWrapper chunkWrapper = new ChunkWrapper(chunk);

        for (ClaimProfile profile : profiles) {
            List<ChunkData> list = new ArrayList<>();

            for (ChunkData access : profile.getAccess()) {
                if (access.getWrapper().equals(chunkWrapper)) {
                    list.add(access);
                }
            }

            profile.getAccess().removeAll(list);
        }
    }

    /**
     * Check if a player can claim a chunk.<br>
     * A chunk can be claimed, if the chunk isn't claimed yet, if the chunks next to it is not claimed and there is
     * no region.
     *
     * @param player player
     * @param chunk chunk to claim
     * @param group group chunk or not
     * @return claim response
     */
    public ClaimResponse canClaim(Player player, Chunk chunk, boolean group) {
        // Check chunk
        if (isClaimed(chunk)) {
            return ClaimResponse.ALREADY_CLAIMED;
        }

        // Check other chunks
        for (ChunkData chunkData : getNextChunks(chunk)) {
            if (getClaimType(fromWrapper(chunkData.getWrapper())) == ClaimType.PLAYER) {
                ClaimProfile profile = getProfile(fromWrapper(chunkData.getWrapper()));

                if (profile.getUuid().equals(player.getUniqueId())) {
                    break;
                } else if (!profile.getUuid().equals(player.getUniqueId()) && !chunkData.isGroupChunk()) {
                    return ClaimResponse.BORDER;
                }
            }
        }

        for (ChunkData chunkData : getNextChunks(chunk)) {
            if (getClaimType(fromWrapper(chunkData.getWrapper())) == ClaimType.PLAYER) {
                ClaimProfile profile = getProfile(fromWrapper(chunkData.getWrapper()));

                if (profile.getUuid().equals(player.getUniqueId())) {
                    if (chunkData.isGroupChunk()) {
                        if (group) {
                            return ClaimResponse.CLAIMABLE;
                        } else {
                            return ClaimResponse.GROUP_CHUNK;
                        }
                    } else {
                        if (group) {
                            return ClaimResponse.GROUP_CHUNK;
                        } else {
                            return ClaimResponse.CLAIMABLE;
                        }
                    }
                } else {
                    if (chunkData.isGroupChunk()) {
                        return ClaimResponse.CLAIMABLE;
                    }
                }
            }
        }

        // Check existing regions
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(
                BukkitAdapter.adapt(player.getWorld()));

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
     * Check if the two chunks have the same owner - either player or city.
     *
     * @param first first chunk
     * @param second second chunk
     * @return true if the chunks have the same owners, otherwise false
     */
    public boolean hasSameOwner(Chunk first, Chunk second) {
        ChunkWrapper firstChunkWrapper = new ChunkWrapper(first);
        ChunkWrapper secondChunkWrapper = new ChunkWrapper(second);

        boolean firstChunk = false;
        boolean secondChunk = false;

        // Check same owner
        for (ClaimProfile profile : profiles) {
            for (ChunkData claimedChunk : profile.getClaimedChunks()) {
                if (claimedChunk.getWrapper().equals(firstChunkWrapper)) {
                    firstChunk = true;
                }
                if (claimedChunk.getWrapper().equals(secondChunkWrapper)) {
                    secondChunk = true;
                }
            }

            if (firstChunk && secondChunk) {
                return true;
            }

            firstChunk = false;
            secondChunk = false;
        }

        // Check city chunks
        for (CityChunk cityChunk : cityChunks) {
            if (cityChunk.getChunkData().getWrapper().equals(firstChunkWrapper)) {
                firstChunk = true;
            }
            if (cityChunk.getChunkData().getWrapper().equals(secondChunkWrapper)) {
                secondChunk = true;
            }
        }

        if (firstChunk && secondChunk) {
            return true;
        }

        return false;
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
     * Check if the chunk is claimed or not.
     *
     * @param chunk chunk
     * @return true if the chunk is claimed, otherwise false
     */
    public boolean isClaimed(Chunk chunk) {
        return getClaimType(chunk) != ClaimType.UNCLAIMED;
    }

    /**
     * Get the claim type by chunk.
     *
     * @param chunk chunk
     * @return claim type
     */
    public ClaimType getClaimType(Chunk chunk) {
        ChunkWrapper chunkWrapper = new ChunkWrapper(chunk);

        for (ClaimProfile profile : profiles) {
            for (ChunkData claimedChunk : profile.getClaimedChunks()) {
                if (claimedChunk.getWrapper().equals(chunkWrapper)) {
                    return ClaimType.PLAYER;
                }
            }
        }

        for (CityChunk cityChunk : cityChunks) {
            if (cityChunk.getChunkData().getWrapper().equals(chunkWrapper)) {
                return ClaimType.CITY;
            }
        }

        return ClaimType.UNCLAIMED;
    }

    /**
     * Get chunk data by chunk.
     *
     * @param chunk chunk
     * @return chunk data
     */
    public ChunkData getChunkData(Chunk chunk) {
        ChunkWrapper chunkWrapper = new ChunkWrapper(chunk);

        for (ClaimProfile profile : profiles) {
            for (ChunkData claimedChunk : profile.getClaimedChunks()) {
                if (claimedChunk.getWrapper().equals(chunkWrapper)) {
                    return claimedChunk;
                }
            }
        }

        throw new IllegalStateException("This method shouldn't return null. Did you use #getClaimType?");
    }

    /**
     * Get the city chunk data by chunk.
     *
     * @param chunk chunk
     * @return chunk data
     */
    public CityChunk getCityChunk(Chunk chunk) {
        ChunkWrapper chunkWrapper = new ChunkWrapper(chunk);

        for (CityChunk cityChunk : cityChunks) {
            if (cityChunk.getChunkData().getWrapper().equals(chunkWrapper)) {
                return cityChunk;
            }
        }

        throw new IllegalStateException("This method shouldn't return null. Did you use #getClaimType?");
    }

    /**
     * Get the claim profile by chunk.
     *
     * @param chunk chunk
     * @return claim profile
     */
    public ClaimProfile getProfile(Chunk chunk) {
        ChunkWrapper chunkWrapper = new ChunkWrapper(chunk);

        for (ClaimProfile profile : profiles) {
            for (ChunkData claimedChunk : profile.getClaimedChunks()) {
                if (claimedChunk.getWrapper().equals(chunkWrapper)) {
                    return profile;
                }
            }
        }

        throw new IllegalStateException("This method shouldn't return null. Did you use #getClaimType?");
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
        for (ClaimProfile profile : profiles) {
            if (profile.getUuid().equals(uuid)) {
                return profile;
            }
        }

        return null;
    }

    /**
     * Get the set of profiles.
     *
     * @return set of profiles
     */
    public Set<ClaimProfile> getProfiles() {
        return profiles;
    }

    /**
     * Get a list of all near chunks, that are claimed.
     *
     * @param chunk center chunk
     * @return list of chunks
     */
    private List<ChunkData> getNextChunks(Chunk chunk) {
        ChunkWrapper chunkWrapper = new ChunkWrapper(chunk);
        List<ChunkData> list = new LinkedList<>();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                ChunkWrapper nextChunk = new ChunkWrapper(chunkWrapper.getWorld(),
                        chunkWrapper.getX() + x, chunkWrapper.getZ() + z);

                if (isClaimed(fromWrapper(nextChunk))) {
                    list.add(getChunkData(fromWrapper(nextChunk)));
                }
            }
        }

        return list;
    }

    /**
     * Get the Bukkit chunk from the chunk wrapper.
     *
     * @param chunkWrapper chunk wrapper
     * @return bukkit chunk
     */
    private Chunk fromWrapper(ChunkWrapper chunkWrapper) {
        return Bukkit.getWorld(chunkWrapper.getWorld()).getChunkAt(chunkWrapper.getX(), chunkWrapper.getZ());
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
