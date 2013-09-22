package com.github.fingolfintek.bukkit.invrestore.dao;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Transaction;
import com.github.fingolfintek.bukkit.invrestore.InventoryRestorePlugin;
import com.github.fingolfintek.bukkit.invrestore.InventorySnapshot;
import com.github.fingolfintek.bukkit.invrestore.RestoreTimeFrame;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Logger;

public class InventorySnapshotDao {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final EbeanServer database;
    private final InventoryRestorePlugin inventoryRestorePlugin;

    public InventorySnapshotDao(InventoryRestorePlugin inventoryRestorePlugin) {
        this.database = inventoryRestorePlugin.getDatabase();
        this.inventoryRestorePlugin = inventoryRestorePlugin;
    }

    public InventorySnapshot findByPlayerNameAndTimeFrame(String playerName, RestoreTimeFrame timeFrame) {
        return findAllByPlayerNameAndTimeFrame(playerName, timeFrame).get(0);
    }

    public List<InventorySnapshot> findAllByPlayerNameAndTimeFrame(String playerName, RestoreTimeFrame timeFrame) {
        return toExpressionList(playerName, timeFrame).orderBy().asc("id").findList();
    }

    private ExpressionList<InventorySnapshot> toExpressionList(String playerName, RestoreTimeFrame timeFrame) {
        return database.find(InventorySnapshot.class)
                       .where()
                       .ieq("playerName", playerName)
                       .between("snapshotDate", timeFrame.getLowerBound(), timeFrame.getUpperBound());
    }

    public void save(InventorySnapshot inventorySnapshot) {
        database.save(inventorySnapshot);
        logger.info("Successfully saved inventory snapshot " + inventorySnapshot);
    }

    public void saveSnapshotsForAll(Player[] onlinePlayers) {
        Transaction transaction = database.beginTransaction();
        logger.info("Saving inventory snapshots for players");

        for (Player player : onlinePlayers) {
            saveSnapshotForPlayer(player);
        }

        transaction.commit();
        logger.info("Finished saving inventory snapshots for players");
    }

    private void saveSnapshotForPlayer(final Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                save(new InventorySnapshot(player));
            }
        }.runTaskAsynchronously(inventoryRestorePlugin);
    }

}
