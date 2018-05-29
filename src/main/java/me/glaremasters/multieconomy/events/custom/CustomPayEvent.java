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
    private OfflinePlayer p1;
    private OfflinePlayer p2;
    private boolean cancelled;
    private int number;

    public CustomPayEvent(OfflinePlayer player, OfflinePlayer target, int amount) {
        p1 = player;
        p2 = target;
        number = amount;
    }

    public OfflinePlayer getP1() {
        return p1;
    }

    public OfflinePlayer getP2() {
        return p2;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public int getNumber() {
        return number;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
