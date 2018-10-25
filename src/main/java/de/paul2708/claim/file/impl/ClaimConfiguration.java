package de.paul2708.claim.file.impl;

import de.paul2708.claim.file.AbstractConfiguration;
import de.paul2708.claim.file.condition.Condition;
import org.bukkit.Material;

/**
 * This configuration is for the claiming.
 *
 * @author Paul2708
 */
public class ClaimConfiguration extends AbstractConfiguration {

    /**
     * Create a new claim config.
     */
    public ClaimConfiguration() {
        super(new SpigotYamlProvider(), "plugins/Chunk Claim/config.yml");
    }

    /**
     * Create the default configuration values.
     */
    @Override
    public void defaultValue() {
        setDefault("item.type", Material.DIAMOND.toString(), new Condition<String>() {

            @Override
            public boolean fulfill(String object) {
                return Material.matchMaterial(object) != null;
            }

            @Override
            public String description() {
                return "correct material type (see https://minecraftitemids.com)";
            }
        });
        setDefault("item.amount", 16, new Condition<Integer>() {

            @Override
            public boolean fulfill(Integer object) {
                return object > 0;
            }

            @Override
            public String description() {
                return "amount has to be positive";
            }
        });
    }
}
