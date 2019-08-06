package me.glaremasters.multieconomy.configuration.sections;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class GUISettings implements SettingsHolder {

    @Comment("What do you want the name of the balance GUI to be?")
    public static final Property<String> BALANCE_TITLE =
            newProperty("guis.balance.title", "{name}'s balances");

    @Comment("What do you want the size of the balance GUI to be?")
    public static final Property<Integer> BALANCE_SIZE =
            newProperty("guis.balance.size", 9);

    @Comment("WHat do you want the names of the items in the GUI to be?")
    public static final Property<String> BALANCE_ITEM =
            newProperty("guis.balance.item-name", "{economy} - {amount}");

    @Comment("What do you want the name of the leaderboard GUI to be?")
    public static final Property<String> LEADERBOARD_TITLE =
            newProperty("guis.leaderboard.title", "Top players for the economy {economy}");

    @Comment("What do you want the size of the leaderboard GUI to be?")
    public static final Property<Integer> LEADERBOARD_SIZE =
            newProperty("guis.leaderboard.size", 9);

    @Comment("WHat do you want the names of the items in the GUI to be?")
    public static final Property<String> LEADERBOARD_ITEM =
            newProperty("guis.leaderboard.item-name", "{name} » {amount}");


}
