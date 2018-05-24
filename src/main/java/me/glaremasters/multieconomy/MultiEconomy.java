package me.glaremasters.multieconomy;

import org.bukkit.plugin.java.JavaPlugin;

public final class MultiEconomy extends JavaPlugin {

    private static MultiEconomy i;

    public static MultiEconomy getI() {
        return i;
    }

    @Override
    public void onEnable() {
        i = this;
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {

    }
}
