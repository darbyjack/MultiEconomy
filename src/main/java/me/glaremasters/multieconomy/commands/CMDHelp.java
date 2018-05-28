package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.util.ColorUtil.color;
import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 5/28/2018.
 */
public class CMDHelp implements CommandExecutor {

    private FileConfiguration c = MultiEconomy.getI().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("me.help")) {
                player.sendMessage(color(c.getString("messages.error.no-permission")));
                return true;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String msg : c.getStringList("messages.help")) {
            sb.append("\n" + color(msg));
        }
        sender.sendMessage(sb.toString());
        return true;
    }


}
