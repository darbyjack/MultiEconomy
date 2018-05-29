package me.glaremasters.multieconomy.commands;

import static me.glaremasters.multieconomy.api.API.checkArgs;
import static me.glaremasters.multieconomy.api.API.checkPerms;
import static me.glaremasters.multieconomy.api.API.checkPlayerExist;
import static me.glaremasters.multieconomy.api.API.getAmount;
import static me.glaremasters.multieconomy.util.ColorUtil.color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by GlareMasters on 5/24/2018.
 */
public class CMDBalances implements CommandExecutor {

    private FileConfiguration c = MultiEconomy.getI().getConfig();
    private MultiEconomy multiEconomy;

    public CMDBalances(MultiEconomy multiEconomy) {
        this.multiEconomy = multiEconomy;
    }

    public static List<UUID> UUIDS = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkArgs(sender, args, 1, "mebalances")) {
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!checkPerms(player, "me.balances")) {
                return true;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (!checkPlayerExist(sender, offlinePlayer)) {
                return true;
            }
            String UUID = offlinePlayer.getUniqueId().toString();
            Inventory balances = Bukkit.createInventory(null, c.getInt("balances-gui.size"),
                    color(c.getString("balances-gui.title").replace("{name}", offlinePlayer.getName())));

            for (String type : c.getStringList("economy-types")) {
                ItemStack item = new ItemStack(Material.getMaterial(c.getString(type + ".item")));
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(color(c.getString("balances-gui.items.name")
                        .replace("{economy}", c.getString(type + ".name"))
                        .replace("{amount}", getAmount(UUID, type))
                        .replace("{symbol}", c.getString(type + ".symbol"))));
                item.setItemMeta(itemMeta);
                balances.setItem(c.getInt(type + ".slot"), item);
            }
            player.openInventory(balances);
            UUIDS.add(player.getUniqueId());
            return true;
        }
        return true;
    }

}
