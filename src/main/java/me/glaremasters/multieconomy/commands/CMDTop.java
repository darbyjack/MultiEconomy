package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkArgs;
import static me.glaremasters.multieconomy.api.API.checkEcoType;
import static me.glaremasters.multieconomy.api.API.checkPerms;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
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
        YamlConfiguration dFC = multiEconomy.dataFileConfig;
        if (!checkArgs(sender, args, 1, "metop")) return true;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!checkPerms(player, "me.top")) return true;
            String econType = args[0].toLowerCase();
            if (!checkEcoType(sender, econType)) return true;

            HashMap<String, Integer> data = new HashMap<>();


            for (String key : dFC.getKeys(false)) {
                data.put(key, Integer.valueOf(dFC.get(key + "." + econType).toString()));
            }

            List<Entry<String, Integer>> list = new LinkedList<>(data.entrySet());
            list.sort((o1, o2) -> o2.getValue() - o1.getValue());
            for (int i = 0; i < dFC.getKeys(false).size(); i++) {
                String ID = list.get(i).getKey();
                String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(ID)).getName();
                Bukkit.broadcastMessage(name + " - " + list.get(i).getValue());
            }

        }
        return true;
    }

}
