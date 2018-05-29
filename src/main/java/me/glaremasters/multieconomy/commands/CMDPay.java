package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkArgs;
import static me.glaremasters.multieconomy.api.API.checkEcoType;
import static me.glaremasters.multieconomy.api.API.checkPerms;
import static me.glaremasters.multieconomy.api.API.checkPlayerExist;
import static me.glaremasters.multieconomy.api.API.setAmount;
import static me.glaremasters.multieconomy.util.ColorUtil.color;
import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.events.custom.CustomPayEvent;
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
            if (!checkArgs(sender, args, 3, "mepay")) return true;

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (!checkPlayerExist(sender, offlinePlayer)) return true;
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

            CustomPayEvent event = new CustomPayEvent(player, offlinePlayer, amount);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                setAmount(player.getUniqueId().toString(), econType, endBal);
                setAmount(UUID, econType, targetEndBalance);
            }
            else {
                return true;
            }
            player.sendMessage(color(c.getString("messages.commands.mepay.result")
                    .replace("{user}", offlinePlayer.getName())
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{economy}", econType)));
        }
        return true;
    }

}
