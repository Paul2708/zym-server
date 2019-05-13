package de.paul2708.claim.item;

/**
 * This enum holds possible claimer types.
 *
 * @author Paul2708
 */
public enum ClaimerType {

    /**
     * The claimer is a normal chunk claimer and can be used to claim personal chunks.
     */
    NORMAL,

    /**
     * The claimer is a group chunk claimer and can be used to claim group chunks.
     */
    GROUP,

    /**
     * The given item stack is no claimer.
     */
    NONE;
}
