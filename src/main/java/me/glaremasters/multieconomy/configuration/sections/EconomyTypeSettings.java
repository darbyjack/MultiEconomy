package me.glaremasters.multieconomy.configuration.sections;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class EconomyTypeSettings implements SettingsHolder {

    @Comment("What are the economies you would like on your server?")
    public static final Property<List<String>> ECONOMY_TYPES =
            newListProperty("economy-types", "gems;Gems;$;100;EMERALD;0", "tokens;Tokens;€;25;GOLD_NUGGET;1", "crystals;Crystals;50;DIAMOND;2");

    private EconomyTypeSettings() {
    }

    @Override
    public void registerComments(CommentsConfiguration conf) {
        String[] pluginHeader = {
                "Creating new economy types are really easy to do!",
                "Each part of the economy is separated by a ;",
                "You can see below how they currently work.",
                "If I wanted to add a new economy, I could do the following:",
                "eggs;Eggs;10;EGG;3",
                "Explanation of this:",
                "eggs is the key that will be used in the data to determine the economy type",
                "Eggs is the name that will display in the GUIs throughout the plugin",
                "10 is the start amount for each player",
                "EGG is the material used in the GUIs throughout the plugin",
                "3 is the slot in the GUI that the Egg will show up"
        };
        conf.setComment("economy-types", pluginHeader);
    }

}
