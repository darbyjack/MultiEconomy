package me.glaremasters.multieconomy.events.custom;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by GlareMasters on 5/28/2018.
 */
public final class CustomPayEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private OfflinePlayer player;
    private OfflinePlayer target;
    private boolean cancelled;
    private int amount;
    private String econType;

    public CustomPayEvent(OfflinePlayer player, OfflinePlayer target, String econType, int amount) {
        this.player = player;
        this.target = target;
        this.econType = econType;
        this.amount = amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public OfflinePlayer getTarget() {
        return target;
    }

    public int getAmount() {
        return amount;
    }

    public String getEconType() {
        return econType;
    }
}
