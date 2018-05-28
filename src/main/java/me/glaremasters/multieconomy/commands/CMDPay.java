package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkEcoType;
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
 * Created by GlareMasters on 5/27/2018.
 */
public class CMDPay implements CommandExecutor {

    private MultiEconomy multiEconomy;
    private Integer amount;
    private FileConfiguration c = MultiEconomy.getI().getConfig();

    public CMDPay(MultiEconomy multiEconomy) {
        this.multiEconomy = multiEconomy;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!checkPerms(player, "me.pay")) return true;
            if (args.length != 3) {
                player.sendMessage(color(c.getString("messages.commands.mepay.invalid-args")));
                return true;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (offlinePlayer == null) {
                player.sendMessage(color(c.getString("messages.error.player-doesnt-exist")));
                return true;
            }
            String UUID = offlinePlayer.getUniqueId().toString();
            String econType = args[1].toLowerCase();
            if (!checkEcoType(sender, econType)) return true;
            try {
                this.amount = Integer.valueOf(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(color(c.getString("messages.error.not-valid-number")));
                return true;
            }
            int curBalance = Integer.valueOf(multiEconomy.dataFileConfig.get(player.getUniqueId().toString() + "." + econType).toString());
            if (amount > curBalance) {
                player.sendMessage(color(c.getString("messages.error.not-enough-pay").replace("{economy}", econType)));
                return true;
            }
            int endBal = curBalance - amount;
            int targetBeforeBalance = Integer.valueOf(multiEconomy.dataFileConfig.get(UUID + "." + econType).toString());
            int targetEndBalance = targetBeforeBalance + amount;
            multiEconomy.dataFileConfig.set(player.getUniqueId().toString() + "." + econType, endBal);
            multiEconomy.dataFileConfig.set(UUID + "." + econType, targetEndBalance);
            MultiEconomy.getI().saveData();
            player.sendMessage(color(c.getString("messages.commands.mepay.result")
                    .replace("{user}", offlinePlayer.getName())
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{economy}", econType)));
        }
        return true;
    }

}
