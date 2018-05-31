package me.glaremasters.multieconomy.events;

import me.glaremasters.multieconomy.MultiEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by GlareMasters on 5/24/2018.
 */
public class JoinEvent implements Listener {

    private MultiEconomy i;

    public JoinEvent(MultiEconomy i) {
        this.i = i;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        i.getDatabaseProvider().addUser(event.getPlayer());
        /*
        FileConfiguration dC = i.dataFileConfig;
        FileConfiguration c= i.getConfig();
        String UUID = event.getPlayer().getUniqueId().toString();
        for (String type : c.getStringList("economy-types")) {
            if (dC.get(UUID + "." + type) == null) {
                setAmount(UUID, type, c.getInt(type + ".start_amount"));
            }
        }
        */
    }

}
