package de.paul2708.claim.file;

import de.paul2708.claim.file.condition.Condition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * This abstract class models a basic configuration, based on an abstract YAML provider.
 *
 * @author Paul2708
 */
public abstract class AbstractConfiguration {

    private YamlProvider yamlProvider;
    private String path;

    private File file;

    private List<ConfigurationElement<?>> elements;

    /**
     * Create a new configuration with path. The path should end on <code>.yml</code>.
     *
     * @param provider yaml provider
     * @param path path to the file
     */
    public AbstractConfiguration(YamlProvider provider, String path) {
        this.yamlProvider = provider;
        this.path = path;

        this.elements = new LinkedList<>();
    }

    /**
     * Load the configuration, create files and set the default values.
     *
     * @throws InvalidValueException if a value is invalid or missing
     */
    public void load() throws InvalidValueException {
        try {
            Path path = Paths.get(this.path);
            this.file = path.toFile();

            if (Files.notExists(path)) {
                if (path.getParent() != null && Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.createFile(path);
            }

            yamlProvider.load(file);

            this.defaultValue();

            this.save();

            this.check();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the default configuration values.
     *
     * @see #setDefault(String, Object, Condition)
     */
    public abstract void defaultValue();

    /**
     * Set a key and value as default.
     *
     * @param key key
     * @param object value
     * @param condition condition
     * @param <T> value type
     */
    public <T> void setDefault(String key, T object, Condition<T> condition) {
        elements.add(new ConfigurationElement<>(key, condition, (Class<T>) object.getClass()));

        if (yamlProvider.get(key) == null) {
            this.set(key, object);
        }
    }

    /**
     * Set a value to a key and save the file.
     *
     * @param key key
     * @param value value
     * @param <T> value type
     */
    public <T> void set(String key, T value) {
        yamlProvider.set(key, value);

        this.save();
    }

    /**
     * Get the value by key.
     *
     * @param key key
     * @param <T> value type
     * @return value
     */
    public <T> T get(String key) {
        return (T) yamlProvider.get(key);
    }

    /**
     * Save the configuration file.
     */
    public void save() {
        try {
            yamlProvider.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check all keys and values, if they are valid.
     *
     * @throws InvalidValueException if the values are invalid
     */
    private void check() throws InvalidValueException {
        for (ConfigurationElement<?> element : elements) {
            if (yamlProvider.get(element.getKey()) == null) {
                throw new InvalidValueException(element.getKey(), "key and value for " + element.getKey()
                        + " is missing");
            }

            element.validate(yamlProvider.get(element.getKey()));
        }
    }
}
