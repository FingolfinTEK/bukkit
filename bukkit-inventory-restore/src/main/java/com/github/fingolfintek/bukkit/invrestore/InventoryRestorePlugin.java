package com.github.fingolfintek.bukkit.invrestore;

import com.avaje.ebean.Transaction;
import com.github.fingolfintek.bukkit.invrestore.command.InventoryRestoreCommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;

public final class InventoryRestorePlugin extends JavaPlugin {

    private static final long SECOND_IN_TICKS = 20;
    private static final long MINUTE_IN_TICKS = 60 * SECOND_IN_TICKS;

    @Override
    public void onDisable() {

    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        return Arrays.<Class<?>>asList(InventorySnapshot.class);
    }

    @Override
    public void onEnable() {
        setUpDatabase();
        setUpCommands();
        scheduleTasks();
        logPluginEnable();
    }

    private void setUpDatabase() {
        try {
            int rowCount = getDatabase().find(InventorySnapshot.class).findRowCount();
            getLogger().info("Database contains " + rowCount + " inventory snapshots");
            for (InventorySnapshot inventorySnapshot : getDatabase().find(InventorySnapshot.class).findList()) {
                getLogger().info("Snapshot " + inventorySnapshot);
            }
        } catch (PersistenceException ex) {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    private void setUpCommands() {
        getCommand("invrestore").setExecutor(new InventoryRestoreCommandExecutor(this));
    }

    private void scheduleTasks() {
        new SaveSnapshotsTask().runTaskTimer(this, MINUTE_IN_TICKS, 5 * MINUTE_IN_TICKS);
    }

    private void logPluginEnable() {
        getLogger().info(getDescription().getFullName() + " has been successfully enabled");
    }

    private class SaveSnapshotsTask extends BukkitRunnable {
        @Override
        public void run() {
            Transaction transaction = getDatabase().beginTransaction();
            getLogger().info("Saving inventory snapshots for players");

            for (Player player : getServer().getOnlinePlayers()) {
                saveSnapshotForPlayer(player);
            }

            transaction.commit();
            getLogger().info("Finished saving inventory snapshots for players");
        }

        private void saveSnapshotForPlayer(Player player) {
            final InventorySnapshot inventorySnapshot = new InventorySnapshot(player);
            new SaveSnapshotTask(inventorySnapshot).runTaskAsynchronously(InventoryRestorePlugin.this);
        }
    }

    private class SaveSnapshotTask extends BukkitRunnable {
        private final InventorySnapshot inventorySnapshot;

        public SaveSnapshotTask(InventorySnapshot inventorySnapshot) {
            this.inventorySnapshot = inventorySnapshot;
        }

        @Override
        public void run() {
            getDatabase().save(inventorySnapshot);
            getLogger().info("Successfully saved inventory snapshot " + inventorySnapshot);
        }
    }
}
