package me.glaremasters.multieconomy.configuration.sections;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class PluginSettings implements SettingsHolder {

    @Comment("Would you like to check for plugin updates on startup? It's highly suggested you keep this enabled!")
    public static final Property<Boolean> UPDATE_CHECK =
            newProperty("settings.update-check", true);

    private PluginSettings() {
    }

    @Override
    public void registerComments(CommentsConfiguration conf) {
        String[] pluginHeader = {
                "MultiEconomy",
                "Creator: Glare",
                "Contributors: https://github.com/darbyjack/MultiEconomy/graphs/contributors",
                "Issues: https://github.com/darbyjack/MultiEconomy/issues",
                "Spigot: https://www.spigotmc.org/resources/57245/",
                "Discord: https://glaremasters.me/discord"
        };
        conf.setComment("settings", pluginHeader);
    }

}
