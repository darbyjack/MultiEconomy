package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkPerms;
import static me.glaremasters.multieconomy.util.ColorUtil.color;
import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.updater.SpigotUpdater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * Created by GlareMasters on 5/28/2018.
 */
public class CMDVersion implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!checkPerms(player, "me.version")) return true;
        }
        SpigotUpdater updater = new SpigotUpdater(MultiEconomy.getI(), 57245);
        PluginDescriptionFile pdf = MultiEconomy.getI().getDescription();
        try {
            String message;
            if (updater.getLatestVersion().equalsIgnoreCase(pdf.getVersion())) {
                message = "";
            } else {
                message = "\n&8» &7An update has been found! &f- " + updater.getResourceURL();
            }
            sender.sendMessage(
                    color("&8&m--------------------------------------------------"
                            + "\n&8» &7Name - &a"
                            + pdf.getName() + "\n&8» &7Version - &a" + pdf.getVersion()
                            + "\n&8» &7Author - &a" + pdf.getAuthors() + "\n&8» &7Support - &a"
                            + pdf.getWebsite() + message
                            + "\n&8&m--------------------------------------------------"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
