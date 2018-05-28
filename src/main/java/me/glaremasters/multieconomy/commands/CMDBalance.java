package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkDataExist;
import static me.glaremasters.multieconomy.api.API.checkEcoType;
import static me.glaremasters.multieconomy.api.API.checkPerms;
import static me.glaremasters.multieconomy.api.API.checkPlayerExist;
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

    private MultiEconomy multiEconomy;

    public CMDBalance(MultiEconomy multiEconomy) {
        this.multiEconomy = multiEconomy;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(color(c.getString("messages.commands.mebalance.invalid-args")));
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!checkPerms(player, "me.balance")) return true;
        }
        String econType = args[1].toLowerCase();

        if (!checkEcoType(sender, econType)) return true;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (!checkPlayerExist(sender, offlinePlayer)) return true;

        String UUID = offlinePlayer.getUniqueId().toString();

        if (!checkDataExist(sender, UUID, econType)) return true;

        String result = multiEconomy.dataFileConfig.get(UUID + "." + econType).toString();

        sender.sendMessage(color(c.getString("messages.commands.mebalance.result")
                .replace("{user}", offlinePlayer.getName())
                .replace("{amount}", result)
                .replace("{economy}", econType)));

        return true;
    }

}
