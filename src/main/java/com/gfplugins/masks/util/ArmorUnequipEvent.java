package com.gfplugins.masks.util;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ArmorUnequipEvent extends Event {

    private static final HandlerList HANDLER_LIST;
    private final Player player;
    private final ItemStack itemStack;

    public ArmorUnequipEvent(final Player player, final ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public HandlerList getHandlers() {
        return ArmorUnequipEvent.HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return ArmorUnequipEvent.HANDLER_LIST;
    }

    static {
        HANDLER_LIST = new HandlerList();
    }
}
