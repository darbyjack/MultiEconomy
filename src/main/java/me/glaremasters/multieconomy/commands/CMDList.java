package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkPerms;
import static me.glaremasters.multieconomy.util.ColorUtil.color;
import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 5/24/2018.
 */
public class CMDList implements CommandExecutor {

    private FileConfiguration c = MultiEconomy.getI().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            sender.sendMessage(color(c.getString("messages.commands.mebalance.invalid-args")));
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!checkPerms(player, "me.list")) return true;
        }

        StringBuilder sb = new StringBuilder();

        for (String type : c.getStringList("economy-types")) {
            sb.append("\n" + type);
        }
        sender.sendMessage(color(c.getString("messages.commands.melist.result").replace("{currencies}", sb.toString())));
        return true;
    }

}
