package me.glaremasters.multieconomy.events;

import static me.glaremasters.multieconomy.util.ColorUtil.color;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import me.glaremasters.multieconomy.MultiEconomy;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by GlareMasters on 5/28/2018.
 */
public class AnnouncementListener implements Listener {

    private static Set<UUID> ALREADY_INFORMED = new HashSet<>();

    private MultiEconomy multiEconomy;

    public AnnouncementListener(MultiEconomy multiEconomy) {
        this.multiEconomy = multiEconomy;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        multiEconomy.getServer().getScheduler().scheduleAsyncDelayedTask(multiEconomy, () -> {
            if (player.isOp()) {
                if (!ALREADY_INFORMED.contains(player.getUniqueId())) {
                    JSONMessage.create(color(multiEconomy.getConfig().getString("plugin-prefix")
                            + " &fAnnouncements"))
                            .tooltip(multiEconomy.getAnnouncements())
                            .openURL(multiEconomy.getDescription().getWebsite())
                            .send(player);
                    ALREADY_INFORMED.add(player.getUniqueId());
                }
            }
        }, 100L);
    }

}

