package com.gfplugins.masks.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Mask {

    private String identifier;
    private ItemStack itemStack;
    private List<String> onActions, offActions;

    public Mask(String identifier, ItemStack itemStack, List<String> onActions, List<String> offActions) {
        this.identifier = identifier;
        this.itemStack = itemStack;
        this.onActions = onActions;
        this.offActions = offActions;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<String> getOnActions() {
        return onActions;
    }

    public List<String> getOffActions() {
        return offActions;
    }
}