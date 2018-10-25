package de.paul2708.claim.file.impl;

import de.paul2708.claim.file.YamlProvider;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * This yaml provider provides the Spigot {@link YamlConfiguration}.
 *
 * @author Paul2708
 */
public class SpigotYamlProvider implements YamlProvider {

    private YamlConfiguration configuration;

    /**
     * Load the configuration by file.
     *
     * @param file file to load by
     */
    @Override
    public void load(File file) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Set the value by key.
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void set(String key, Object value) {
        this.configuration.set(key, value);
    }

    /**
     * Get an generic object by key.
     *
     * @param key key
     * @return value
     */
    @Override
    public <T> T get(String key) {
        if (!configuration.contains(key)) {
            return null;
        }

        return (T) configuration.get(key);
    }

    /**
     * Save the configuration to file.
     *
     * @param file file to save
     * @throws IOException if saving throws any exceptions
     */
    @Override
    public void save(File file) throws IOException {
        this.configuration.save(file);
    }
}
