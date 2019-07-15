package com.gfplugins.masks;

import com.gfplugins.masks.commands.MaskCommand;
import com.gfplugins.masks.listeners.ArmorEquipListener;
import com.gfplugins.masks.listeners.PlayerInteractListener;
import com.gfplugins.masks.managers.ArmorEventManager;
import com.gfplugins.masks.managers.MessageManager;
import com.gfplugins.masks.objects.Mask;
import com.google.common.collect.Lists;
import de.erethon.headlib.HeadLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class Masks<ActionExecutor> extends JavaPlugin {

    private List<Mask> maskList;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        maskList = Lists.newArrayList();
        loadConfig();

        Bukkit.getPluginManager().registerEvents(new ArmorEventManager(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);

        getCommand("mask").setExecutor(new MaskCommand(this));
    }

    @Override
    public void onDisable() {
        maskList = null;
        saveDefaultConfig();
    }

    public void loadConfig() {
        saveDefaultConfig();
        maskList.clear();

        ConfigurationSection section = getConfig().getConfigurationSection("masks");

        if (section == null) {
            return;
        }

        Set<String> masks = section.getKeys(false);

        if (masks == null || masks.isEmpty()) {
            return;
        }

        for (String mask : masks) {
            String texture = getConfig().getString("masks." + mask + ".item.texture");
            String name = getConfig().getString("masks." + mask + ".item.name");
            List<String> lore = getConfig().getStringList("masks." + mask + ".item.lore");
            List<String> onActions = getConfig().getStringList("masks." + mask + ".actions.on");
            List<String> offActions = getConfig().getStringList("masks." + mask + ".actions.off");

            List<String> coloredLore = Lists.newArrayList();

            for (String line : lore) {
                line = ChatColor.translateAlternateColorCodes('&', line);

                coloredLore.add(line);
            }

            ItemStack itemStack = HeadLib.setSkullOwner(new ItemStack(Material.PLAYER_HEAD), UUID.randomUUID(), texture);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            itemMeta.setLore(coloredLore);
            itemStack.setItemMeta(itemMeta);

            getMaskList().add(new Mask(mask, itemStack, onActions, offActions));
        }

        String noPermission = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.no-permission"));
        String reloadSuccess = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.reload-success"));
        String noMaskFound = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.no-mask-found"));
        String notANumber = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.not-a-number"));
        String gaveAllMask = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.gave-all-mask"));
        String gaveMask = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.gave-mask"));
        String receivedMask = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.received-mask"));
        String noPermissionMask = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.no-permission-mask"));

        List<String> help = Lists.newArrayList();

        for (String helpmsg : getConfig().getStringList("messages.help")) {
            helpmsg = ChatColor.translateAlternateColorCodes('&', helpmsg);

            help.add(helpmsg);
        }

        messageManager = new MessageManager(noPermission, reloadSuccess, noMaskFound, notANumber, gaveAllMask, gaveMask, receivedMask, noPermissionMask, help);
    }

    public Mask getMaskByName(String name) {
        for (Mask mask : getMaskList()) {
            if (mask.getIdentifier().equalsIgnoreCase(name)) {
                return mask;
            }
        }

        return null;
    }

    public Mask getMaskByItem(ItemStack itemStack) {
        for (Mask mask : getMaskList()) {
            if (mask.getItemStack().isSimilar(itemStack)) {
                return mask;
            }
        }

        return null;
    }

    public List<Mask> getMaskList() {
        if (maskList == null) {
            return Lists.newArrayList();
        }

        return maskList;
    }

    public String getMasks() {
        StringBuilder result = new StringBuilder();

        for(Mask mask : maskList) {
            result.append(mask.getIdentifier());
            result.append(", ");
        }

        return result.length() > 0 ? result.substring(0, result.length() - 1): "";
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}