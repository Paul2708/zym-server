package de.paul2708.claim.file.impl;

import de.paul2708.claim.file.AbstractConfiguration;
import de.paul2708.claim.file.condition.Condition;
import de.paul2708.claim.file.condition.NoneCondition;

/**
 * This configuration is for the mysql connection.
 *
 * @author Paul2708
 */
public class MySQLConfiguration extends AbstractConfiguration {

    /**
     * Default password. Indicator weather the file was edited or not.
     */
    public static final String DEFAULT_PASSWORD = "my_bad_password";

    /**
     * Create a new mysql configuration.
     */
    public MySQLConfiguration() {
        super(new SpigotYamlProvider(), "plugins/Chunk Claim/mysql.yml");
    }

    /**
     * Create the default configuration values.
     */
    @Override
    public void defaultValue() {
        setDefault("host", "localhost", new NoneCondition<>());
        setDefault("port", 3306, new Condition<Integer>() {

            @Override
            public boolean fulfill(Integer object) {
                return object <= 65535 && object >= 0;
            }

            @Override
            public String description() {
                return "port must be a positive integer";
            }
        });
        setDefault("database", "zym", new NoneCondition<>());
        setDefault("user", "root", new NoneCondition<>());
        setDefault("password", MySQLConfiguration.DEFAULT_PASSWORD, new NoneCondition<>());
    }
}
