package com.github.fingolfintek.bukkit.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerInventoryUtil {

    public static void copyInventoryToPlayer(List<ItemStack[]> sourceInventory, Player destination) {
        PlayerInventory destinationInventory = destination.getInventory();
        destinationInventory.clear();
        destinationInventory.setArmorContents(sourceInventory.get(0));
        destinationInventory.setContents(sourceInventory.get(1));
        destination.updateInventory();
    }

    public static List<ItemStack[]> getItemStacks(PlayerInventory inventory) {
        List<ItemStack[]> stacks = new ArrayList<ItemStack[]>();
        stacks.add(inventory.getArmorContents());
        stacks.add(inventory.getContents());
        return stacks;
    }
}
