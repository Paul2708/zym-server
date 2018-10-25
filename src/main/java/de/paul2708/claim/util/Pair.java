package de.paul2708.claim.util;

/**
 * This class represents a pair with key and value.
 *
 * @param <K> key type
 * @param <V> value type
 * @author Paul2708
 */
public class Pair<K, V> {

    private K key;
    private V value;

    /**
     * Create a new pair with key and value.
     *
     * @param key key
     * @param value value
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the key.
     *
     * @return key
     */
    public K getKey() {
        return key;
    }

    /**
     * Get the value.
     *
     * @return value
     */
    public V getValue() {
        return value;
    }
}
