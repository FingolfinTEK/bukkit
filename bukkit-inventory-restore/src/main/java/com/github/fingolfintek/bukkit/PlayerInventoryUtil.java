package com.github.fingolfintek.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryUtil {

    public static void copyInventoryToPlayer(PlayerInventory sourceInventory, Player destination) {
        PlayerInventory destinationInventory = destination.getInventory();
        destinationInventory.clear();
        destinationInventory.setArmorContents(sourceInventory.getArmorContents());
        destinationInventory.setContents(sourceInventory.getContents());
        destination.updateInventory();
    }
}
