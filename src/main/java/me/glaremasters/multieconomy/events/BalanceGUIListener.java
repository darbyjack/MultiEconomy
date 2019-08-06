package me.glaremasters.multieconomy.events;

import static me.glaremasters.multieconomy.commands.CMDBalances.UUIDS;
import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Created by GlareMasters on 5/29/2018.
 */
public class BalanceGUIListener implements Listener {

    private FileConfiguration c = MultiEconomy.getI().getConfig();

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (UUIDS.contains(player.getUniqueId())) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUIDS.remove(player.getUniqueId());
    }

}
