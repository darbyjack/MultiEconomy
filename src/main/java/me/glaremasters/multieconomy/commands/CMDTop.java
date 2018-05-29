package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkArgs;
import static me.glaremasters.multieconomy.api.API.checkEcoType;
import static me.glaremasters.multieconomy.api.API.checkPerms;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Vector;
import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.util.TopUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 5/29/2018.
 */
public class CMDTop implements CommandExecutor {

    private MultiEconomy multiEconomy;

    public CMDTop(MultiEconomy multiEconomy) {
        this.multiEconomy = multiEconomy;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkArgs(sender, args, 1, "metop")) return true;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!checkPerms(player, "me.top")) return true;
            String econType = args[0].toLowerCase();
            if (!checkEcoType(sender, econType)) return true;

            Map<String, Object> list = new TreeMap<>();

            for (String key : multiEconomy.dataFileConfig.getKeys(false)) {
                list.put(key, multiEconomy.dataFileConfig.get(key + "." + econType));
            }
            TopUtil topUtil = new TopUtil(list);
            Map<String, Object> newMap = new TreeMap(topUtil);
            newMap.putAll(list);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < newMap.size(); i++) {
                String ID = new Vector(newMap.keySet()).get(i).toString();
                String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(ID)).getName();
                String value = new Vector(newMap.values()).get(i).toString();
                sb.append("\n" + name + " - " + value);
            }
            player.sendMessage(sb.toString());

        }
        return true;
    }

}
