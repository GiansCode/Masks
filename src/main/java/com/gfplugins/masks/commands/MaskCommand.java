package com.gfplugins.masks.commands;

import com.gfplugins.masks.Masks;
import com.gfplugins.masks.objects.Mask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaskCommand implements CommandExecutor {

    private Masks plugin;

    public MaskCommand(Masks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender.hasPermission("masks.commands.help"))) {
                sender.sendMessage(plugin.getMessageManager().getNoPermission());
                return true;
            }

            for (String help : plugin.getMessageManager().getHelp()) {
                sender.sendMessage(help);
            }

            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!(sender.hasPermission("masks.commands.reload"))) {
                    sender.sendMessage(plugin.getMessageManager().getNoPermission());
                    return true;
                }

                plugin.saveDefaultConfig();
                plugin.reloadConfig();
                plugin.loadConfig();
                sender.sendMessage(plugin.getMessageManager().getReloadSuccess());
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (!(sender.hasPermission("mask.commands.list"))) {
                    sender.sendMessage(plugin.getMessageManager().getNoPermission());
                    return true;
                }

                sender.sendMessage(plugin.getMasks());
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("giveall")) {
                if (!(sender.hasPermission("masks.commands.giveall"))) {
                    sender.sendMessage(plugin.getMessageManager().getNoPermission());
                    return true;
                }
            }

            Mask mask = plugin.getMaskByName(args[1]);

            if (mask == null) {
                sender.sendMessage(plugin.getMessageManager().getNoMaskFound().replace("%mask%", args[1]));
                return true;
            }

            int amount = 0;

            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(plugin.getMessageManager().getNotANumber().replace("%number%", args[2]));
                return true;
            }

            mask.getItemStack().setAmount(amount);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.getInventory().addItem(mask.getItemStack());
                player.sendMessage(plugin.getMessageManager().getReceivedMask()
                        .replace("%sender%", sender.getName())
                        .replace("%mask%", mask.getIdentifier())
                        .replace("%amount%", args[2]));
            }

            sender.sendMessage(plugin.getMessageManager().getGaveAllMask()
                    .replace("%mask%", mask.getIdentifier())
                    .replace("%amount%", args[2]));
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("give")) {
                if (!(sender.hasPermission("masks.commands.give"))) {
                    sender.sendMessage(plugin.getMessageManager().getNoPermission());
                    return true;
                }
            }

            Player player = getPlayer(args[1]);

            if (player == null) {
                return true;
            }

            Mask mask = plugin.getMaskByName(args[2]);

            if (mask == null) {
                sender.sendMessage(plugin.getMessageManager().getNoMaskFound().replace("%mask%", args[2]));
                return true;
            }

            int amount = 0;

            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(plugin.getMessageManager().getNotANumber().replace("%number%", args[3]));
                return true;
            }

            mask.getItemStack().setAmount(amount);
            player.getInventory().addItem(mask.getItemStack());
            player.sendMessage(plugin.getMessageManager().getReceivedMask()
                    .replace("%sender%", sender.getName())
                    .replace("%mask%", mask.getIdentifier())
                    .replace("%amount%", args[3]));

            sender.sendMessage(plugin.getMessageManager().getGaveMask()
                    .replace("%player%", player.getName())
                    .replace("%mask%", mask.getIdentifier())
                    .replace("%amount%", args[3]));
        }

        if (args.length == 2 || args.length > 4) {
            if (!(sender.hasPermission("mask.commands.help"))) {
                sender.sendMessage(plugin.getMessageManager().getNoPermission());
                return true;
            }

            for (String help : plugin.getMessageManager().getHelp()) {
                sender.sendMessage(help);
            }
        }

        return true;
    }

    private Player getPlayer(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }

        return null;
    }
}