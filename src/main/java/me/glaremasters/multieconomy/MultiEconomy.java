package me.glaremasters.multieconomy;

import static me.glaremasters.multieconomy.util.AnnouncementUtil.unescape_perl_string;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import me.glaremasters.multieconomy.database.DatabaseProvider;
import me.glaremasters.multieconomy.database.databases.mysql.MySQL;
import me.glaremasters.multieconomy.database.databases.yml.YML;
import me.glaremasters.multieconomy.events.AnnouncementListener;
import me.glaremasters.multieconomy.events.BalanceGUIListener;
import me.glaremasters.multieconomy.events.JoinEvent;
import me.glaremasters.multieconomy.metrics.Metrics;
import me.glaremasters.multieconomy.updater.SpigotUpdater;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MultiEconomy extends JavaPlugin {

    private static MultiEconomy i;

    public static MultiEconomy getI() {
        return i;
    }

    public File dataFile;
    public YamlConfiguration dataFileConfig;


    private DatabaseProvider database;

    private static TaskChainFactory taskChainFactory;

    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    @Override
    public void onEnable() {
        i = this;
        updateConfig("version", 1);
        saveDefaultConfig();


        taskChainFactory = BukkitTaskChainFactory.create(this);

        setDatabaseType();

        this.dataFile = new File(getDataFolder(), "storage.yml");
        this.dataFileConfig = YamlConfiguration.loadConfiguration(dataFile);

        if (!dataFile.exists()) {
            this.saveResource("storage.yml", false);
        }

        Metrics metrics = new Metrics(this);

        Bukkit.getConsoleSender().sendMessage(getAnnouncements());
        getServer().getPluginManager().registerEvents(new JoinEvent(i), this);
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
        getCommand("metop").setExecutor(new CMDTop(i));

        getServer().getPluginManager().registerEvents(new AnnouncementListener(this), this);
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

    /**
     * Grab the announcement from the API
     *
     * @return announcement in string text form
     */
    public String getAnnouncements() {
        String announcement = "";
        try {
            URL url = new URL("https://glaremasters.me/api/announcements/multieconomy/?id=" + getDescription()
                    .getVersion());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            try (InputStream in = con.getInputStream()) {
                String encoding = con.getContentEncoding();
                encoding = encoding == null ? "UTF-8" : encoding;
                announcement = unescape_perl_string(IOUtils.toString(in, encoding));
                con.disconnect();
            }
        } catch (Exception exception) {
            announcement = "Could not fetch announcements!";
        }
        return announcement;
    }

    private void updateConfig(String path, Integer version) {
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

    private void setDatabaseType() {
        switch(getConfig().getString("database.type").toLowerCase()) {
            case "mysql":
                database = new MySQL();
                break;
            case "yml":
                database = new YML();
                break;
            default:
                database = new YML();
                break;
        }
        database.initialize();
    }

    public DatabaseProvider getDatabaseProvider() {
        return database;
    }

}
