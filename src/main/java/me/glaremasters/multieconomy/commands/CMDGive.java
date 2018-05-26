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
public class CMDGive implements CommandExecutor {

    private FileConfiguration c = MultiEconomy.getI().getConfig();
    Integer amount;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(color(c.getString("messages.commands.megive.invalid-args")));
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("me.give")) {
                player.sendMessage(color(c.getString("messages.error.no-permission")));
                return true;
            }
        }
        String econType = args[1].toLowerCase();

        if (!c.getStringList("economy-types").contains(econType)) {
            sender.sendMessage(color(c.getString("messages.error.eco-doesnt-exist")));
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (offlinePlayer == null) {
            sender.sendMessage(color(c.getString("messages.error.player-doesnt-exist")));
            return true;
        }
        String UUID = offlinePlayer.getUniqueId().toString();

        if (MultiEconomy.getI().dataFileConfig.get(UUID) == null) {
            sender.sendMessage(color(c.getString("messages.error.eco-player-doesnt-exist")));
            return true;
        }

        try {
            this.amount = Integer.valueOf(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(color(c.getString("messages.error.not-valid-number")));
            return true;
        }

        int beforeAmount = Integer.valueOf(MultiEconomy.getI().dataFileConfig.get(UUID + "." + econType).toString());
        int endAmount = beforeAmount + amount;

        MultiEconomy.getI().dataFileConfig.set(UUID + "." + econType, endAmount);
        MultiEconomy.getI().saveData();

        sender.sendMessage(color(c.getString("messages.commands.megive.result")
                .replace("{user}", offlinePlayer.getName())
                .replace("{amount}", String.valueOf(endAmount))
                .replace("{economy}", econType)));

        return true;
    }

}
