package me.kuma.aooni.listeners.alwayslisteners;

import me.kuma.aooni.mains.AooniManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobListener implements Listener {

    private AooniManager aooniManager;

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }
}
