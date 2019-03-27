package de.paul2708.claim.model;

/**
 * This enum holds information about claiming.
 *
 * @author Paul2708
 */
public enum ClaimResponse {

    /**
     * The chunk is already claimed.
     */
    ALREADY_CLAIMED,

    /**
     * A region is located in the chunk.
     */
    REGION,

    /**
     * The chunk is next to a claimed chunk.
     */
    BORDER,

    /**
     * The chunk is next to a group chunk.
     */
    GROUP_CHUNK,

    /**
     * The chunk can be claimed.
     */
    CLAIMABLE
}
