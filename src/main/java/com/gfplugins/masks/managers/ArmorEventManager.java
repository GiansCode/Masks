package com.gfplugins.masks.managers;

import com.gfplugins.masks.Masks;
import com.gfplugins.masks.util.ArmorEquipEvent;
import com.gfplugins.masks.util.ArmorUnequipEvent;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class ArmorEventManager implements Listener {

    private ConcurrentMap<UUID, ItemStack[]> contents;

    public ArmorEventManager() {
        this.contents = Maps.newConcurrentMap();

        Bukkit.getOnlinePlayers().stream().forEach(player -> this.getContents().putIfAbsent(player.getUniqueId(), player.getEquipment().getArmorContents()));
    }

    @EventHandler
    public final void onEvent(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Inventory inventory = event.getClickedInventory();

        if (inventory != null && (inventory.getType() == InventoryType.CRAFTING || inventory.getType() == InventoryType.PLAYER) && (event.getSlotType() == InventoryType.SlotType.ARMOR || event.isShiftClick())) {
            this.check((Player)event.getWhoClicked());
        }
    }

    @EventHandler
    public final void onEvent(final PlayerInteractEvent event) {
        final Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            final ItemStack item = event.getItem();

            if (item == null) {
                return;
            }

            final String name = item.getType().name();

            if (name.contains("_HELMET") || name.contains("_CHESTPLATE") || name.contains("_LEGGINGS") || name.contains("_BOOTS")) {
                this.check(event.getPlayer());
            }
        }
    }

    @EventHandler
    public final void onEvent(final PlayerDeathEvent event) {
        this.check(event.getEntity());
    }

    @EventHandler
    public final void onEvent(final PlayerJoinEvent event) {
        this.check(event.getPlayer());
    }

    @EventHandler
    public final void onEvent(final PlayerQuitEvent event) {
        if (this.getContents().containsKey(event.getPlayer().getUniqueId())) {
            this.getContents().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public final void onEvent(final PlayerItemBreakEvent event) {
        this.check(event.getPlayer());
    }

    private void check(final Player player) {
        new BukkitRunnable() {
            public void run() {
                final ItemStack[] now = player.getEquipment().getArmorContents();
                final ItemStack[] saved = getContents().get(player.getUniqueId());

                for (int i = 0; i < now.length; ++i) {
                    if (now[i] == null && saved != null && saved[i] != null) {
                        Bukkit.getPluginManager().callEvent(new ArmorUnequipEvent(player, saved[i]));
                    }
                    else if (now[i] != null && (saved == null || saved[i] == null)) {
                        Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(player, now[i]));
                    }
                    else if (saved != null && now[i] != null && saved[i] != null && !now[i].toString().equalsIgnoreCase(saved[i].toString())) {
                        Bukkit.getPluginManager().callEvent(new ArmorUnequipEvent(player, saved[i]));
                        Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(player, now[i]));
                    }
                }

                getContents().put(player.getUniqueId(), now);
            }
        }.runTaskLater((JavaPlugin.getPlugin(Masks.class)), 1L);
    }

    private ConcurrentMap<UUID, ItemStack[]> getContents() {
        return this.contents;
    }
}