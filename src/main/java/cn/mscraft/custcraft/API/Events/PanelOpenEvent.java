package cn.mscraft.custcraft.API.Events;

import cn.mscraft.custcraft.Model.Panel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PanelOpenEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Panel panel;

    private Player player;

    private boolean cancelled;

    public PanelOpenEvent(Panel panel, Player player) {
        this.panel = panel;
        this.player = player;
    }

    public Panel getPanel() {
        return this.panel;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
