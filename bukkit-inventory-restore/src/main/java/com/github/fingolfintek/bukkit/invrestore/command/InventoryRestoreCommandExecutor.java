package com.github.fingolfintek.bukkit.invrestore.command;

import com.avaje.ebean.EbeanServer;
import com.github.fingolfintek.bukkit.invrestore.InventoryRestorePlugin;
import com.github.fingolfintek.bukkit.invrestore.InventorySnapshot;
import com.github.fingolfintek.bukkit.invrestore.RestoreTimeFrame;
import com.github.fingolfintek.bukkit.invrestore.dao.InventorySnapshotDao;
import com.github.fingolfintek.bukkit.util.PlayerInventoryUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class InventoryRestoreCommandExecutor implements CommandExecutor {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final InventorySnapshotDao snapshotDao;
    private final InventoryRestorePlugin inventoryRestorePlugin;

    public InventoryRestoreCommandExecutor(InventoryRestorePlugin inventoryRestorePlugin) {
        this.snapshotDao = new InventorySnapshotDao(inventoryRestorePlugin);
        this.inventoryRestorePlugin = inventoryRestorePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean returnValue = false;

        if (command.testPermission(sender)) {
            try {
                Player player = determinePlayer(sender, args);
                String timeFrame = determineTimeFrame(sender, args);
                restoreInventoryToPlayerUsingTimeFrame(sender, player, timeFrame);
                sendMessageToSender(sender, "Restored inventory for player " + player.getName());
                returnValue = true;
            } catch (Exception e) {
                sendMessageToSender(sender, e.getMessage());
            }
        }

        return returnValue;
    }

    private Player determinePlayer(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            final String playerName = args[0];
            final Player player = inventoryRestorePlugin.getServer().getPlayerExact(playerName);

            if (player == null)
                throw new IllegalArgumentException("Could not find player with name " + playerName);

            return player;
        }
    }

    private String determineTimeFrame(CommandSender sender, String[] args) {
        return sender instanceof Player ? args[0] : args[1];
    }

    private void restoreInventoryToPlayerUsingTimeFrame(CommandSender sender, Player player, String timeFrame) {
        doRestore(sender, player, timeFrame);
    }

    private void doRestore(CommandSender sender, Player player, String timeFrame) {
        RestoreTimeFrame restoreTimeFrame = new RestoreTimeFrame(timeFrame);
        InventorySnapshot snapshot = snapshotDao.findByPlayerNameAndTimeFrame(player.getName(), restoreTimeFrame);

        if (snapshot == null) {
            throw new IllegalArgumentException("No inventory snapshots found for time frame " + restoreTimeFrame);
        } else {
            PlayerInventoryUtil.copyInventoryToPlayer(snapshot.getInventory(), player);
            player.sendMessage("Restored inventory from snapshot taken on " + snapshot.getSnapshotDate());
        }
    }

    private void sendMessageToSender(CommandSender sender, String message) {
        logger.info(message);
        sender.sendMessage(message);
    }

}
