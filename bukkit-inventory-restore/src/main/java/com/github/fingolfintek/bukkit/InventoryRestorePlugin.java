package com.github.fingolfintek.bukkit;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;

public final class InventoryRestorePlugin extends JavaPlugin {

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        setUpDatabase();
        setUpCommands();
        logPluginEnable();
    }

    private void logPluginEnable() {
        PluginDescriptionFile desc = getDescription();
        getLogger().info(desc.getFullName() + " has been successfully enabled");
    }

    private void setUpCommands() {
        getCommand("invrestore").setExecutor(new InventoryRestoreCommandExecutor(this));
    }

    private void setUpDatabase() {
        try {
            getDatabase().find(PlayerInventorySnapshot.class).findRowCount();
        } catch (PersistenceException ex) {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }
}
