package com.gfplugins.masks.listeners;

import com.gfplugins.masks.Masks;
import com.gfplugins.masks.events.MaskEquipEvent;
import com.gfplugins.masks.events.MaskUnequipEvent;
import com.gfplugins.masks.objects.Mask;
import com.gfplugins.masks.util.ArmorEquipEvent;
import com.gfplugins.masks.util.ArmorUnequipEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArmorEquipListener implements Listener {

    private Masks plugin;

    public ArmorEquipListener(Masks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        Mask mask = plugin.getMaskByItem(event.getItemStack());

        if (mask == null) {
            return;
        }

        if (!(event.getPlayer().hasPermission("masks.use." + mask.getIdentifier()))) {
            event.getPlayer().sendMessage(plugin.getMessageManager().getNoPermissionMask().replace("%mask%", mask.getIdentifier()));
            return;
        }

        MaskEquipEvent maskEquipEvent = new MaskEquipEvent(event.getPlayer(), mask);

        Bukkit.getPluginManager().callEvent(maskEquipEvent);

        if (maskEquipEvent.isCancelled()) {
            return;
        }

        for (String actions : mask.getOnActions()) {
            event.getPlayer().sendMessage(actions);

            if (actions.startsWith("[CONSOLE]")) {
                actions = actions.replace("[CONSOLE] ", "").replace("%player%", event.getPlayer().getName());

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), actions);
            }

            if (actions.startsWith("[PLAYER]")) {
                actions = actions.replace("[PLAYER] ", "").replace("%player%", event.getPlayer().getName());

                event.getPlayer().performCommand(actions);
            }
        }
    }

    @EventHandler
    public void onArmorUnequip(ArmorUnequipEvent event) {
        Mask mask = plugin.getMaskByItem(event.getItemStack());

        if (mask == null) {
            return;
        }

        if (!(event.getPlayer().hasPermission("masks.use." + mask.getIdentifier()))) {
            event.getPlayer().sendMessage(plugin.getMessageManager().getNoPermissionMask().replace("%mask%", mask.getIdentifier()));
            return;
        }

        MaskUnequipEvent maskUnequipEvent = new MaskUnequipEvent(event.getPlayer(), mask);

        Bukkit.getPluginManager().callEvent(maskUnequipEvent);

        if (maskUnequipEvent.isCancelled()) {
            return;
        }

        for (String actions : mask.getOffActions()) {
            if (actions.startsWith("[CONSOLE]")) {
                actions = actions.replace("[CONSOLE] ", "").replace("%player%", event.getPlayer().getName());

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), actions);
            }

            if (actions.startsWith("[PLAYER]")) {
                actions = actions.replace("[PLAYER] ", "").replace("%player%", event.getPlayer().getName());

                event.getPlayer().performCommand(actions);
            }
        }
    }
}