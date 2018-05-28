package me.glaremasters.multieconomy.api;

import static me.glaremasters.multieconomy.util.ColorUtil.color;
import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 5/28/2018.
 */
public class API {

    private static FileConfiguration c = MultiEconomy.getI().getConfig();

    /**
     * Check the permissions of the player running the command
     * @param player the player running the command
     * @param permission the permission to check if the player has
     * @return if true / false based on if the user has the permission or not
     */
    public static boolean checkPerms(Player player, String permission) {
        if (player.hasPermission(permission)) return true;
        player.sendMessage(color(c.getString("messages.error.no-permission")));
        return false;
    }

    /**
     * Check if the economy type exist on the server
     * @param sender the person who sent the command
     * @param type the type of economy to check
     * @return if true / false based on if the economy type exist
     */
    public static boolean checkEcoType(CommandSender sender, String type) {
        if (c.getStringList("economy-types").contains(type)) return true;
        sender.sendMessage(color(c.getString("messages.error.eco-doesnt-exist")));
        return false;
    }

    /**
     * Check if user has economy data for that type
     * @param sender the person who sent the command
     * @param UUID the UUID of the player to check
     * @param econType the economy type
     * @return if true / false based on if the user has economy data for the specified currency
     */
    public static boolean checkDataExist(CommandSender sender, String UUID, String econType) {
        if (MultiEconomy.getI().dataFileConfig.get(UUID + "." + econType) == null) {
            sender.sendMessage(color(c.getString("messages.error.data-doesnt-exist")));
            return false;
        }
        return true;
    }

    /**
     * Check if a player exist in the server data
     * @param sender the person who sent the command
     * @param offlinePlayer the offline player to check
     * @return if true / false based on if the user has existed on the server
     */
    public static boolean checkPlayerExist(CommandSender sender, OfflinePlayer offlinePlayer) {
        if (offlinePlayer == null) {
            sender.sendMessage(color(c.getString("messages.error.player-doesnt-exist")));
            return false;
        }
        return true;
    }

}
