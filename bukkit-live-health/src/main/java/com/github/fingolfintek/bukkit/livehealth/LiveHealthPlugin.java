package com.github.fingolfintek.bukkit.livehealth;

import com.github.fingolfintek.bukkit.util.BukkitSchedulerConstants;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.github.fingolfintek.bukkit.util.BukkitSchedulerConstants.TICS_IN_MINUTE;

public class LiveHealthPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new BukkitRunnable(){
            @Override
            public void run() {
            }
        }.runTaskTimer(this, TICS_IN_MINUTE, TICS_IN_MINUTE);
    }
}
