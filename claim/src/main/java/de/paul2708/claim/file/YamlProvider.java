package de.paul2708.claim.file;

import java.io.File;
import java.io.IOException;

/**
 * This interface represents a yaml configuration.
 *
 * @author Paul2708
 */
public interface YamlProvider {

    /**
     * Load the configuration by file.
     *
     * @param file file to load by
     * @throws IOException if loading throws any exceptions
     */
    void load(File file) throws IOException;

    /**
     * Set the value by key.
     *
     * @param key key
     * @param value value
     */
    void set(String key, Object value);

    /**
     * Get an generic object by key.
     *
     * @param key key
     * @param <T> expected value type
     * @return value
     */
    <T> T get(String key);

    /**
     * Save the configuration to file.
     *
     * @param file file to save
     * @throws IOException if saving throws any exceptions
     */
    void save(File file) throws IOException;
}
