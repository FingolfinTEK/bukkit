package com.github.fingolfintek.bukkit;

import com.avaje.ebean.EbeanServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class InventoryRestoreCommandExecutor implements CommandExecutor {

    private final InventoryRestorePlugin inventoryRestorePlugin;
    private final Logger pluginLogger;

    public InventoryRestoreCommandExecutor(InventoryRestorePlugin inventoryRestorePlugin) {
        this.inventoryRestorePlugin = inventoryRestorePlugin;
        this.pluginLogger = inventoryRestorePlugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean returnValue = false;
        if (command.testPermission(sender) && args.length == 2) {
            String playerName = args[0];
            Player player = inventoryRestorePlugin.getServer().getPlayerExact(playerName);

            if (player != null) {
                String timeFrame = args[1];
                if (InventoryRestoreTimeFrame.matches(timeFrame)) {
                    doRestore(sender, player, timeFrame);
                    returnValue = true;
                } else {
                    sendMessageToSender(sender, command.getUsage());
                }
            } else {
                sendMessageToSender(sender, "Could not find player with name " + playerName);
            }
        }

        return returnValue;
    }

    private void doRestore(CommandSender sender, Player player, String timeFrame) {
        String playerName = player.getName();
        InventoryRestoreTimeFrame restoreTimeFrame = new InventoryRestoreTimeFrame(timeFrame);
        PlayerInventorySnapshot snapshot = findInDatabase(playerName, restoreTimeFrame);
        PlayerInventoryUtil.copyInventoryToPlayer(snapshot.getInventory(), player);
        sendMessageToSender(sender, "Restored inventory for player " + playerName);
    }

    private PlayerInventorySnapshot findInDatabase(String playerName, InventoryRestoreTimeFrame restoreTimeFrame) {
        return getDatabase().find(PlayerInventorySnapshot.class)
                .where()
                .ieq("playerName", playerName)
                .between("date", restoreTimeFrame.getLowerBound(), restoreTimeFrame.getUpperBound())
                .findUnique();
    }

    private EbeanServer getDatabase() {
        return inventoryRestorePlugin.getDatabase();
    }

    private void sendMessageToSender(CommandSender sender, String message) {
        pluginLogger.severe(message);
        sender.sendMessage(message);
    }

    private void saveSnapshotForPlayer(Player player) {
        PlayerInventorySnapshot inventorySnapshot = new PlayerInventorySnapshot(player);
        getDatabase().save(inventorySnapshot);
    }

}
