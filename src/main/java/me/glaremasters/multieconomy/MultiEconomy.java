package me.glaremasters.multieconomy;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import co.aikar.commands.PaperCommandManager;
import me.glaremasters.multieconomy.commands.CommandHelp;
import me.glaremasters.multieconomy.configuration.ConfigurationBuilder;
import me.glaremasters.multieconomy.configuration.MultiEconomyMigrationService;
import me.glaremasters.multieconomy.updater.SpigotUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class MultiEconomy extends JavaPlugin {

    private PaperCommandManager commandManager;
    private SettingsManager settingsManager;

    private static MultiEconomy i;

    public File dataFile = new File(this.getDataFolder(), "data.yml");

    public YamlConfiguration dataFileConfig = YamlConfiguration.loadConfiguration(this.dataFile);

    public static MultiEconomy getI() {
        return i;
    }

    @Override
    public void onEnable() {
        loadConfig();
        saveData();

        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        commandManager.registerCommand(new CommandHelp());

        Metrics metrics = new Metrics(this);

        SpigotUpdater updater = new SpigotUpdater(this, 57245);

        try {
            // If there's an update, tell the user that they can update
            if (updater.checkForUpdates()) {
                getLogger()
                        .info("You appear to be running a version other than our latest stable release."
                                + " You can download our newest version at: " + updater
                                .getResourceURL());
            }
        } catch (Exception e) {
            // If it can't check for an update, tell the user and throw an error.
            getLogger().info("Could not check for updates! Stacktrace:");
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {

    }


    public void saveData() {
        try {
            dataFileConfig.save(dataFile);
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Could not save data file!");
            e.printStackTrace();
        }
    }

    /**
     * Load the config
     */
    public void loadConfig() {
        settingsManager = SettingsManagerBuilder
                .withYamlFile(new File(getDataFolder(), "config.yml"))
                .migrationService(new MultiEconomyMigrationService())
                .configurationData(ConfigurationBuilder.buildConfig())
                .create();
    }
}
