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

    /**
     * Check if a pair is equal to the other one.
     *
     * @param o object
     * @return true if the key and vaule is the same, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (key != null ? !key.equals(pair.key) : pair.key != null) {
            return false;
        }

        return value != null ? value.equals(pair.value) : pair.value == null;
    }

    /**
     * Get the hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);

        return result;
    }
}
