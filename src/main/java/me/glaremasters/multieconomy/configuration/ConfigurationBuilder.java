package me.glaremasters.multieconomy.configuration;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import me.glaremasters.multieconomy.configuration.sections.PluginSettings;

public class ConfigurationBuilder {

    private ConfigurationBuilder() {

    }

    public static ConfigurationData buildConfig() {
        return ConfigurationDataBuilder.createConfiguration(
                PluginSettings.class
        );
    }

}
