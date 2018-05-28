package me.glaremasters.multieconomy;

import static me.glaremasters.multieconomy.util.AnnouncementUtil.unescape_perl_string;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import me.glaremasters.multieconomy.commands.CMDBalance;
import me.glaremasters.multieconomy.commands.CMDBalances;
import me.glaremasters.multieconomy.commands.CMDGive;
import me.glaremasters.multieconomy.commands.CMDHelp;
import me.glaremasters.multieconomy.commands.CMDList;
import me.glaremasters.multieconomy.commands.CMDPay;
import me.glaremasters.multieconomy.commands.CMDReset;
import me.glaremasters.multieconomy.commands.CMDSet;
import me.glaremasters.multieconomy.commands.CMDTake;
import me.glaremasters.multieconomy.events.AnnouncementListener;
import me.glaremasters.multieconomy.events.JoinEvent;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
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

        getServer().getPluginManager().registerEvents(new AnnouncementListener(this), this);



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

    public void saveData() {
        try {
            dataFileConfig.save(dataFile);
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Could not save data file!");
            e.printStackTrace();
        }
    }
}
