package me.glaremasters.multieconomy.commands;

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
public class CMDBalances implements CommandExecutor {

    private FileConfiguration c = MultiEconomy.getI().getConfig();
    private MultiEconomy multiEconomy;

    public CMDBalances(MultiEconomy multiEconomy) {
        this.multiEconomy = multiEconomy;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(color(c.getString("messages.commands.mebalances.invalid-args")));
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!checkPerms(player, "me.balances")) return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (!checkPlayerExist(sender, offlinePlayer)) return true;
        String UUID = offlinePlayer.getUniqueId().toString();

        StringBuilder sb = new StringBuilder();

        for (String type : c.getStringList("economy-types")) {
            sb.append("\n" + c.getString(type + ".symbol") + multiEconomy.dataFileConfig.get(UUID + "." + type) + " " + c.getString(type + ".name"));
        }
        sender.sendMessage(color(c.getString("messages.commands.mebalances.result").replace("{currencies}", sb.toString()).replace("{user}", offlinePlayer.getName())));
        return true;
    }

}
