package me.glaremasters.multieconomy;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import me.glaremasters.multieconomy.commands.CMDBalance;
import me.glaremasters.multieconomy.commands.CMDBalances;
import me.glaremasters.multieconomy.commands.CMDGive;
import me.glaremasters.multieconomy.commands.CMDList;
import me.glaremasters.multieconomy.commands.CMDPay;
import me.glaremasters.multieconomy.commands.CMDReset;
import me.glaremasters.multieconomy.commands.CMDSet;
import me.glaremasters.multieconomy.commands.CMDTake;
import me.glaremasters.multieconomy.events.JoinEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MultiEconomy extends JavaPlugin {

    private static MultiEconomy i;

    public static MultiEconomy getI() {
        return i;
    }

    public File dataFile = new File(this.getDataFolder(), "data.yml");
    public YamlConfiguration dataFileConfig = YamlConfiguration.loadConfiguration(this.dataFile);

    @Override
    public void onEnable() {
        i = this;
        saveDefaultConfig();
        saveData();

        getServer().getPluginManager().registerEvents(new JoinEvent(i), this);
        getCommand("mebalance").setExecutor(new CMDBalance(this));
        getCommand("meset").setExecutor(new CMDSet(this));
        getCommand("mereset").setExecutor(new CMDReset(this));
        getCommand("megive").setExecutor(new CMDGive(this));
        getCommand("metake").setExecutor(new CMDTake(this));
        getCommand("melist").setExecutor(new CMDList());
        getCommand("mebalances").setExecutor(new CMDBalances(this));
        getCommand("mepay").setExecutor(new CMDPay(this));
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
}
