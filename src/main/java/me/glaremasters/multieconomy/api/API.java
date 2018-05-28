package me.glaremasters.multieconomy.api;

import static me.glaremasters.multieconomy.util.ColorUtil.color;
import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 5/28/2018.
 */
public class API {

    private static FileConfiguration c = MultiEconomy.getI().getConfig();

    public static boolean checkPerms(Player player, String permission) {
        if (player.hasPermission(permission)) return true;
        player.sendMessage(color(c.getString("messages.error.no-permission")));
        return false;
    }

}
