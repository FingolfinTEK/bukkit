package com.github.fingolfintek.bukkit.invrestore;

import com.github.fingolfintek.bukkit.invrestore.command.InventoryRestoreCommandExecutor;
import com.github.fingolfintek.bukkit.invrestore.dao.InventorySnapshotDao;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;

public final class InventoryRestorePlugin extends JavaPlugin {

    public static final String RESTORE_COMMAND_NAME = "invrestore";
    private static final long TICS_IN_SECOND = 20;
    private static final long TICS_IN_MINUTE = 60 * TICS_IN_SECOND;

    private InventorySnapshotDao snapshotDao;

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
        snapshotDao = new InventorySnapshotDao(this);

        try {
            int rowCount = getDatabase().find(InventorySnapshot.class).findRowCount();
            getLogger().info("Database contains " + rowCount + " inventory snapshots");
        } catch (PersistenceException ex) {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    private void setUpCommands() {
        getCommand(RESTORE_COMMAND_NAME).setExecutor(new InventoryRestoreCommandExecutor(this));
    }

    private void scheduleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                snapshotDao.saveSnapshotsForAll();
            }
        }.runTaskTimer(this, TICS_IN_MINUTE, 5 * TICS_IN_MINUTE);
    }

    private void logPluginEnable() {
        getLogger().info(getDescription().getFullName() + " has been successfully enabled");
    }
}
