package de.paul2708.claim.model;

/**
 * This enum holds all possible types a chunk can be claimed.
 *
 * @author Paul2708
 */
public enum ClaimType {

    /**
     * The given chunk is claimed by a player. Can be used as group chunk.
     */
    PLAYER,

    /**
     * The given chunk is claimed by the city.
     */
    CITY,

    /**
     * The given chunk is unclaimed.
     */
    UNCLAIMED;
}
