package me.glaremasters.multieconomy.configuration;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.migration.PlainMigrationService;
import ch.jalu.configme.resource.PropertyReader;

public class MultiEconomyMigrationService extends PlainMigrationService {

    /**
     * Checks if migrations need to be done
     * @param reader the property reader
     * @param configurationData the data to check
     * @return migrate or not
     */
    @Override
    protected boolean performMigrations(PropertyReader reader, ConfigurationData configurationData) {
        return hasDeprecatedProperties(reader);
    }

    /**
     * Check if config has old paths
     * @param reader the reader
     * @return old paths or not
     */
    private static boolean hasDeprecatedProperties(PropertyReader reader) {
        String[] deprecatedProperties = {
                "messages",
        };
        for (String deprecatedPath :deprecatedProperties) {
            if (reader.contains(deprecatedPath)) {
                return true;
            }
        }
        return false;
    }
}
