package com.github.fingolfintek.bukkit.invrestore.command;

import com.avaje.ebean.EbeanServer;
import com.github.fingolfintek.bukkit.invrestore.InventoryRestorePlugin;
import com.github.fingolfintek.bukkit.invrestore.InventorySnapshot;
import com.github.fingolfintek.bukkit.invrestore.RestoreTimeFrame;
import com.github.fingolfintek.bukkit.util.PlayerInventoryUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class InventoryRestoreCommandExecutor implements CommandExecutor {

    private final InventoryRestorePlugin inventoryRestorePlugin;
    private final Logger logger;

    public InventoryRestoreCommandExecutor(InventoryRestorePlugin inventoryRestorePlugin) {
        this.inventoryRestorePlugin = inventoryRestorePlugin;
        this.logger = inventoryRestorePlugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean returnValue = false;
        if (command.testPermission(sender) && args.length == 2) {
            String playerName = args[0];
            Player player = inventoryRestorePlugin.getServer().getPlayerExact(playerName);

            if (player != null) {
                String timeFrame = args[1];
                if (RestoreTimeFrame.matches(timeFrame)) {
                    doRestore(sender, player, timeFrame);
                    returnValue = true;
                } else {
                    sendMessageToSender(sender, "Not a valid time frame " + timeFrame);
                }
            } else {
                sendMessageToSender(sender, "Could not find player with name " + playerName);
            }
        }

        return returnValue;
    }

    private void doRestore(CommandSender sender, Player player, String timeFrame) {
        String playerName = player.getName();
        RestoreTimeFrame restoreTimeFrame = new RestoreTimeFrame(timeFrame);
        InventorySnapshot snapshot = findInDatabase(playerName, restoreTimeFrame);

        if (snapshot == null) {
            sendMessageToSender(sender, "No inventory snapshots found for time frame " + restoreTimeFrame);
        } else {
            PlayerInventoryUtil.copyInventoryToPlayer(snapshot.getInventory(), player);
            sendMessageToSender(sender, "Restored inventory for player " + playerName);
            player.sendMessage("Restored inventory from snapshot taken on " + snapshot.getSnapshotDate());
        }
    }

    private InventorySnapshot findInDatabase(String playerName, RestoreTimeFrame restoreTimeFrame) {
        return getDatabase().find(InventorySnapshot.class)
                .where()
                .ieq("playerName", playerName)
                .between("snapshotDate", restoreTimeFrame.getLowerBound(), restoreTimeFrame.getUpperBound())
                .orderBy()
                .asc("id")
                .findList()
                .get(0);
    }

    private EbeanServer getDatabase() {
        return inventoryRestorePlugin.getDatabase();
    }

    private void sendMessageToSender(CommandSender sender, String message) {
        logger.info(message);
        sender.sendMessage(message);
    }

}
