package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkArgs;
import static me.glaremasters.multieconomy.api.API.checkEcoType;
import static me.glaremasters.multieconomy.api.API.checkPerms;
import static me.glaremasters.multieconomy.commands.CMDBalances.UUIDS;
import static me.glaremasters.multieconomy.util.ColorUtil.color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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
        FileConfiguration c = multiEconomy.getConfig();
        if (!checkArgs(sender, args, 1, "metop")) {
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!checkPerms(player, "me.top")) {
                return true;
            }
            String econType = args[0].toLowerCase();
            if (!checkEcoType(sender, econType)) {
                return true;
            }

            HashMap<String, Integer> data = new HashMap<>();

            for (String key : dFC.getKeys(false)) {
                data.put(key, Integer.valueOf(dFC.get(key + "." + econType).toString()));
            }

            List<Entry<String, Integer>> list = new LinkedList<>(data.entrySet());
            list.sort((o1, o2) -> o2.getValue() - o1.getValue());

            Inventory top = Bukkit.createInventory(null, c.getInt("leaderboard-gui.size"),
                    color(c.getString("leaderboard-gui.title").replace("{economy}", econType)));
            for (int i = 0; i < c.getInt("leaderboard-gui.size"); i++) {
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                String ID = list.get(i).getKey();
                String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(ID)).getName();
                meta.setOwner(name);
                meta.setDisplayName(color(c.getString("leaderboard-gui.items.name")
                        .replace("{name}", name)
                        .replace("{amount}", String.valueOf(list.get(i).getValue()))));
                skull.setItemMeta(meta);
                top.setItem(i, skull);
            }
            player.sendMessage(color(c.getString("messages.commands.metop.wait")));
            Bukkit.getScheduler().runTaskLaterAsynchronously(multiEconomy, () -> {
                player.openInventory(top);
                UUIDS.add(player.getUniqueId());
            }, (20 * 2));



        }
        return true;
    }

}
