package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.util.ColorUtil.color;
import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 5/24/2018.
 */
public class CMDBalance implements CommandExecutor {

    private FileConfiguration c = MultiEconomy.getI().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(color(c.getString("messages.commands.mebalance.invalid-args")));
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("me.balance")) {
                player.sendMessage(color(c.getString("messages.error.no-permission")));
                return true;
            }
        }
        String econType = args[0];

        if (!c.getStringList("economy-types").contains(econType)) {
            sender.sendMessage(color(c.getString("messages.error.eco-doesnt-exist")));
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        if (offlinePlayer == null) {
            sender.sendMessage(color(c.getString("messages.error.player-doesnt-exist")));
            return true;
        }
        String UUID = offlinePlayer.getUniqueId().toString();

        if (MultiEconomy.getI().dataFileConfig.get(UUID + "." + econType) == null) {
            sender.sendMessage(color(c.getString("messages.error.data-doesnt-exist")));
            return true;
        }

        String result = MultiEconomy.getI().dataFileConfig.get(UUID + "." + econType).toString();

        sender.sendMessage(color(c.getString("messages.commands.mebalance.result")
                .replace("{user}", offlinePlayer.getName())
                .replace("{amount}", result)
                .replace("{economy}", econType)));

        return true;
    }

}
