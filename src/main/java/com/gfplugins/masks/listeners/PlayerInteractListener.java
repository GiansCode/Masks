package com.gfplugins.masks.listeners;

import com.gfplugins.masks.Masks;;
import com.gfplugins.masks.objects.Mask;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerInteractListener implements Listener {

    private Masks plugin;

    public PlayerInteractListener(Masks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
       if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
           return;
       }

       if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
           return;
       }

       if (event.getClickedBlock() == null) {
           return;
       }

       Mask mask = plugin.getMaskByItem(event.getItem());

       if (mask == null) {
           return;
       }

       event.setCancelled(true);
    }
}