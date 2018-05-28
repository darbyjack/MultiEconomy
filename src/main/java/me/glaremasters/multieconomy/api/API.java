package me.glaremasters.multieconomy.api;

import static me.glaremasters.multieconomy.util.ColorUtil.color;
import me.glaremasters.multieconomy.MultiEconomy;
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

    public static boolean checkEcoType(CommandSender sender, String type) {
        if (c.getStringList("economy-types").contains(type)) return true;
        sender.sendMessage(color(c.getString("messages.error.eco-doesnt-exist")));
        return false;
    }

}
