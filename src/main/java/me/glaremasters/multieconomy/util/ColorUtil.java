package me.glaremasters.multieconomy.util;

import org.bukkit.ChatColor;

/**
 * Created by GlareMasters on 5/24/2018.
 */
public class ColorUtil {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
