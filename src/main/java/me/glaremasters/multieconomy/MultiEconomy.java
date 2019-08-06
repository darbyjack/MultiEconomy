package me.glaremasters.multieconomy;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import co.aikar.commands.PaperCommandManager;
import me.glaremasters.multieconomy.commands.CMDBalance;
import me.glaremasters.multieconomy.commands.CMDBalances;
import me.glaremasters.multieconomy.commands.CMDGive;
import me.glaremasters.multieconomy.commands.CMDHelp;
import me.glaremasters.multieconomy.commands.CMDList;
import me.glaremasters.multieconomy.commands.CMDPay;
import me.glaremasters.multieconomy.commands.CMDReset;
import me.glaremasters.multieconomy.commands.CMDSet;
import me.glaremasters.multieconomy.commands.CMDTake;
import me.glaremasters.multieconomy.commands.CMDTop;
import me.glaremasters.multieconomy.commands.CMDVersion;
import me.glaremasters.multieconomy.configuration.ConfigurationBuilder;
import me.glaremasters.multieconomy.configuration.MultiEconomyMigrationService;
import me.glaremasters.multieconomy.events.BalanceGUIListener;
import me.glaremasters.multieconomy.events.JoinEvent;
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

        Metrics metrics = new Metrics(this);

        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        getCommand("mebalance").setExecutor(new CMDBalance(this));
        getCommand("meset").setExecutor(new CMDSet(this));
        getCommand("mereset").setExecutor(new CMDReset(this));
        getCommand("megive").setExecutor(new CMDGive(this));
        getCommand("metake").setExecutor(new CMDTake(this));
        getCommand("melist").setExecutor(new CMDList());
        getCommand("mebalances").setExecutor(new CMDBalances(this));
        getCommand("mepay").setExecutor(new CMDPay(this));
        getCommand("mehelp").setExecutor(new CMDHelp());
        getCommand("meversion").setExecutor(new CMDVersion());
        getCommand("metop").setExecutor(new CMDTop(this));

        getServer().getPluginManager().registerEvents(new BalanceGUIListener(), this);

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

    public void updateConfig(String path, Integer version) {
        if (!getConfig().isSet(path) || getConfig().getInt(path) != version) {
            if (getConfig().getBoolean("auto-update-config")) {
                File oldConfig = new File(this.getDataFolder(), "config.yml");
                File newConfig = new File(this.getDataFolder(), "config-old.yml");
                oldConfig.renameTo(newConfig);
                getLogger().info("Your config has been auto updated. You can disable this in the config.");
            } else {
                getLogger().info("Your config is out of date!");
            }
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
