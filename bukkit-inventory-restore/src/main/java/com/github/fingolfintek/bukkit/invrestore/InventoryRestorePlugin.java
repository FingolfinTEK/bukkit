package com.github.fingolfintek.bukkit.invrestore;

import com.github.fingolfintek.bukkit.invrestore.command.InventoryRestoreCommandExecutor;
import com.github.fingolfintek.bukkit.invrestore.dao.InventorySnapshotDao;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;

public final class InventoryRestorePlugin extends JavaPlugin {

    private static final long SECOND_IN_TICKS = 20;
    private static final long MINUTE_IN_TICKS = 60 * SECOND_IN_TICKS;

    private final InventorySnapshotDao snapshotDao = new InventorySnapshotDao(this);

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
        } catch (PersistenceException ex) {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    private void setUpCommands() {
        getCommand("invrestore").setExecutor(new InventoryRestoreCommandExecutor(this));
    }

    private void scheduleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                snapshotDao.saveSnapshotsForAll();
            }
        }.runTaskTimer(this, MINUTE_IN_TICKS, 5 * MINUTE_IN_TICKS);
    }

    private void logPluginEnable() {
        getLogger().info(getDescription().getFullName() + " has been successfully enabled");
    }
}
