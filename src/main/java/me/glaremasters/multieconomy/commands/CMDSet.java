package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkPerms;
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
public class CMDSet implements CommandExecutor {

    private FileConfiguration c = MultiEconomy.getI().getConfig();
    private Integer amount;

    private MultiEconomy multiEconomy;

    public CMDSet(MultiEconomy multiEconomy) {
        this.multiEconomy = multiEconomy;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(color(c.getString("messages.commands.meset.invalid-args")));
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (checkPerms(player, "me.set")) return true;
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

        if (multiEconomy.dataFileConfig.get(UUID) == null) {
            sender.sendMessage(color(c.getString("messages.error.eco-player-doesnt-exist")));
            return true;
        }

        try {
            this.amount = Integer.valueOf(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(color(c.getString("messages.error.not-valid-number")));
            return true;
        }

        multiEconomy.dataFileConfig.set(UUID + "." + econType, amount);
        MultiEconomy.getI().saveData();

        sender.sendMessage(color(c.getString("messages.commands.meset.result")
                .replace("{user}", offlinePlayer.getName())
                .replace("{amount}", String.valueOf(amount))
                .replace("{economy}", econType)));

        return true;
    }

}
